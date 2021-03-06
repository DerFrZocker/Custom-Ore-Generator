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

package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataType;
import org.apache.commons.lang.Validate;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class AbstractVariantCustomData extends AbstractCustomData<AbstractVariantCustomData.VariantApplier> {

    public AbstractVariantCustomData(@NotNull final Function<String, Info> infoFunction) {
        super("VARIANT", CustomDataType.INTEGER, infoFunction);
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return getCustomDataApplier().canApply(oreConfig);
    }

    @Override
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        if (!(customData instanceof Integer))
            return false;

        return getCustomDataApplier().isValidCustomData((Integer) customData, oreConfig);
    }

    @NotNull
    @Override
    public Object normalize(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        return customData;
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        Validate.notNull(blockState, "BlockState can not be null");

        return getCustomDataApplier().hasCustomData(blockState);
    }

    @NotNull
    @Override
    public Integer getCustomData(@NotNull final BlockState blockState) {
        Validate.isTrue(hasCustomData(blockState), "The given BlockState '" + blockState.getType() + ", " + blockState.getLocation() + "' can not have the CustomData '" + getName() + "'");

        return getCustomDataApplier().getCustomData(blockState);
    }

    public interface VariantApplier extends CustomDataApplier {

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
        boolean isValidCustomData(@NotNull Integer customData, @NotNull OreConfig oreConfig);

        /**
         * @param blockState to check
         * @return true if the blockState has a Variant
         */
        boolean hasCustomData(@NotNull BlockState blockState);

        /**
         * @param blockState to get the data from
         * @return the Variant as number
         */
        int getCustomData(@NotNull BlockState blockState);

    }

}
