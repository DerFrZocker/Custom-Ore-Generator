package de.derfrzocker.custom.ore.generator.impl.v1_17_R1.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import net.minecraft.core.BlockPos;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.generator.CraftLimitedRegion;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TickBlockApplier_v1_17_R1 implements CustomDataApplier {

    @NotNull
    private final CustomData customData;

    public TickBlockApplier_v1_17_R1(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object position, @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final boolean tickBlock = (boolean) objectOptional.get();

        if (!tickBlock)
            return;

        BlockPos blockPosition = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ((CraftLimitedRegion) limitedRegion).getHandle().getChunk(blockPosition).getBlockTicks().scheduleTick(blockPosition, null, -1, null);
    }

}
