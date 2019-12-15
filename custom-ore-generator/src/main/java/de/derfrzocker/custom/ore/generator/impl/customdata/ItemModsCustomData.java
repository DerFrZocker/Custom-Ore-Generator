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

package de.derfrzocker.custom.ore.generator.impl.customdata;

import com.gitlab.codedoctorde.itemmods.api.CustomBlockManager;
import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.itemmods.main.Main;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.ItemModsApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.ItemModsApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.ItemModsApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.ItemModsApplier_v1_15_R1;
import de.derfrzocker.spigot.utils.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemModsCustomData implements CustomData {

    public static final ItemModsCustomData INSTANCE = new ItemModsCustomData();

    @Nullable
    private CustomDataApplier customDataApplier;

    private ItemModsCustomData() {
    }

    @NotNull
    @Override
    public String getName() {
        return "ITEM_MODS";
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.STRING;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        final CustomBlockManager customBlockManager = Main.getPlugin().getCustomBlockManager();
        final List<BlockConfig> blockConfigs = customBlockManager.getBlockConfigs();

        return blockConfigs.stream().anyMatch(blockConfig -> blockConfig.getBlock().getMaterial() == oreConfig.getMaterial());
    }

    @Override
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        if (!(customData instanceof String))
            return false;

        final CustomBlockManager customBlockManager = Main.getPlugin().getCustomBlockManager();
        final List<BlockConfig> blockConfigs = customBlockManager.getBlockConfigs();

        return blockConfigs.stream().filter(blockConfig -> blockConfig.getBlock().getMaterial() == oreConfig.getMaterial()).anyMatch(blockConfig -> blockConfig.getName().equals(customData));
    }

    @NotNull
    @Override
    public CustomDataApplier getCustomDataApplier() {
        if (customDataApplier == null)
            customDataApplier = getCustomDataApplier0();

        return customDataApplier;
    }

    private CustomDataApplier getCustomDataApplier0() {
        switch (Version.getCurrent()) {
            case v1_15_R1:
                return new ItemModsApplier_v1_15_R1(this);
            case v1_14_R1:
                return new ItemModsApplier_v1_14_R1(this);
            case v1_13_R2:
                return new ItemModsApplier_v1_13_R2(this);
            case v1_13_R1:
                return new ItemModsApplier_v1_13_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
