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

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata.BlockStateApplier_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata.BlockStateApplier_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.customdata.BlockStateApplier_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.BlockStateApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.BlockStateApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.BlockStateApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.BlockStateApplier_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R1.customdata.BlockStateApplier_v1_16_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R2.customdata.BlockStateApplier_v1_16_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R3.customdata.BlockStateApplier_v1_16_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_17_R1.customdata.BlockStateApplier_v1_17_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R1.customdata.BlockStateApplier_v1_18_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R2.customdata.BlockStateApplier_v1_18_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R1.customdata.BlockStateApplier_v1_19_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R2.customdata.BlockStateApplier_v1_19_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R3.customdata.BlockStateApplier_v1_19_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R1.customdata.BlockStateApplier_v1_20_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R2.customdata.BlockStateApplier_v1_20_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R3.customdata.BlockStateApplier_v1_20_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R4.customdata.BlockStateApplier_v1_20_R4;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R1.customdata.BlockStateApplier_v1_21_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R2.customdata.BlockStateApplier_v1_21_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R3.customdata.BlockStateApplier_v1_21_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R4.customdata.BlockStateApplier_v1_21_R4;
import de.derfrzocker.spigot.utils.version.InternalVersion;
import de.derfrzocker.spigot.utils.version.ServerVersion;
import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BlockStateCustomData extends AbstractBlockStateCustomData {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public BlockStateCustomData(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier,
                                @NotNull Function<String, Info> infoFunction,
                                @NotNull final File fileFolder) {
        super(infoFunction, fileFolder);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");

        this.serviceSupplier = serviceSupplier;
    }

    @NotNull
    @Override
    protected AbstractBlockStateCustomData.BlockStateApplier getCustomDataApplier0() {
        ServerVersion version = ServerVersion.getCurrentVersion(Bukkit.getServer());
        if (InternalVersion.v1_21_R4.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_21_R4(this.serviceSupplier, this);
        } else if (InternalVersion.v1_21_R3.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_21_R3(this.serviceSupplier, this);
        } else if (InternalVersion.v1_21_R2.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_21_R2(this.serviceSupplier, this);
        } else if (InternalVersion.v1_21_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_21_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_20_R4.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_20_R4(this.serviceSupplier, this);
        } else if (InternalVersion.v1_20_R3.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_20_R3(this.serviceSupplier, this);
        } else if (InternalVersion.v1_20_R2.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_20_R2(this.serviceSupplier, this);
        } else if (InternalVersion.v1_20_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_20_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_19_R3.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_19_R3(this.serviceSupplier, this);
        } else if (InternalVersion.v1_19_R2.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_19_R2(this.serviceSupplier, this);
        } else if (InternalVersion.v1_19_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_19_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_18_R2.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_18_R2(this.serviceSupplier, this);
        } else if (InternalVersion.v1_18_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_18_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_17_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_17_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_16_R3.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_16_R3(this.serviceSupplier, this);
        } else if (InternalVersion.v1_16_R2.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_16_R2(this.serviceSupplier, this);
        } else if (InternalVersion.v1_16_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_16_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_15_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_15_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_14_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_14_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_13_R2.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_13_R2(this.serviceSupplier, this);
        } else if (InternalVersion.v1_13_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_13_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_12_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_12_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_11_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_11_R1(this.serviceSupplier, this);
        } else if (InternalVersion.v1_10_R1.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_10_R1(this.serviceSupplier, this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
