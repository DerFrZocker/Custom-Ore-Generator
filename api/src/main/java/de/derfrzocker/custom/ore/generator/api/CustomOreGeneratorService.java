package de.derfrzocker.custom.ore.generator.api;


import org.bukkit.Material;
import org.bukkit.World;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

public interface CustomOreGeneratorService {

    Optional<OreGenerator> getOreGenerator(String name);

    void registerOreGenerator(OreGenerator oreGenerator);

    Set<OreGenerator> getOreGenerators();

    Optional<WorldConfig> getWorldConfig(String world);

    WorldConfig createWorldConfig(World world);

    OreConfig createOreConfig(String name, Material material, OreGenerator oreGenerator);

    void saveWorldConfig(WorldConfig config);

    Set<WorldConfig> getWorldConfigs();

    void setDefaultOreGenerator(OreGenerator oreGenerator);

    OreGenerator getDefaultOreGenerator();

    Random createRandom(long seed, int x, int z);

}
