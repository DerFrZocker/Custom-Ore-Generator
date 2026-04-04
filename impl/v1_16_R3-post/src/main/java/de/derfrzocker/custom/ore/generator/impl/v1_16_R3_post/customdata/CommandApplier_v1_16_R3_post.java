package de.derfrzocker.custom.ore.generator.impl.v1_16_R3_post.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import java.util.Optional;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

public class CommandApplier_v1_16_R3_post implements CustomDataApplier {

    @NotNull
    private final CustomData customData;

    public CommandApplier_v1_16_R3_post(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object position, @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;
        final CommandBlock commandBlock = (CommandBlock) limitedRegion.getBlockState(location);

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String command = (String) objectOptional.get();
        commandBlock.setCommand(command);
        commandBlock.update();
    }

}
