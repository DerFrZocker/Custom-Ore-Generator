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
import de.derfrzocker.custom.ore.generator.api.customdata.LimitedValuesCustomData;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.TickBlockApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.TickBlockApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.TickBlockApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.TickBlockApplier_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R1.customdata.TickBlockApplier_v1_16_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R2.customdata.TickBlockApplier_v1_16_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R3.customdata.TickBlockApplier_v1_16_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_17_R1.customdata.TickBlockApplier_v1_17_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R1.customdata.TickBlockApplier_v1_18_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R2.customdata.TickBlockApplier_v1_18_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R1.customdata.TickBlockApplier_v1_19_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R2.customdata.TickBlockApplier_v1_19_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R3.customdata.TickBlockApplier_v1_19_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R1.customdata.TickBlockApplier_v1_20_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R2.customdata.TickBlockApplier_v1_20_R2;
import de.derfrzocker.spigot.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;

public class TickBlockCustomData extends AbstractCustomData<CustomDataApplier> implements LimitedValuesCustomData {

    public TickBlockCustomData(@NotNull final Function<String, Info> infoFunction) {
        super("TICK_BLOCK", CustomDataType.BOOLEAN, infoFunction);
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return true;
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

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        return true;
    }

    @NotNull
    @Override
    public Boolean getCustomData(@NotNull final BlockState blockState) {
        return false;
    }

    @NotNull
    @Override
    protected CustomDataApplier getCustomDataApplier0() {
        switch (Version.getServerVersion(Bukkit.getServer())) {
            case v1_20_R2:
                return new TickBlockApplier_v1_20_R2(this);
            case v1_20_R1:
                return new TickBlockApplier_v1_20_R1(this);
            case v1_19_R3:
                return new TickBlockApplier_v1_19_R3(this);
            case v1_19_R2:
                return new TickBlockApplier_v1_19_R2(this);
            case v1_19_R1:
                return new TickBlockApplier_v1_19_R1(this);
            case v1_18_R2:
                return new TickBlockApplier_v1_18_R2(this);
            case v1_18_R1:
                return new TickBlockApplier_v1_18_R1(this);
            case v1_17_R1:
                return new TickBlockApplier_v1_17_R1(this);
            case v1_16_R3:
                return new TickBlockApplier_v1_16_R3(this);
            case v1_16_R2:
                return new TickBlockApplier_v1_16_R2(this);
            case v1_16_R1:
                return new TickBlockApplier_v1_16_R1(this);
            case v1_15_R1:
                return new TickBlockApplier_v1_15_R1(this);
            case v1_14_R1:
                return new TickBlockApplier_v1_14_R1(this);
            case v1_13_R2:
                return new TickBlockApplier_v1_13_R2(this);
            case v1_13_R1:
                return new TickBlockApplier_v1_13_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

    @NotNull
    @Override
    public Set<Object> getPossibleValues(@NotNull final Material material) {
        return BOOLEAN_VALUE;
    }

}
