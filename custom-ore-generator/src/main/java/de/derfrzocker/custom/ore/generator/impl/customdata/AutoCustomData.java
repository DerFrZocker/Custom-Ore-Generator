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

import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata.AutoApplier_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata.AutoApplier_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.customdata.AutoApplier_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.AutoApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.AutoApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.AutoApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.AutoApplier_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.customdata.AutoApplier_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.customdata.AutoApplier_v1_9_R2;
import de.derfrzocker.spigot.utils.Version;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AutoCustomData extends AbstractAutoCustomData {

    public static final AutoCustomData INSTANCE = new AutoCustomData();
    private static final Set<Material> MATERIALS = new HashSet<>();

    static {
        switch (Version.getCurrent()) {
            case v1_15_R1:
            case v1_14_R1:
            case v1_13_R2:
            case v1_13_R1:
                MATERIALS.add(Material.valueOf("COMMAND_BLOCK"));
                MATERIALS.add(Material.valueOf("CHAIN_COMMAND_BLOCK"));
                MATERIALS.add(Material.valueOf("REPEATING_COMMAND_BLOCK"));
                break;
            case v1_12_R1:
            case v1_11_R1:
            case v1_10_R1:
            case v1_9_R2:
            case v1_9_R1:
                MATERIALS.add(Material.valueOf("COMMAND"));
                MATERIALS.add(Material.valueOf("COMMAND_REPEATING"));
                MATERIALS.add(Material.valueOf("COMMAND_CHAIN"));
                break;
        }
    }

    @Nullable
    private CustomDataApplier customDataApplier;

    private AutoCustomData() {
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return MATERIALS.contains(oreConfig.getMaterial());
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        Validate.notNull(blockState, "BlockState can not be null");

        return MATERIALS.contains(blockState.getType());
    }

    protected AutoApplier getCustomDataApplier0() {
        switch (Version.getCurrent()) {
            case v1_15_R1:
                return new AutoApplier_v1_15_R1(this);
            case v1_14_R1:
                return new AutoApplier_v1_14_R1(this);
            case v1_13_R2:
                return new AutoApplier_v1_13_R2(this);
            case v1_13_R1:
                return new AutoApplier_v1_13_R1(this);
            case v1_12_R1:
                return new AutoApplier_v1_12_R1(this);
            case v1_11_R1:
                return new AutoApplier_v1_11_R1(this);
            case v1_10_R1:
                return new AutoApplier_v1_10_R1(this);
            case v1_9_R2:
                return new AutoApplier_v1_9_R2(this);
            case v1_9_R1:
                return new AutoApplier_v1_9_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
