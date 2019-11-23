package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.TickBlockApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.TickBlockApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.TickBlockApplier_v1_14_R1;
import de.derfrzocker.spigot.utils.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TickBlockCustomData implements CustomData {

    public static final TickBlockCustomData INSTANCE = new TickBlockCustomData();

    @Nullable
    private CustomDataApplier customDataApplier;

    private TickBlockCustomData() {
    }

    @NotNull
    @Override
    public String getName() {
        return "TICK_BLOCK";
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.BOOLEAN;
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
    public CustomDataApplier getCustomDataApplier() {
        if (customDataApplier == null)
            customDataApplier = getCustomDataApplier0();

        return customDataApplier;
    }

    private CustomDataApplier getCustomDataApplier0() {
        switch (Version.getCurrent()) {
            case v1_14_R1:
                return new TickBlockApplier_v1_14_R1(this);
            case v1_13_R2:
                return new TickBlockApplier_v1_13_R2(this);
            case v1_13_R1:
                return new TickBlockApplier_v1_13_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
