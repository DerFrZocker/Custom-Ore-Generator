package de.derfrzocker.custom.generator.ore.api;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Optional;
import java.util.Set;

public interface BiomeConfig {

    Biome getBiome();

    Optional<OreConfig> getOreConfig(Material material);

    Set<OreConfig> getOreConfigs();

    void addOreConfig(OreConfig oreConfig);

}
