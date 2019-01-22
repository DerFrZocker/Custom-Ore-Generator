package de.derfrzocker.custom.generator.ore;

import de.derfrzocker.custom.generator.ore.api.*;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class CustomOreBlockPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk source) {
        Set<Biome> biomes = getBiomes(source);

        CustomOreGeneratorService service = CustomOreGenerator.getService();

        WorldConfig worldConfig;

        {
            Optional<WorldConfig> optional = service.getWorldConfig(world.getName());

            if (!optional.isPresent())
                return;

            worldConfig = optional.get();
        }

        biomes.forEach(biome -> {
            Set<OreConfig> oreConfigs = worldConfig.getBiomeConfig(biome).map(BiomeConfig::getOreConfigs).orElseGet(worldConfig::getOreConfigs);

            oreConfigs.forEach(oreConfig -> generate(oreConfig, world, source, biome));
        });

    }

    private Set<Biome> getBiomes(Chunk chunk) {
        Set<Biome> set = new HashSet<>();

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                set.add(chunk.getBlock(x, 0, z).getBiome());

        return set;
    }

    private void generate(OreConfig oreConfig, World world, Chunk chunk, Biome biome) {
        CustomOreGeneratorService service = CustomOreGenerator.getService();

        Optional<OreGenerator> optional = service.getOreGenerator(oreConfig.getOreGenerator());

        if (!optional.isPresent())
            return;

        OreGenerator oreGenerator = optional.get();

        oreGenerator.generate(oreConfig, world, chunk.getX(), chunk.getZ(), getRandom(world.getSeed() + oreConfig.getMaterial().toString().hashCode(), chunk), biome);
    }

    private static Random getRandom(long seed, Chunk chunk) {
        Random random = new Random(seed);

        long long1 = random.nextLong();
        long long2 = random.nextLong();
        long newseed = (long) chunk.getX() * long1 ^ (long) chunk.getZ() * long2 ^ seed;
        random.setSeed(newseed);

        return random;
    }

}
