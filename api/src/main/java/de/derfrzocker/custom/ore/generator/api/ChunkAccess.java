package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface ChunkAccess {


    /**
     * Set the given material to the given position
     * The position is absolute from the world point and not relative from the chunk
     *
     * @param material to set
     * @param x        position
     * @param y        position
     * @param z        position
     * @throws IllegalArgumentException if material is null
     */
    void setMaterial(@NotNull Material material, int x, int y, int z);

    /**
     * Return the material on the given position.
     * The position is absolute from the world point and not relative from the chunk
     *
     * @param x position
     * @param y position
     * @param z position
     * @return the material on the given position
     */
    @NotNull
    Material getMaterial(int x, int y, int z);

}
