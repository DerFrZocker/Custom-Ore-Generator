package de.derfrzocker.custom.ore.generator.impl.v1_10_R1.oregenerator;

import com.google.common.base.Predicate;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractMinableGenerator;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public class MinableGenerator_v1_10_R1 extends AbstractMinableGenerator {

    public final Predicate<IBlockData> blocks = (value) -> {
        if (value == null) {
            return false;
        } else {
            Block block = value.getBlock();
            return block == Blocks.STONE || block == Blocks.END_STONE || block == Blocks.NETHERRACK;
        }
    };

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final World world, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = config.getValue(OreSettings.VEIN_SIZE).orElse(OreSettings.VEIN_SIZE.getSaveValue());

        final CraftWorld craftWorld = (CraftWorld) world;

        final IBlockData blockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();

        final WorldGenMinable generator = new WorldGenMinable(blockData, veinSize, blocks);
        final BlockPosition chunkPosition = new BlockPosition(x << 4, 0, z << 4);

        for (final Location location : locations) {
            generator.generate(craftWorld.getHandle(), random, chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }

    }

}
