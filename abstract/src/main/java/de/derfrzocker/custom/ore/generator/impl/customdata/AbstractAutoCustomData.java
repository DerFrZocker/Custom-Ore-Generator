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

package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.customdata.LimitedValuesCustomData;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;

public abstract class AbstractAutoCustomData extends AbstractCustomData<AbstractAutoCustomData.AutoApplier> implements LimitedValuesCustomData {

    public AbstractAutoCustomData(@NotNull final Function<String, Info> infoFunction) {
        super("AUTO", CustomDataType.BOOLEAN, infoFunction);
    }

    @Override
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        return customData instanceof Boolean;
    }

    @NotNull
    @Override
    public Object normalize(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        return customData;
    }

    @NotNull
    @Override
    public Boolean getCustomData(@NotNull BlockState blockState) {
        Validate.isTrue(hasCustomData(blockState), "The given BlockState '" + blockState.getType() + ", " + blockState.getLocation() + "' can not have the CustomData '" + getName() + "'");

        return getCustomDataApplier().getCustomData((CommandBlock) blockState);
    }

    @NotNull
    @Override
    public Set<Object> getPossibleValues(@NotNull final Material material) {
        return BOOLEAN_VALUE;
    }

    public interface AutoApplier extends CustomDataApplier {

        /**
         * @param commandBlock to get the data from
         * @return true if auto is activated
         */
        boolean getCustomData(@NotNull CommandBlock commandBlock);

    }

}
