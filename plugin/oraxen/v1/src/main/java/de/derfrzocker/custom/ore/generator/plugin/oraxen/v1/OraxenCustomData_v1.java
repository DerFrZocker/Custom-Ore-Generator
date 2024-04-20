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

package de.derfrzocker.custom.ore.generator.plugin.oraxen.v1;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataType;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractCustomData;
import de.derfrzocker.spigot.utils.Version;
import io.th0rgal.oraxen.api.OraxenBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class OraxenCustomData_v1 extends AbstractCustomData<CustomDataApplier> {

    public OraxenCustomData_v1(@NotNull Function<String, Info> infoFunction) {
        super("ORAXEN", CustomDataType.STRING, infoFunction);
    }

    @Override
    public boolean canApply(@NotNull OreConfig oreConfig) {
        return oreConfig.getMaterial() == Material.NOTE_BLOCK || oreConfig.getMaterial() == Material.TRIPWIRE;
    }

    @Override
    public boolean isValidCustomData(@NotNull Object customData, @NotNull OreConfig oreConfig) {
        if (!(customData instanceof String))
            return false;

        return OraxenBlocks.isOraxenBlock((String) customData);
    }

    @NotNull
    @Override
    public Object normalize(@NotNull Object customData, @NotNull OreConfig oreConfig) {
        return customData;
    }

    @Override
    public boolean hasCustomData(@NotNull BlockState blockState) {
        BlockData blockData = blockState.getBlockData();

        // Check for note block mechanic
        if (blockState.getType() == Material.NOTE_BLOCK) {
            return OraxenBlocks.getNoteBlockMechanic(blockData) != null;
        }

        // Check for string mechanic
        if (blockState.getType() == Material.TRIPWIRE) {
            return OraxenBlocks.getStringMechanic(blockData) != null;
        }

        return false;
    }

    @NotNull
    @Override
    public Object getCustomData(@NotNull BlockState blockState) {
        BlockData blockData = blockState.getBlockData();

        // Check for note block mechanic
        if (blockState.getType() == Material.NOTE_BLOCK) {
            return OraxenBlocks.getNoteBlockMechanic(blockData).getItemID();
        }

        // Check for string mechanic
        if (blockState.getType() == Material.TRIPWIRE) {
            return OraxenBlocks.getStringMechanic(blockData).getItemID();
        }

        return false;
    }

    @NotNull
    @Override
    protected CustomDataApplier getCustomDataApplier0() {
        if (Version.getServerVersion(Bukkit.getServer()).isNewerThan(Version.v1_16_R3)) {
            return new OraxenApplier(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
