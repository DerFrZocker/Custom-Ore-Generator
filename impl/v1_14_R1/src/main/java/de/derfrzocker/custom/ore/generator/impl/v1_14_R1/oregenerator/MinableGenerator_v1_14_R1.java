package de.derfrzocker.custom.ore.generator.impl.v1_14_R1.oregenerator;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractMinableGenerator;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.GeneratorAccessOverrider;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.OreGenerator_v1_14_R1;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.ChunkGenerator;
import net.minecraft.server.v1_14_R1.WorldGenFeatureOreConfiguration;
import net.minecraft.server.v1_14_R1.WorldGenMinable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public class MinableGenerator_v1_14_R1 extends AbstractMinableGenerator implements OreGenerator_v1_14_R1 {

    private final WorldGenMinable generator = new WorldGenMinable(null);

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final World world, @NotNull final GeneratorAccessOverrider access, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = config.getValue(OreSettings.VEIN_SIZE).orElse(OreSettings.VEIN_SIZE.getSaveValue());
        final BlockPosition chunkPosition = new BlockPosition(access.getX() << 4, 0, access.getZ() << 4);
        final ChunkGenerator<?> chunkGenerator = ((CraftWorld) world).getHandle().getChunkProvider().getChunkGenerator();
        final WorldGenFeatureOreConfiguration worldGenFeatureOreConfiguration = new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData(), veinSize);

        for (final Location location : locations) {
            generator.generate(access, chunkGenerator, random, chunkPosition.b(location.getBlockX(), location.getBlockY(), location.getBlockZ()), worldGenFeatureOreConfiguration);
        }
    }

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final World world, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        throw new UnsupportedOperationException("Not Supported in version 1_13_R1");
    }

}
