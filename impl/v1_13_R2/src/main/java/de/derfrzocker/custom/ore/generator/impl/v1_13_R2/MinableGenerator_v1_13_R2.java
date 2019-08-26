package de.derfrzocker.custom.ore.generator.impl.v1_13_R2;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.api.OreConfig;
import de.derfrzocker.custom.generator.ore.api.OreGenerator;
import de.derfrzocker.custom.generator.ore.api.OreSetting;
import lombok.Getter;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_13_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;

import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class MinableGenerator_v1_13_R2 implements OreGenerator {

    private final WorldGenMinable generator = new WorldGenMinable();

    @Getter
    private final Set<OreSetting> neededOreSettings = Sets.newHashSet(OreSetting.VEIN_SIZE, OreSetting.VEINS_PER_CHUNK, OreSetting.HEIGHT_RANGE, OreSetting.MINIMUM_HEIGHT);

    @Getter
    private final String name = "vanilla_minable_generator";

    public final Predicate<IBlockData> blocks = (value) -> {
        if (value == null) {
            return false;
        } else {
            Block block = value.getBlock();
            return block == Blocks.STONE || block == Blocks.GRANITE || block == Blocks.DIORITE || block == Blocks.ANDESITE || block == Blocks.END_STONE || block == Blocks.NETHERRACK;
        }
    };

    public MinableGenerator_v1_13_R2() {
        CustomOreGenerator.getService().setDefaultOreGenerator(this);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void generate(OreConfig config, World world, int x2, int z2, Random random, Biome biome) {
        final int veinSize = config.getValue(OreSetting.VEIN_SIZE).orElse(OreSetting.VEIN_SIZE.getSaveValue());
        final int veinsPerChunk = config.getValue(OreSetting.VEINS_PER_CHUNK).orElse(OreSetting.VEINS_PER_CHUNK.getSaveValue());
        final int heightRange = config.getValue(OreSetting.HEIGHT_RANGE).orElse(OreSetting.HEIGHT_RANGE.getSaveValue());
        final int minimumHeight = config.getValue(OreSetting.MINIMUM_HEIGHT).orElse(OreSetting.MINIMUM_HEIGHT.getSaveValue());

        final CraftWorld craftWorld = (CraftWorld) world;
        final CraftChunk craftChunk = (CraftChunk) world.getChunkAt(x2, z2);

        if (!craftChunk.getHandle().heightMap.containsKey(HeightMap.Type.OCEAN_FLOOR_WG))
            craftChunk.getHandle().heightMap.put(HeightMap.Type.OCEAN_FLOOR_WG, new HeightMapOverrider(craftChunk.getHandle(), HeightMap.Type.OCEAN_FLOOR_WG));

        for (int trys = 0; trys < veinsPerChunk; ++trys) {
            int x = random.nextInt(15);
            int y = random.nextInt(heightRange) + minimumHeight;
            int z = random.nextInt(15);

            BlockPosition position = new BlockPosition(x + (x2 << 4), y, z + (z2 << 4));

            if (biome == null || craftChunk.getBlock(x, y, z).getBiome() == biome) {
                generator.a(craftWorld.getHandle(), craftWorld.getHandle().getChunkProvider().getChunkGenerator(), random, position, new WorldGenFeatureOreConfiguration(blocks, CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData(), veinSize));
            }
        }

    }

    public void generate(OreConfig config, World world, RegionLimitedWorldAccess access, Random random, Biome biome) {
        final int veinSize = config.getValue(OreSetting.VEIN_SIZE).orElse(OreSetting.VEIN_SIZE.getSaveValue());
        final int veinsPerChunk = config.getValue(OreSetting.VEINS_PER_CHUNK).orElse(OreSetting.VEINS_PER_CHUNK.getSaveValue());
        final int heightRange = config.getValue(OreSetting.HEIGHT_RANGE).orElse(OreSetting.HEIGHT_RANGE.getSaveValue());
        final int minimumHeight = config.getValue(OreSetting.MINIMUM_HEIGHT).orElse(OreSetting.MINIMUM_HEIGHT.getSaveValue());

        final CraftWorld craftWorld = (CraftWorld) world;

        for (int trys = 0; trys < veinsPerChunk; ++trys) {
            int x = random.nextInt(15);
            int y = random.nextInt(heightRange) + minimumHeight;
            int z = random.nextInt(15);

            BlockPosition position = new BlockPosition(x + (access.a() << 4), y, z + (access.b() << 4));

            if (biome == null || biome.toString().equalsIgnoreCase(IRegistry.BIOME.getKey(access.getBiome(position)).getKey())) {
                generator.a(access, craftWorld.getHandle().getChunkProvider().getChunkGenerator(), random, position, new WorldGenFeatureOreConfiguration(blocks, CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData(), veinSize));
            }
        }

    }

    private final class HeightMapOverrider extends HeightMap {
        private HeightMapOverrider(IChunkAccess iChunkAccess, Type type) {
            super(iChunkAccess, type);
        }

        @Override
        public int a(int x, int z) {
            int y = super.a(x, z);

            return y == 0 ? 128 : y;
        }
    }
}
