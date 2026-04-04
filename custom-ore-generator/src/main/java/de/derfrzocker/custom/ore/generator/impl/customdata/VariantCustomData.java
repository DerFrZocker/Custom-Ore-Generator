package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata.VariantApplier_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata.VariantApplier_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.customdata.VariantApplier_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata.VariantApplier_v1_8_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.customdata.VariantApplier_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.customdata.VariantApplier_v1_9_R2;
import de.derfrzocker.spigot.utils.version.InternalVersion;
import de.derfrzocker.spigot.utils.version.ServerVersion;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class VariantCustomData extends AbstractVariantCustomData {

    public VariantCustomData(@NotNull final Function<String, Info> infoFunction) {
        super(infoFunction);
    }

    @NotNull
    @Override
    protected VariantApplier getCustomDataApplier0() {
        ServerVersion version = ServerVersion.getCurrentVersion(Bukkit.getServer());
        if (InternalVersion.v1_12_R1.getServerVersionRange().isInRange(version)) {
            return new VariantApplier_v1_12_R1(this);
        } else if (InternalVersion.v1_11_R1.getServerVersionRange().isInRange(version)) {
            return new VariantApplier_v1_11_R1(this);
        } else if (InternalVersion.v1_10_R1.getServerVersionRange().isInRange(version)) {
            return new VariantApplier_v1_10_R1(this);
        } else if (InternalVersion.v1_9_R2.getServerVersionRange().isInRange(version)) {
            return new VariantApplier_v1_9_R2(this);
        } else if (InternalVersion.v1_9_R1.getServerVersionRange().isInRange(version)) {
            return new VariantApplier_v1_9_R1(this);
        } else if (InternalVersion.v1_8_R3.getServerVersionRange().isInRange(version)) {
            return new VariantApplier_v1_8_R3(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
