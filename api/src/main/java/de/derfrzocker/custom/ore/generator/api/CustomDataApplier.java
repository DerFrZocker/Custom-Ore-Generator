package de.derfrzocker.custom.ore.generator.api;

import org.jetbrains.annotations.NotNull;

/**
 * A CustomDataApplier applies the CustomData to the block,
 * on the given location.
 * This Interface handel the nms part of the CustomData
 */
public interface CustomDataApplier {

    /**
     * Applies a CustomData to the Block on the given Location.
     * This method may get called from differed Threads in the same time.
     *
     * @param oreConfig   from which the block is
     * @param location    nms BlockPosition
     * @param blockAccess nms world or BlockAccess
     * @throws IllegalArgumentException if any value is null
     */
    void apply(@NotNull OreConfig oreConfig, @NotNull Object location, @NotNull Object blockAccess);

}
