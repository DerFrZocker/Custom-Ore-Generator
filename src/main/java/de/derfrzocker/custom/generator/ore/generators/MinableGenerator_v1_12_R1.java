package de.derfrzocker.custom.generator.ore.generators;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import de.derfrzocker.custom.generator.ore.api.OreConfig;
import de.derfrzocker.custom.generator.ore.api.OreGenerator;
import de.derfrzocker.custom.generator.ore.api.OreSetting;
import lombok.Getter;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;

import java.util.Random;
import java.util.Set;

public class MinableGenerator_v1_12_R1 implements OreGenerator {

    public final Predicate<IBlockData> blocks = (var0) -> {
        if (var0 == null) {
            return false;
        } else {
            Block var1 = var0.getBlock();
            return var1 == Blocks.STONE || var1 == Blocks.END_STONE || var1 == Blocks.NETHERRACK;
        }
    };

    @Getter
    private final Set<OreSetting> neededOreSettings = Sets.newHashSet(OreSetting.VEIN_SIZE, OreSetting.VEINS_PER_CHUNK, OreSetting.HEIGHT_RANGE, OreSetting.MINIMUM_HEIGHT);

    @Getter
    private final String name = "vanilla_minable_generator";

    @Override
    public void generate(OreConfig config, World world, int x2, int z2, Random random, Biome biome) {
        final int veinSize = config.getValue(OreSetting.VEIN_SIZE).orElse(0);
        final int veinsPerChunk = config.getValue(OreSetting.VEINS_PER_CHUNK).orElse(0);
        final int heightRange = config.getValue(OreSetting.HEIGHT_RANGE).orElse(0);
        final int minimumHeight = config.getValue(OreSetting.MINIMUM_HEIGHT).orElse(0);

        final CraftWorld craftWorld = (CraftWorld) world;
        final CraftChunk craftChunk = (CraftChunk) world.getChunkAt(x2, z2);

        WorldGenMinable generator = new WorldGenMinable(CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData(),veinSize, blocks);

        for (int trys = 0; trys < veinsPerChunk; ++trys) {
            int x = random.nextInt(15);
            int y = random.nextInt(heightRange) + minimumHeight;
            int z = random.nextInt(15);

            if (biome == null || craftChunk.getBlock(x, y, z).getBiome() == biome){
                generator.generate(craftWorld.getHandle(), random, new BlockPosition(x + (x2 <<4), y, z + (z2 <<4)));
            }
        }


    }

}
