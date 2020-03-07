/*
 * MIT License
 *
 * Copyright (c) 2019 Marvin (DerFrZocker)
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
