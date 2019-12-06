package de.derfrzocker.custom.ore.generator.impl.v1_9_R1.oregenerator;

import com.google.common.base.Predicate;
import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractMinableGenerator;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.ChunkAccessImpl;
import de.derfrzocker.spigot.utils.NumberUtil;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MinableGenerator_v1_9_R1 extends AbstractMinableGenerator {

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = NumberUtil.getInt(config.getValue(OreSettings.VEIN_SIZE).orElse(0d), random);

        if(veinSize == 0)
            return;

        final WorldServer worldServer = ((ChunkAccessImpl) chunkAccess).getWorldServer();

        final IBlockData blockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();
        final Set<Block> blocks = new HashSet<>();

        replaceMaterials.forEach(material -> blocks.add(CraftMagicNumbers.getBlock(material)));
        final WorldGenMinable generator = new WorldGenMinable(blockData, veinSize, getPredicate(blocks));
        final BlockPosition chunkPosition = new BlockPosition(x << 4, 0, z << 4);


        for (final Location location : locations) {
            generator.generate(worldServer, random, chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
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
