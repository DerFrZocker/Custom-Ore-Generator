/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
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

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

/**
 * A BlockSelector select location's on which a vein should get generated
 */
public interface BlockSelector extends InfoAble, OreSettingsAble {

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
     * The name is used to identify the BlockSelector,
     * each BlockSelector should have a unique name.
     * The name must match the following Regex: ^[A-Z_]*$
     * The name can not be empty
     *
     * @return the name of this BlockSelector
     */
    @NotNull
    String getName();

}
