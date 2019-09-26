package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.FacingApplier_v1_14_R1;
import de.derfrzocker.spigot.utils.Version;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.data.Directional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FacingCustomData implements CustomData {

    public static final FacingCustomData INSTANCE = new FacingCustomData();

    private CustomDataApplier customDataApplier;

    @Override
    public String getName() {
        return "FACING";
    }

    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.STRING;
    }

    @Override
    public boolean canApply(OreConfig oreConfig) {
        return Bukkit.createBlockData(oreConfig.getMaterial()) instanceof Directional;
    }

    @Override
    public boolean isValidCustomData(Object customData, OreConfig oreConfig) {
        if (!(customData instanceof String))
            return false;

        switch (((String) customData).toUpperCase()) {
            case "DOWN":
            case "UP":
            case "NORTH":
            case "SOUTH":
            case "WEST":
            case "EAST":
                return true;
        }

        return false;
    }

    @Override
    public CustomDataApplier getCustomDataApplier() {
        if (customDataApplier == null)
            customDataApplier = getCustomDataApplier0();

        return customDataApplier;
    }

    private CustomDataApplier getCustomDataApplier0() {
        switch (Version.getCurrent()) {
            case v1_14_R1:
                return new FacingApplier_v1_14_R1(this);
            case v1_13_R2:
            case v1_13_R1:
            case v1_12_R1:
            case v1_11_R1:
            case v1_10_R1:
            case v1_9_R2:
            case v1_9_R1:
            case v1_8_R3:
            case v1_8_R2:
            case v1_8_R1:
                throw new UnsupportedOperationException("Version not supported jet!");
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
