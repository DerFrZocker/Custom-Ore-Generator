package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

/**
 * A BlockSelector select location's on which a vein should get generated
 */
public interface BlockSelector {

    /**
     * Returns a Set of Locations on which veins should get generated
     * The x and z positions should be between 0 and 15
     * The y positions should be between 0 and 255
     *
     * @param chunkInfo the ChunkInfo of the chunk
     * @param config    which get generated
     * @param random    to use
     * @return a Set of Locations on which veins should get generated
     * @throws NullPointerException if chunkInfo, config, or random is null
     */
    @NotNull
    Set<Location> selectBlocks(@NotNull ChunkInfo chunkInfo, @NotNull OreConfig config, @NotNull Random random);

    /**
     * @return a set with all OreSettings which this BlockSelector needs
     */
    @NotNull
    Set<OreSetting> getNeededOreSettings();

    /**
     * The name is used to identify the BlockSelector,
     * each BlockSelector should have a unique name.
     * The name must match the following Regex: ^[A-Z_]*$
     * The name can not be empty
     *
     * @return the name of this BlockSelector
     */
    @NotNull
    String getName();

    /**
     * Checks if the given value for the given oreSetting is save or not.
     * Save means, that passing an ore config with the given oreSetting and value to the selectBlocks method,
     * will not cause definitely an error.
     *
     * @param oreSetting to check
     * @param value      to check
     * @param oreConfig  which gets the setting set
     * @return true if the given value is save, otherwise return false
     * @throws IllegalArgumentException if oreSetting or oreConfig is null
     * @throws IllegalArgumentException if the BlockSelector does not need the given oreSetting
     */
    boolean isSaveValue(@NotNull OreSetting oreSetting, double value, @NotNull OreConfig oreConfig);

}
