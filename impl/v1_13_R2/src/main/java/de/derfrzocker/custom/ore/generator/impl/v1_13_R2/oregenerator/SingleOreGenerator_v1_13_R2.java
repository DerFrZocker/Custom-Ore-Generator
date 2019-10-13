package de.derfrzocker.custom.ore.generator.impl.v1_13_R2.oregenerator;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractSingleOreGenerator;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.GeneratorAccessOverrider;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.OreGenerator_v1_13_R2;
import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.IBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SingleOreGenerator_v1_13_R2 extends AbstractSingleOreGenerator implements OreGenerator_v1_13_R2 {

    @Override
    public void generate(@NotNull OreConfig config, @NotNull World world, @NotNull GeneratorAccessOverrider access, @NotNull Random random, @NotNull Biome biome, @NotNull Set<Location> locations) {
        final BlockPosition chunkPosition = new BlockPosition(access.getX() << 4, 0, access.getZ() << 4);
        final IBlockData iBlockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();
        final Set<Block> blocks = new HashSet<>();

        replaceMaterials.forEach(material -> blocks.add(CraftMagicNumbers.getBlock(material)));

        for (final Location location : locations) {
            final BlockPosition blockPosition = chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            final IBlockData blockData = access.getType(blockPosition);
            if (blocks.contains(blockData.getBlock()))
                access.setTypeAndData(blockPosition, iBlockData, 2);
        }
    }

    @Override
    public void generate(@NotNull OreConfig config, @NotNull World world, int x, int z, @NotNull Random random, @NotNull Biome biome, @NotNull Set<Location> locations) {
        throw new UnsupportedOperationException("Not Supported in version 1_13_R1");
    }

}
