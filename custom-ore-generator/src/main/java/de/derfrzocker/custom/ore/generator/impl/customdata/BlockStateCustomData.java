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
import de.derfrzocker.custom.ore.generator.impl.v1_21_R5.customdata.BlockStateApplier_v1_21_R5;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R6.customdata.BlockStateApplier_v1_21_R6;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R7.customdata.BlockStateApplier_v1_21_R7;
import de.derfrzocker.custom.ore.generator.impl.v26_1_base.customdata.BlockStateApplier_v26_1_base;
import de.derfrzocker.spigot.utils.version.InternalVersion;
import de.derfrzocker.spigot.utils.version.ServerVersion;
import de.derfrzocker.spigot.utils.version.ServerVersionRange;
import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BlockStateCustomData extends AbstractBlockStateCustomData {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public BlockStateCustomData(
            @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier,
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
        if (version.isNewerThanOrSameAs(ServerVersionRange.V26_1.minInclusive())) {
            return new BlockStateApplier_v26_1_base(this.serviceSupplier, this);
        } else if (InternalVersion.v1_21_R7.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_21_R7(this.serviceSupplier, this);
        } else if (InternalVersion.v1_21_R6.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_21_R6(this.serviceSupplier, this);
        } else if (InternalVersion.v1_21_R5.getServerVersionRange().isInRange(version)) {
            return new BlockStateApplier_v1_21_R5(this.serviceSupplier, this);
        } else if (InternalVersion.v1_21_R4.getServerVersionRange().isInRange(version)) {
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
