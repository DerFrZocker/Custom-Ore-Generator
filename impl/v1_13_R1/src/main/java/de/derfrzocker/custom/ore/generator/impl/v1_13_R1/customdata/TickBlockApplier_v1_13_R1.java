package de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.GeneratorAccess;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public class TickBlockApplier_v1_13_R1 implements CustomDataApplier {

    @NotNull
    private final CustomData customData;

    public TickBlockApplier_v1_13_R1(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object location, @NotNull final Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final GeneratorAccess generatorAccess = (GeneratorAccess) blockAccess;

        generatorAccess.y(blockPosition).k().a(blockPosition, null, -1, null);
    }

}
