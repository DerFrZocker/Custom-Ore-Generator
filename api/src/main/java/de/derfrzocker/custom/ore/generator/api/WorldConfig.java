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
    String getName();

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
     * The returning Set is in order in which the OreConfig should get generated
     *
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
     * @throws IllegalArgumentException if a OreConfig with the same name already exists in this WorldConfig
     */
    void addOreConfig(@NotNull OreConfig oreConfig);

    /**
     * Adds the given OreConfig to the given position.
     * Where 0 is the first position and the amount of OreConfig the last.
     * If the given position is less than 1 it will at the OreConfig to the firsts place.
     * If the given position is the amount oreConfigs this WorldConfig has or higher, it will set it to the last place
     * If the given OreConfig is already present in this WorldConfig it will reposition the OreConfig. In this case
     * the last position is amount of OreConfig - 1.
     *
     * @param oreConfig to add
     * @param position  to set
     * @throws IllegalArgumentException if oreConfig is null
     */
    void addOreConfig(@NotNull OreConfig oreConfig, int position);

    /**
     * Returns a set with all OreConfigs, this WorldConfig has.
     * The returning Set contains Lazy and none Lazy OreConfigs
     * The returning Set is in order in which the OreConfig should get generated
     *
     * @return a new set with all all OreConfigs
     */
    Set<String> getAllOreConfigs();

}
