package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

/**
 * A OreGenerator generate veins of ores on the given locations
 */
public interface OreGenerator extends InfoAble{

    /**
     * Generates veins of ores on the given Locations
     * The given Locations are relative from the chunk position,
     * this means the x and z values of the locations have a range from 0 - 15
     *
     * @param config      which get generated
     * @param chunkAccess to use
     * @param x           position of the chunk
     * @param z           position of the chunk
     * @param random      to use
     * @param biome       which get generated
     * @param locations   where the veins should be generated
     * @throws IllegalArgumentException if config, world, random, biome or locations are null
     */
    void generate(@NotNull OreConfig config, @NotNull ChunkAccess chunkAccess, int x, int z, @NotNull Random random, @NotNull Biome biome, @NotNull Set<Location> locations);

    /**
     * @return a set with all OreSettings which this BlockSelector needs
     */
    @NotNull
    Set<OreSetting> getNeededOreSettings();

    /**
     * The name is used to identify the OreGenerator,
     * each OreGenerator should have a unique name.
     * The name must match the following Regex: ^[A-Z_]*$
     * The name can not be empty
     *
     * @return the name of this OreGenerator
     */
    @NotNull
    String getName();

    /**
     * Checks if the given value for the given oreSetting is save or not.
     * Save means, that passing an ore config with the given oreSetting and value to the generate method,
     * will not cause definitely an error.
     *
     * @param oreSetting to check
     * @param value      to check
     * @param oreConfig  which gets the setting set
     * @return true if the given value is save, otherwise return false
     * @throws IllegalArgumentException if oreSetting or oreConfig is null
     * @throws IllegalArgumentException if the OreGenerator does not need the given oreSetting
     */
    boolean isSaveValue(@NotNull OreSetting oreSetting, double value, @NotNull OreConfig oreConfig);

}
