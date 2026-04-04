package de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractNBTTagCustomData;
import net.minecraft.server.v1_10_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NBTTagApplier_v1_10_R1 implements AbstractNBTTagCustomData.NBTTagApplier {

    @NotNull
    private final CustomData customData;

    public NBTTagApplier_v1_10_R1(@NotNull final CustomData data) {
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

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String nbtTag = (String) objectOptional.get();

        final NBTTagCompound nbtTagCompound = new NBTTagCompound();
        tileEntity.save(nbtTagCompound);

        try {
            final NBTTagCompound nbtTagCompound1 = MojangsonParser.parse(nbtTag);

            nbtTagCompound.a(nbtTagCompound1);
        } catch (final MojangsonParseException e) {
            throw new RuntimeException("Error while parsing String to NBTTagCompound", e);
        }

        tileEntity.a(nbtTagCompound);
    }

    @Override
    public boolean isValidCustomData(@NotNull final String customData, @NotNull final OreConfig oreConfig) {
        try {
            MojangsonParser.parse(customData);
        } catch (final MojangsonParseException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return CraftMagicNumbers.getBlock(oreConfig.getMaterial()).isTileEntity();
    }

    @Override
    public boolean hasCustomData(@NotNull final org.bukkit.block.BlockState blockState) {
        final TileEntity tileEntity = ((CraftWorld) blockState.getWorld()).getHandle().getTileEntity(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()));

        if (tileEntity == null)
            return false;

        final NBTTagCompound nbtTagCompound = new NBTTagCompound();

        tileEntity.save(nbtTagCompound);

        // A standard TileEntity has 4 keys, "id", "x", "y" and "z"
        // ,since we dont wont this keys in the customData, we return false if only this 4 keys are present
        return nbtTagCompound.d() > 4;
    }

    @NotNull
    @Override
    public String getCustomData(@NotNull final BlockState blockState) {
        final TileEntity tileEntity = ((CraftWorld) blockState.getWorld()).getHandle().getTileEntity(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()));
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();

        tileEntity.save(nbtTagCompound);

        //removing the unwanted NBT keys and values
        nbtTagCompound.remove("id");
        nbtTagCompound.remove("x");
        nbtTagCompound.remove("y");
        nbtTagCompound.remove("z");

        return nbtTagCompound.toString();
    }

}
