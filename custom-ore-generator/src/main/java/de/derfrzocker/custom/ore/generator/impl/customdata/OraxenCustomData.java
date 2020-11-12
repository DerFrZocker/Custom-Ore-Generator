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
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.OraxenApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.OraxenApplier_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R1.customdata.OraxenApplier_v1_16_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R2.customdata.OraxenApplier_v1_16_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R3.customdata.OraxenApplier_v1_16_R3;
import de.derfrzocker.spigot.utils.Version;
import io.th0rgal.oraxen.items.OraxenItems;
import io.th0rgal.oraxen.mechanics.provided.block.BlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.block.BlockMechanicFactory;
import io.th0rgal.oraxen.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class OraxenCustomData extends AbstractCustomData<CustomDataApplier> {

    public OraxenCustomData(@NotNull Function<String, Info> infoFunction) {
        super("ORAXEN", CustomDataType.STRING, infoFunction);
    }

    @Override
    public boolean canApply(@NotNull OreConfig oreConfig) {
        return oreConfig.getMaterial() == Material.MUSHROOM_STEM;
    }

    @Override
    public boolean isValidCustomData(@NotNull Object customData, @NotNull OreConfig oreConfig) {
        if (!(customData instanceof String))
            return false;

        String name = (String) customData;

        return OraxenItems.exists(name);
    }

    @NotNull
    @Override
    public Object normalize(@NotNull Object customData, @NotNull OreConfig oreConfig) {
        return customData;
    }

    @Override
    public boolean hasCustomData(@NotNull BlockState blockState) {
        BlockData blockData = blockState.getBlockData();

        if (!(blockData instanceof MultipleFacing)) {
            return false;
        }

        int code = Utils.getCode((MultipleFacing) blockData);

        return BlockMechanicFactory.getBlockMechanic(code) != null;
    }

    @NotNull
    @Override
    public Object getCustomData(@NotNull BlockState blockState) {
        BlockData blockData = blockState.getBlockData();

        if (!(blockData instanceof MultipleFacing)) {
            return false;
        }

        int code = Utils.getCode((MultipleFacing) blockData);
        BlockMechanic blockMechanic = BlockMechanicFactory.getBlockMechanic(code);

        return blockMechanic.getItemID();
    }

    @NotNull
    @Override
    protected CustomDataApplier getCustomDataApplier0() {
        switch (Version.getServerVersion(Bukkit.getServer())) {
            case v1_16_R3:
                return new OraxenApplier_v1_16_R3(this);
            case v1_16_R2:
                return new OraxenApplier_v1_16_R2(this);
            case v1_16_R1:
                return new OraxenApplier_v1_16_R1(this);
            case v1_15_R1:
                return new OraxenApplier_v1_15_R1(this);
            case v1_14_R1:
                return new OraxenApplier_v1_14_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
