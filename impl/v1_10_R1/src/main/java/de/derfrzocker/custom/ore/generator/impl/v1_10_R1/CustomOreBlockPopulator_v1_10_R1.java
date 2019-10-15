package de.derfrzocker.custom.ore.generator.impl.v1_10_R1;

import de.derfrzocker.custom.ore.generator.api.*;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.IBlockData;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CustomOreBlockPopulator_v1_10_R1 extends BlockPopulator implements WorldHandler, Listener {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public CustomOreBlockPopulator_v1_10_R1(@NotNull final JavaPlugin javaPlugin, @NotNull Supplier<CustomOreGeneratorService> serviceSupplier) {
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

        locations.stream().filter(location -> chunk.getBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ()).getBiome() == biome).forEach(biomeLocations::add);

        craftWorld.getHandle().captureTreeGeneration = true;
        craftWorld.getHandle().captureBlockStates = true;

        oreGenerator.generate(oreConfig, craftWorld, chunk.getX(), chunk.getZ(), random, biome, biomeLocations);

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
    public void onWorldLoad(@NotNull final WorldLoadEvent event) {
        if (event.getWorld().getPopulators().contains(this))
            return;

        event.getWorld().getPopulators().add(this);
    }

}
