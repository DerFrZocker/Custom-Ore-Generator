package de.derfrzocker.custom.ore.generator.impl.v1_8_R3.oregenerator;

import com.google.common.base.Predicate;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractMinableGenerator;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.WorldGenMinable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MinableGenerator_v1_8_R3 extends AbstractMinableGenerator {

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final World world, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = config.getValue(OreSettings.VEIN_SIZE).orElse(OreSettings.VEIN_SIZE.getSaveValue());

        final CraftWorld craftWorld = (CraftWorld) world;

        final IBlockData blockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();
        final Set<Block> blocks = new HashSet<>();

        replaceMaterials.forEach(material -> blocks.add(CraftMagicNumbers.getBlock(material)));
        final WorldGenMinable generator = new WorldGenMinable(blockData, veinSize, getPredicate(blocks));
        final BlockPosition chunkPosition = new BlockPosition(x << 4, 0, z << 4);


        for (final Location location : locations) {
            generator.generate(craftWorld.getHandle(), random, chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }

    }

    private Predicate<IBlockData> getPredicate(@NotNull final Set<Block> blocks) {
        return (value) -> {
            if (value == null) {
                return false;
            } else {
                final Block block = value.getBlock();
                return blocks.contains(block);
            }
        };
    }

}
