package de.derfrzocker.custom.ore.generator.api;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public interface OreSettingContainer {

    /**
     * This sets the given OreSetting with the given value, to this
     * OreSettingContainer. If this OreSettingContainer already contains the given OreSetting,
     * than the old value get's replaced with the new one.
     *
     * @param oreSetting which must be non-null
     * @param value      for the given setting
     * @throws IllegalArgumentException if oreSetting is null
     */
    void setValue(@NotNull OreSetting oreSetting, double value);

    /**
     * If this OreSettingContainer contains the value of the given OreSetting,
     * it returns an Optional that contains the value,
     * otherwise it return an empty Optional
     *
     * @param oreSetting which must be non-null
     * @return an Optional that hold the value of the given OreSetting,
     * or an empty Optional if the OreSettingContainer not contains the given OreSetting.
     * @throws IllegalArgumentException if oreSetting is null
     */
    @NotNull
    Optional<Double> getValue(@NotNull OreSetting oreSetting);

    /**
     * Removes the value of the given OreSetting from this OreSettingContainer.
     * If this OreSettingContainer don't contains a value for the given OreSetting, nothing happens.
     *
     * @param oreSetting which get's removed
     * @return true if it was removed, false if this OreSettingContainer don't have the OreSetting.
     * @throws IllegalArgumentException if oreSetting is null
     */
    boolean removeValue(@NotNull OreSetting oreSetting);

    /**
     * Returns a copy of all OreSettings and it's values, which this OreSettingContainer has.
     * <p>
     * The map that get's returned can be immutable.
     *
     * @return a new map with all OreSetting and values
     */
    @NotNull
    Map<OreSetting, Double> getValues();

}
