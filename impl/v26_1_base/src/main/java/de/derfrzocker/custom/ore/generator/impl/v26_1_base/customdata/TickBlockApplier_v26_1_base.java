package de.derfrzocker.custom.ore.generator.impl.v26_1_base.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ticks.ScheduledTick;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.generator.CraftLimitedRegion;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

public class TickBlockApplier_v26_1_base implements CustomDataApplier {

    @NotNull
    private final CustomData customData;

    public TickBlockApplier_v26_1_base(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(
            @NotNull final OreConfig oreConfig,
            @NotNull final Object position,
            @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent()) {
            return; //TODO maybe throw exception?
        }

        final boolean tickBlock = (boolean) objectOptional.get();

        if (!tickBlock) {
            return;
        }

        BlockPos blockPosition = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ((CraftLimitedRegion) limitedRegion)
                .getHandle()
                .getChunk(blockPosition)
                .getBlockTicks()
                .schedule(new ScheduledTick<>(
                        null,
                        blockPosition,
                        ((CraftLimitedRegion) limitedRegion).getHandle().getLevelData().getGameTime(),
                        ((CraftLimitedRegion) limitedRegion).getHandle().nextSubTickCount()));
    }

}
