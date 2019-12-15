/*
 * MIT License
 *
 * Copyright (c) 2019 DerFrZocker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
