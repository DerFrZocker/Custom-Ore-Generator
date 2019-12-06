package de.derfrzocker.custom.ore.generator.impl.v1_13_R2.oregenerator;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractMinableGenerator;
import de.derfrzocker.spigot.utils.NumberUtil;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class MinableGenerator_v1_13_R2 extends AbstractMinableGenerator {

    private final WorldGenMinable generator = new WorldGenMinable();

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = NumberUtil.getInt(config.getValue(OreSettings.VEIN_SIZE).orElse(0d), random);

        if(veinSize == 0)
            return;

        final BlockPosition chunkPosition = new BlockPosition(x << 4, 0, z << 4);
        final GeneratorAccess generatorAccess = (GeneratorAccess) chunkAccess;
        final ChunkGenerator<?> chunkGenerator = generatorAccess.getChunkProvider().getChunkGenerator();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();
        final Set<Block> blocks = new HashSet<>();

        replaceMaterials.forEach(material -> blocks.add(CraftMagicNumbers.getBlock(material)));
        final WorldGenFeatureOreConfiguration worldGenFeatureOreConfiguration = new WorldGenFeatureOreConfiguration(getPredicate(blocks), CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData(), veinSize);

        for (final Location location : locations) {
            generator.generate(generatorAccess, chunkGenerator, random, chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ()), worldGenFeatureOreConfiguration);
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
