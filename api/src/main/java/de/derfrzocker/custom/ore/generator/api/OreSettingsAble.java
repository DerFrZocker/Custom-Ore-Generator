package de.derfrzocker.custom.ore.generator.api;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface OreSettingsAble {

    Set<OreSetting> EMPTY = Collections.unmodifiableSet(new HashSet<>());

    /**
     * @return a set with all OreSettings which this OreSettingAble needs
     */
    @NotNull
    Set<OreSetting> getNeededOreSettings();

    /**
     * Returns the Info Object for the given OreSetting
     *
     * @param oreSetting to get the info from
     * @return the info of the given OreSetting
     * @throws IllegalArgumentException if oreSetting is null
     * @throws IllegalArgumentException if this OreSettingAble don't have the given oreSetting
     */
    @NotNull
    Info getOreSettingInfo(@NotNull OreSetting oreSetting);

    /**
     * Checks if the given value for the given oreSetting is save or not.
     * Save means, that passing an ore config with the given oreSetting and value will not cause definitely an error.
     *
     * @param oreSetting to check
     * @param value      to check
     * @param oreConfig  which gets the setting set
     * @return true if the given value is save, otherwise return false
     * @throws IllegalArgumentException if oreSetting or oreConfig is null
     * @throws IllegalArgumentException if the OreSettingAble does not need the given oreSetting
     */
    boolean isSaveValue(@NotNull OreSetting oreSetting, double value, @NotNull OreConfig oreConfig);

}
