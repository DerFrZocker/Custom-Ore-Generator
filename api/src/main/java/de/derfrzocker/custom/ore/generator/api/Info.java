package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface Info {

    /**
     * @return a fancy name of the the holder of this Info
     */
    @NotNull
    String getDisplayName();

    /**
     * @return the Material which represent the holder of this Info
     */
    @NotNull
    Material getMaterial();

    /**
     * @return the Description of the holder of this Info
     */
    @NotNull
    String getDescription();

}
