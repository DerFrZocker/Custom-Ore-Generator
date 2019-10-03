package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Optional;
import java.util.Set;

/**
 * Biome specific values are handel in the OreConfig itself
 * Only for Backwards compatibility.
 */
@Deprecated
public interface BiomeConfig {

    Biome getBiome();

    Optional<OreConfig> getOreConfig(Material material);

    Set<OreConfig> getOreConfigs();

    void addOreConfig(OreConfig oreConfig);

}
