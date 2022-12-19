/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
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

package de.derfrzocker.custom.ore.generator.impl.v1_19_R2;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftLimitedRegion;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class CustomOrePopulator extends BlockPopulator {

    @NotNull
    private final org.bukkit.World world;
    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final WorldHandler_v1_19_R2 worldHandler;

    public CustomOrePopulator(@NotNull final org.bukkit.World world, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull WorldHandler_v1_19_R2 worldHandler) {
        Validate.notNull(world, "World cannot be null");
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(worldHandler, "WorldHandler can not be null");

        this.world = world;
        this.serviceSupplier = serviceSupplier;
        this.worldHandler = worldHandler;
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        final Set<Biome> biomes = getBiomes(chunkX, chunkZ, limitedRegion);

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

            oreConfigs.forEach(oreConfig -> generate(oreConfig, chunkX, chunkZ, limitedRegion, biome, service));
        });
    }

    private Set<Biome> getBiomes(int chunkX, int chunkZ, LimitedRegion limitedRegion) {
        final Set<Biome> set = new HashSet<>();

        final int x = chunkX << 4;
        final int z = chunkZ << 4;

        for (int x2 = x; x2 < x + 16; x2++)
            for (int z2 = z; z2 < z + 16; z2++) {
                try {
                    set.add(limitedRegion.getBiome(x2, 60, z2));
                } catch (Exception ignored) {
                    System.out.println("Biome not Found"); //TODO test and remove
                }
            }

        return set;
    }

    private void generate(final OreConfig oreConfig, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion, final Biome biome, final CustomOreGeneratorService service) {
        final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(oreConfig.getOreGenerator());
        final Optional<BlockSelector> optionalBlockSelector = service.getBlockSelector(oreConfig.getBlockSelector());
        if (!optionalOreGenerator.isPresent())
            return;

        if (!optionalBlockSelector.isPresent())
            return;

        final OreGenerator oreGenerator = optionalOreGenerator.get();
        final BlockSelector blockSelector = optionalBlockSelector.get();
        final Random random = service.createRandom(world.getSeed() + oreConfig.getMaterial().toString().hashCode() + oreConfig.getName().hashCode(), chunkX, chunkZ);
        final BlockPos blockPosition = new BlockPos(chunkX << 4, 0, chunkZ << 4);

        final Set<Location> locations = blockSelector.selectBlocks((x, z) -> ((CraftLimitedRegion) limitedRegion).getHandle().getHeight(Heightmap.Types.MOTION_BLOCKING, blockPosition.getX() + x, blockPosition.getZ() + z), oreConfig, random);
        final Set<Location> biomeLocations = new HashSet<>();
        final Location chunkPosition = new Location(null, chunkX << 4, 0, chunkZ << 4);
        final Set<Material> replaceMaterials = oreConfig.getReplaceMaterials();
        Set<Material> selectMaterials = oreConfig.getSelectMaterials();
        if (selectMaterials.isEmpty())
            selectMaterials = replaceMaterials;

        Set<Material> finalSelectMaterials = selectMaterials;
        locations.stream().filter(location -> checkBlockAndBiome(limitedRegion, chunkPosition, location, biome, finalSelectMaterials))
                .forEach(biomeLocations::add);

        ChunkAccessImpl chunkAccess = new ChunkAccessImpl(((CraftLimitedRegion) limitedRegion).getHandle());
        oreGenerator.generate(oreConfig, chunkAccess, chunkX, chunkZ, random, biome, biomeLocations);
        chunkAccess.refreshTiles();
        chunkAccess.updateList();
        for (BlockPos pos : chunkAccess.getBlocks()) {
            oreConfig.getCustomData().forEach((customData, object) -> customData.getCustomDataApplier().apply(oreConfig, new Location(null, pos.getX(), pos.getY(), pos.getZ()), limitedRegion));
        }
    }

    private boolean checkBlockAndBiome(final LimitedRegion limitedRegion, final Location chunkPosition, final Location location, final Biome biome, final Set<Material> materials) {
        final Location blockPosition = chunkPosition.clone().add(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        final boolean isBiome = limitedRegion.getBiome(blockPosition) == biome;

        if (!isBiome)
            return false;

        return materials.contains(limitedRegion.getType(blockPosition));
    }
}
