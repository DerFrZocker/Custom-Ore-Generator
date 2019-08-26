package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Optional;
import java.util.Set;

public interface WorldConfig {

    String getWorld();

    Optional<OreConfig> getOreConfig(Material material);

    Set<OreConfig> getOreConfigs();

    void addOreConfig(OreConfig oreConfig);

    Optional<BiomeConfig> getBiomeConfig(Biome biome);

    Set<BiomeConfig> getBiomeConfigs();

    void addBiomeConfig(BiomeConfig biomeConfig);

}
