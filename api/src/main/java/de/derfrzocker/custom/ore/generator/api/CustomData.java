package de.derfrzocker.custom.ore.generator.api;

import org.jetbrains.annotations.NotNull;

/**
 * A CustomData get's applied to every Block, which get's generated from an OreConfig,
 * which have the CustomData
 */
public interface CustomData {

    /**
     * The name is used to identify the CustomData,
     * each CustomData should have a unique name.
     * The name must match the following Regex: ^[A-Z_]*$
     * The name can not be empty
     *
     * @return the name of this CustomData
     */
    @NotNull
    String getName();

    /**
     * @return the data type which this CustomData needs
     */
    @NotNull
    CustomDataType getCustomDataType();

    /**
     * Checks, if the given OreConfig can use this CustomData
     *
     * @param oreConfig that get's checked
     * @return true if this OreConfig can apply the CustomData
     * @throws IllegalArgumentException if oreConfig is null
     */
    boolean canApply(@NotNull OreConfig oreConfig);

    /**
     * Checks, if the given customData value is valid or not
     *
     * @param customData to check
     * @param oreConfig  which get's the customData
     * @return true if valid other wise false
     * @throws IllegalArgumentException if customData or OreConfig is null
     */
    boolean isValidCustomData(@NotNull Object customData, @NotNull OreConfig oreConfig);

    /**
     * @return the CustomDataApplier of this CustomData
     */
    @NotNull
    CustomDataApplier getCustomDataApplier();

}
