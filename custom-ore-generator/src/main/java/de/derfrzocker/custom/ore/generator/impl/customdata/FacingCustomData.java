package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata.FacingApplier_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata.FacingApplier_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.customdata.FacingApplier_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.FacingApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.FacingApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.FacingApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.FacingApplier_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.customdata.FacingApplier_v1_18_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.customdata.FacingApplier_v1_18_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata.FacingApplier_v1_18_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.customdata.FacingApplier_v1_19_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.customdata.FacingApplier_v1_19_R2;
import de.derfrzocker.spigot.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FacingCustomData implements CustomData {

    public static final FacingCustomData INSTANCE = new FacingCustomData();

    @Nullable
    private CustomDataApplier customDataApplier;

    private FacingCustomData() {
    }

    @NotNull
    @Override
    public String getName() {
        return "FACING";
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.STRING;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return Bukkit.createBlockData(oreConfig.getMaterial()) instanceof Directional;
    }

    @Override
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        if (!(customData instanceof String))
            return false;

        try {
            final BlockFace blockFace = BlockFace.valueOf(((String) customData).toUpperCase());

            return ((Directional) Bukkit.createBlockData(oreConfig.getMaterial())).getFaces().contains(blockFace);
        } catch (IllegalArgumentException e) {
            return false;
        }
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
                return new FacingApplier_v1_15_R1(this);
            case v1_14_R1:
                return new FacingApplier_v1_14_R1(this);
            case v1_13_R2:
                return new FacingApplier_v1_13_R2(this);
            case v1_13_R1:
                return new FacingApplier_v1_13_R1(this);
            case v1_12_R1:
                return new FacingApplier_v1_12_R1(this);
            case v1_11_R1:
                return new FacingApplier_v1_11_R1(this);
            case v1_10_R1:
                return new FacingApplier_v1_10_R1(this);
            case v1_9_R2:
                return new FacingApplier_v1_19_R2(this);
            case v1_9_R1:
                return new FacingApplier_v1_19_R1(this);
            case v1_8_R3:
                return new FacingApplier_v1_18_R3(this);
            case v1_8_R2:
                return new FacingApplier_v1_18_R2(this);
            case v1_8_R1:
                return new FacingApplier_v1_18_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
