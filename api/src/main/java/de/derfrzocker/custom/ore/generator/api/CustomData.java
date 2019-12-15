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
