package de.derfrzocker.custom.ore.generator.impl.v1_14_R1.oregenerator;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractSingleOreGenerator;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.GeneratorAccessOverrider;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.OreGenerator_v1_14_R1;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.IBlockData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public class SingleOreGenerator_v1_14_R1 extends AbstractSingleOreGenerator implements OreGenerator_v1_14_R1 {

    @Override
    public void generate(@NotNull OreConfig config, @NotNull World world, @NotNull GeneratorAccessOverrider access, @NotNull Random random, @NotNull Biome biome, @NotNull Set<Location> locations) {
        final BlockPosition chunkPosition = new BlockPosition(access.getX() << 4, 0, access.getZ() << 4);
        final IBlockData iBlockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();

        for (final Location location : locations) {
            final BlockPosition blockPosition = chunkPosition.b(location.getBlockX(), location.getBlockY(), location.getBlockZ());

            access.setTypeAndData(blockPosition, iBlockData, 2);
        }
    }

    @Override
    public void generate(@NotNull OreConfig config, @NotNull World world, int x, int z, @NotNull Random random, @NotNull Biome biome, @NotNull Set<Location> locations) {
        throw new UnsupportedOperationException("Not Supported in version 1_13_R1");
    }

}