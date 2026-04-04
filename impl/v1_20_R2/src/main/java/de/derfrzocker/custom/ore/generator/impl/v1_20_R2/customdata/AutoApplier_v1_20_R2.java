package de.derfrzocker.custom.ore.generator.impl.v1_20_R2.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractAutoCustomData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.generator.CraftLimitedRegion;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AutoApplier_v1_20_R2 implements AbstractAutoCustomData.AutoApplier {

    @NotNull
    private final CustomData customData;

    public AutoApplier_v1_20_R2(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object position, @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;
        final BlockPos blockPosition = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final WorldGenLevel generatorAccess = ((CraftLimitedRegion) limitedRegion).getHandle();
        final BlockEntity tileEntity = generatorAccess.getBlockEntity(blockPosition);

        if (tileEntity == null)
            return; //TODO maybe throw exception?

        if (!(tileEntity instanceof CommandBlockEntity))
            return; //TODO maybe throw exception?

        final CommandBlockEntity tileCommand = (CommandBlockEntity) tileEntity;
        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final boolean auto = (boolean) objectOptional.get();

        final CompoundTag nbtTagCompound = tileCommand.saveWithFullMetadata();
        nbtTagCompound.putBoolean("auto", auto);
        nbtTagCompound.putBoolean("conditionMet", auto);

        generatorAccess.getChunk(blockPosition).removeBlockEntity(blockPosition);
        generatorAccess.getChunk(blockPosition).setBlockEntityNbt(nbtTagCompound);
    }

    @Override
    public boolean getCustomData(@NotNull final CommandBlock commandBlock) {
        final ServerLevel worldServer = ((CraftWorld) commandBlock.getWorld()).getHandle();
        final BlockPos blockPosition = new BlockPos(commandBlock.getX(), commandBlock.getY(), commandBlock.getZ());

        final BlockEntity tileEntity = worldServer.getBlockEntity(blockPosition);

        if (tileEntity == null) {
            return false;
        }

        if (!(tileEntity instanceof CommandBlockEntity))
            return false;

        final CommandBlockEntity tileEntityCommand = (CommandBlockEntity) tileEntity;

        return tileEntityCommand.isAutomatic();
    }

}
