package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
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
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AutoCustomData implements CustomData {

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

    @NotNull
    @Override
    public String getName() {
        return "AUTO";
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.BOOLEAN;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return MATERIALS.contains(oreConfig.getMaterial());
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
