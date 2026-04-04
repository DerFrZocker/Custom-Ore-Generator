package de.derfrzocker.custom.ore.generator.impl.v1_20_R1.customdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractSkullTextureCustomData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.generator.CraftLimitedRegion;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class SkullTextureApplier_v1_20_R1 implements AbstractSkullTextureCustomData.SkullTextureApplier {

    private final CustomData customData;

    public SkullTextureApplier_v1_20_R1(CustomData customData) {
        this.customData = customData;
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

        if (!(tileEntity instanceof SkullBlockEntity))
            return; //TODO maybe throw exception?

        final SkullBlockEntity skull = (SkullBlockEntity) tileEntity;
        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String texture = (String) objectOptional.get();
        final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);

        gameProfile.getProperties().put("textures", new Property("textures", texture));

        skull.setOwner(gameProfile);

        final CompoundTag nbtTagCompound = skull.saveWithFullMetadata();

        generatorAccess.getChunk(blockPosition).removeBlockEntity(blockPosition);
        generatorAccess.getChunk(blockPosition).setBlockEntityNbt(nbtTagCompound);
    }

    @Override
    public boolean hasCustomData(@NotNull final Skull skull) {
        final BlockEntity tileEntity = ((CraftWorld) skull.getWorld()).getHandle().getBlockEntity(new BlockPos(skull.getX(), skull.getY(), skull.getZ()));

        if (tileEntity == null)
            return false;

        if (!(tileEntity instanceof SkullBlockEntity))
            return false;

        final GameProfile gameProfile = ((SkullBlockEntity) tileEntity).owner;

        if (gameProfile == null)
            return false;

        final Collection<Property> propertyCollection = gameProfile.getProperties().get("textures");

        if (propertyCollection == null || propertyCollection.isEmpty())
            return false;

        for (final Property property : propertyCollection) {
            if (property.getName().equals("textures"))
                return true;
        }

        return false;
    }

    @Override
    public String getCustomData(@NotNull final Skull skull) {
        final SkullBlockEntity tileEntitySkull = (SkullBlockEntity) ((CraftWorld) skull.getWorld()).getHandle().getBlockEntity(new BlockPos(skull.getX(), skull.getY(), skull.getZ()));

        final GameProfile gameProfile = tileEntitySkull.owner;

        for (final Property property : gameProfile.getProperties().get("textures")) {
            if (property.getName().equals("textures"))
                return property.getValue();
        }

        throw new RuntimeException("TileEntitySkull on Location " + tileEntitySkull.getBlockPos() + " dont have the property 'textures' in its GameProfile " + gameProfile);
    }

}
