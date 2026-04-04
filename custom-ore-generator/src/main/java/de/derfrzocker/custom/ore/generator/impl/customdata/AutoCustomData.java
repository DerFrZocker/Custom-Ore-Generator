package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata.AutoApplier_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata.AutoApplier_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.customdata.AutoApplier_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.AutoApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.AutoApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.AutoApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.AutoApplier_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R1.customdata.AutoApplier_v1_16_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R2.customdata.AutoApplier_v1_16_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R3.customdata.AutoApplier_v1_16_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_17_R1.customdata.AutoApplier_v1_17_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R1.customdata.AutoApplier_v1_18_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R2.customdata.AutoApplier_v1_18_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R1.customdata.AutoApplier_v1_19_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R2.customdata.AutoApplier_v1_19_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R3.customdata.AutoApplier_v1_19_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R1.customdata.AutoApplier_v1_20_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R2.customdata.AutoApplier_v1_20_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R3.customdata.AutoApplier_v1_20_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R4.customdata.AutoApplier_v1_20_R4;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R1.customdata.AutoApplier_v1_21_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R2.customdata.AutoApplier_v1_21_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R3.customdata.AutoApplier_v1_21_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R4.customdata.AutoApplier_v1_21_R4;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R5.customdata.AutoApplier_v1_21_R5;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R6.customdata.AutoApplier_v1_21_R6;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R7.customdata.AutoApplier_v1_21_R7;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.customdata.AutoApplier_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v26_1_base.customdata.AutoApplier_v26_1_base;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.customdata.AutoApplier_v1_9_R2;
import de.derfrzocker.spigot.utils.version.InternalVersion;
import de.derfrzocker.spigot.utils.version.ServerVersion;
import de.derfrzocker.spigot.utils.version.ServerVersionRange;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

public class AutoCustomData extends AbstractAutoCustomData {

    private static final Set<Material> MATERIALS = new HashSet<>();

    static {
        ServerVersion version = ServerVersion.getCurrentVersion(Bukkit.getServer());
        if (version.isNewerThanOrSameAs(InternalVersion.v1_13_R1.getServerVersionRange().minInclusive())) {
            MATERIALS.add(Material.valueOf("COMMAND_BLOCK"));
            MATERIALS.add(Material.valueOf("CHAIN_COMMAND_BLOCK"));
            MATERIALS.add(Material.valueOf("REPEATING_COMMAND_BLOCK"));
        } else if (version.isNewerThanOrSameAs(InternalVersion.v1_9_R1.getServerVersionRange().minInclusive())) {
            MATERIALS.add(Material.valueOf("COMMAND"));
            MATERIALS.add(Material.valueOf("COMMAND_REPEATING"));
            MATERIALS.add(Material.valueOf("COMMAND_CHAIN"));
        }
    }

    public AutoCustomData(@NotNull final Function<String, Info> infoFunction) {
        super(infoFunction);
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

    @NotNull
    @Override
    protected AutoApplier getCustomDataApplier0() {
        ServerVersion version = ServerVersion.getCurrentVersion(Bukkit.getServer());
        if (version.isNewerThanOrSameAs(ServerVersionRange.V26_1.minInclusive())) {
            return new AutoApplier_v26_1_base(this);
        } else if (InternalVersion.v1_21_R7.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_21_R7(this);
        } else if (InternalVersion.v1_21_R6.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_21_R6(this);
        } else if (InternalVersion.v1_21_R5.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_21_R5(this);
        } else if (InternalVersion.v1_21_R4.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_21_R4(this);
        } else if (InternalVersion.v1_21_R3.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_21_R3(this);
        } else if (InternalVersion.v1_21_R2.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_21_R2(this);
        } else if (InternalVersion.v1_21_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_21_R1(this);
        } else if (InternalVersion.v1_20_R4.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_20_R4(this);
        } else if (InternalVersion.v1_20_R3.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_20_R3(this);
        } else if (InternalVersion.v1_20_R2.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_20_R2(this);
        } else if (InternalVersion.v1_20_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_20_R1(this);
        } else if (InternalVersion.v1_19_R3.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_19_R3(this);
        } else if (InternalVersion.v1_19_R2.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_19_R2(this);
        } else if (InternalVersion.v1_19_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_19_R1(this);
        } else if (InternalVersion.v1_18_R2.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_18_R2(this);
        } else if (InternalVersion.v1_18_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_18_R1(this);
        } else if (InternalVersion.v1_17_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_17_R1(this);
        } else if (InternalVersion.v1_16_R3.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_16_R3(this);
        } else if (InternalVersion.v1_16_R2.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_16_R2(this);
        } else if (InternalVersion.v1_16_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_16_R1(this);
        } else if (InternalVersion.v1_15_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_15_R1(this);
        } else if (InternalVersion.v1_14_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_14_R1(this);
        } else if (InternalVersion.v1_13_R2.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_13_R2(this);
        } else if (InternalVersion.v1_13_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_13_R1(this);
        } else if (InternalVersion.v1_12_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_12_R1(this);
        } else if (InternalVersion.v1_11_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_11_R1(this);
        } else if (InternalVersion.v1_10_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_10_R1(this);
        } else if (InternalVersion.v1_9_R2.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_9_R2(this);
        } else if (InternalVersion.v1_9_R1.getServerVersionRange().isInRange(version)) {
            return new AutoApplier_v1_9_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
