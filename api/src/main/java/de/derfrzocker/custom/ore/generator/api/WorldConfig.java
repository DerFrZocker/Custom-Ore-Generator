package de.derfrzocker.custom.ore.generator.api;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

/**
 * Holds all OreConfig for a World
 */
public interface WorldConfig {

    /**
     * @return the name of the WorldConfig
     */
    @NotNull
    String getWorld();

    /**
     * If this WorldConfig have the OreConfig with the given name
     * it returns an Optional that contains the OreConfig,
     * Otherwise it return an empty Optional
     *
     * @param name of the OreConfig
     * @return an Optional that hold the value of the given name,
     * or an empty Optional if the WorldConfig not have the OreConfig with the given name
     * @throws IllegalArgumentException if name is null
     */
    @NotNull
    Optional<OreConfig> getOreConfig(@NotNull String name);

    /**
     * @return a new Set with all OreConfigs this WorldConfig have
     */
    @NotNull
    Set<OreConfig> getOreConfigs();

    /**
     * Adds the given OreConfig to this WorldConfig
     * If a OreConfig with the same name already exists in this WorldConfig,
     * it will throw an Exception
     *
     * @param oreConfig to add
     * @throws IllegalArgumentException if oreConfig is null
     * @throws IllegalArgumentException if a OreConfig with the same name already exists
     */
    void addOreConfig(@NotNull OreConfig oreConfig);

}
