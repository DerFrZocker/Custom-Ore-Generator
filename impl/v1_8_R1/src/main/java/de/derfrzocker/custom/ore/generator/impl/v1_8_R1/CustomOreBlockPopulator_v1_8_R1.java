/*
 * MIT License
 *
 * Copyright (c) 2019 DerFrZocker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.custom.ore.generator.impl.v1_8_R1;

import de.derfrzocker.custom.ore.generator.api.*;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.IBlockData;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.util.CraftMagicNumbers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CustomOreBlockPopulator_v1_8_R1 extends BlockPopulator implements WorldHandler, Listener {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public CustomOreBlockPopulator_v1_8_R1(@NotNull final JavaPlugin javaPlugin, @NotNull Supplier<CustomOreGeneratorService> serviceSupplier) {
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(serviceSupplier, "Service supplier can not be null");

        this.serviceSupplier = serviceSupplier;

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @Override
    public void populate(@NotNull final World world, @NotNull final Random random, @NotNull final Chunk source) {
        final Set<Biome> biomes = getBiomes(source);

        final CustomOreGeneratorService service = serviceSupplier.get();

        final WorldConfig worldConfig;

        {
            final Optional<WorldConfig> optional = service.getWorldConfig(world.getName());

            if (!optional.isPresent())
                return;

            worldConfig = optional.get();
        }

        biomes.forEach(biome -> {
            final List<OreConfig> oreConfigs = Arrays.asList(worldConfig.getOreConfigs().stream().filter(oreConfig -> oreConfig.getBiomes().contains(biome) || oreConfig.shouldGeneratedAll()).filter(OreConfig::isActivated).toArray(OreConfig[]::new));

            oreConfigs.forEach(oreConfig -> generate(oreConfig, (CraftWorld) world, source, biome, service));
        });

    }

    private Set<Biome> getBiomes(final Chunk chunk) {
        final Set<Biome> set = new HashSet<>();

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                set.add(chunk.getBlock(x, 0, z).getBiome());

        return set;
    }

    private void generate(final OreConfig oreConfig, final CraftWorld craftWorld, final Chunk chunk, final Biome biome, final CustomOreGeneratorService service) {
        final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(oreConfig.getOreGenerator());
        final Optional<BlockSelector> optionalBlockSelector = service.getBlockSelector(oreConfig.getBlockSelector());

        if (!optionalOreGenerator.isPresent())
            return;

        if (!optionalBlockSelector.isPresent())
            return;

        final OreGenerator oreGenerator = optionalOreGenerator.get();
        final BlockSelector blockSelector = optionalBlockSelector.get();
        final Random random = service.createRandom(craftWorld.getSeed() + oreConfig.getMaterial().toString().hashCode(), chunk.getX(), chunk.getZ());
        final Location chunkLocation = new Location(null, chunk.getX() << 4, 0, chunk.getZ() << 4);

        final Set<Location> locations = blockSelector.selectBlocks((x, z) -> craftWorld.getHighestBlockYAt(chunkLocation.getBlockX() + x, chunkLocation.getBlockZ() + z), oreConfig, random);
        final Set<Location> biomeLocations = new HashSet<>();
        final Set<org.bukkit.Material> selectMaterials;
        if (oreConfig.getSelectMaterials().isEmpty())
            selectMaterials = oreConfig.getReplaceMaterials();
        else
            selectMaterials = oreConfig.getSelectMaterials();

        locations.stream().filter(location -> checkBlockAndBiome(chunk, location, biome, selectMaterials))
                .forEach(biomeLocations::add);

        craftWorld.getHandle().captureTreeGeneration = true;
        craftWorld.getHandle().captureBlockStates = true;

        oreGenerator.generate(oreConfig, new ChunkAccessImpl(craftWorld.getHandle()), chunk.getX(), chunk.getZ(), random, biome, biomeLocations);

        craftWorld.getHandle().captureTreeGeneration = false;
        craftWorld.getHandle().captureBlockStates = false;

        final IBlockData blockData = CraftMagicNumbers.getBlock(oreConfig.getMaterial()).getBlockData();

        for (org.bukkit.block.BlockState blockState : craftWorld.getHandle().capturedBlockStates) {
            final BlockPosition blockPosition = new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ());

            if (craftWorld.getHandle().setTypeAndData(blockPosition, blockData, 2)) {
                oreConfig.getCustomData().forEach((customData, object) -> customData.getCustomDataApplier().apply(oreConfig, blockPosition, craftWorld.getHandle()));
            }
        }

        craftWorld.getHandle().capturedBlockStates.clear();
    }

    @EventHandler
    public void onWorldLoad(@NotNull final WorldInitEvent event) {
        if (event.getWorld().getPopulators().contains(this))
            return;

        event.getWorld().getPopulators().add(this);
    }

    private boolean checkBlockAndBiome(final Chunk chunk, final Location location, final Biome biome, final Set<Material> materials) {
        final Block block = chunk.getBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        final boolean isBiome = block.getBiome() == biome;

        if (!isBiome)
            return false;

        return materials.contains(block.getType());
    }

}
