package de.derfrzocker.custom.ore.generator.impl.v1_9_R1.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.TileEntity;
import net.minecraft.server.v1_9_R1.TileEntityCommand;
import net.minecraft.server.v1_9_R1.World;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CommandApplier_v1_9_R1 implements CustomDataApplier {

    @NotNull
    private final CustomData customData;

    public CommandApplier_v1_9_R1(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object location, @NotNull final Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final World world = (World) blockAccess;
        final TileEntity tileEntity = world.getTileEntity(blockPosition);

        if (tileEntity == null)
            return; //TODO maybe throw exception?

        if (!(tileEntity instanceof TileEntityCommand))
            return; //TODO maybe throw exception?

        final TileEntityCommand tileCommand = (TileEntityCommand) tileEntity;
        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String command = (String) objectOptional.get();
        tileCommand.getCommandBlock().setCommand(command);
    }

}
