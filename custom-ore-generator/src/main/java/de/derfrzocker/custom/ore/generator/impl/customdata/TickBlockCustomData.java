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
import de.derfrzocker.custom.ore.generator.impl.v1_20_R3.customdata.TickBlockApplier_v1_20_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R4.customdata.TickBlockApplier_v1_20_R4;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R1.customdata.TickBlockApplier_v1_21_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R2.customdata.TickBlockApplier_v1_21_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R3.customdata.TickBlockApplier_v1_21_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R4.customdata.TickBlockApplier_v1_21_R4;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R5.customdata.TickBlockApplier_v1_21_R5;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R6.customdata.TickBlockApplier_v1_21_R6;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R7.customdata.TickBlockApplier_v1_21_R7;
import de.derfrzocker.custom.ore.generator.impl.v26_1_base.customdata.TickBlockApplier_v26_1_base;
import de.derfrzocker.spigot.utils.version.InternalVersion;
import de.derfrzocker.spigot.utils.version.ServerVersion;
import de.derfrzocker.spigot.utils.version.ServerVersionRange;
import java.util.Set;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

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
        ServerVersion version = ServerVersion.getCurrentVersion(Bukkit.getServer());
        if (version.isNewerThanOrSameAs(ServerVersionRange.V26_1.minInclusive())) {
            return new TickBlockApplier_v26_1_base(this);
        } else if (InternalVersion.v1_21_R7.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_21_R7(this);
        } else if (InternalVersion.v1_21_R6.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_21_R6(this);
        } else if (InternalVersion.v1_21_R5.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_21_R5(this);
        } else if (InternalVersion.v1_21_R4.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_21_R4(this);
        } else if (InternalVersion.v1_21_R3.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_21_R3(this);
        } else if (InternalVersion.v1_21_R2.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_21_R2(this);
        } else if (InternalVersion.v1_21_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_21_R1(this);
        } else if (InternalVersion.v1_20_R4.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_20_R4(this);
        } else if (InternalVersion.v1_20_R3.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_20_R3(this);
        } else if (InternalVersion.v1_20_R2.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_20_R2(this);
        } else if (InternalVersion.v1_20_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_20_R1(this);
        } else if (InternalVersion.v1_19_R3.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_19_R3(this);
        } else if (InternalVersion.v1_19_R2.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_19_R2(this);
        } else if (InternalVersion.v1_19_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_19_R1(this);
        } else if (InternalVersion.v1_18_R2.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_18_R2(this);
        } else if (InternalVersion.v1_18_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_18_R1(this);
        } else if (InternalVersion.v1_17_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_17_R1(this);
        } else if (InternalVersion.v1_16_R3.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_16_R3(this);
        } else if (InternalVersion.v1_16_R2.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_16_R2(this);
        } else if (InternalVersion.v1_16_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_16_R1(this);
        } else if (InternalVersion.v1_15_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_15_R1(this);
        } else if (InternalVersion.v1_14_R1.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_14_R1(this);
        } else if (InternalVersion.v1_13_R2.getServerVersionRange().isInRange(version)) {
            return new TickBlockApplier_v1_13_R2(this);
        } else if (InternalVersion.v1_13_R1.getServerVersionRange().isInRange(version)) {
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
