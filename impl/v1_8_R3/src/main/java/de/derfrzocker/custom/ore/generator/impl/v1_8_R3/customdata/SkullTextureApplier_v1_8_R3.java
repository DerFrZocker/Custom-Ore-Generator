package de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractSkullTextureCustomData;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class SkullTextureApplier_v1_8_R3 implements AbstractSkullTextureCustomData.SkullTextureApplier {

    private final CustomData customData;

    public SkullTextureApplier_v1_8_R3(CustomData customData) {
        this.customData = customData;
    }

    @Override
    public void apply(OreConfig oreConfig, Object location, Object blockAccess) {

        final BlockPosition blockPosition = (BlockPosition) location;
        final World world = (World) blockAccess;
        final TileEntity tileEntity = world.getTileEntity(blockPosition);

        if (tileEntity == null)
            return; //TODO maybe throw exeption?

        if (!(tileEntity instanceof TileEntitySkull))
            return; //TODO maybe throw exeption?

        final TileEntitySkull skull = (TileEntitySkull) tileEntity;
        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exeption?

        final String texture = (String) objectOptional.get();
        final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);

        gameProfile.getProperties().put("textures", new Property("textures", texture));

        skull.setGameProfile(gameProfile);
    }

    @Override
    public boolean hasCustomData(@NotNull final Skull skull) {
        final TileEntity tileEntity = ((CraftWorld) skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));

        if (tileEntity == null)
            return false;

        if (!(tileEntity instanceof TileEntitySkull))
            return false;

        final GameProfile gameProfile = ((TileEntitySkull) tileEntity).getGameProfile();

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
        final TileEntitySkull tileEntitySkull = (TileEntitySkull) ((CraftWorld) skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));

        final GameProfile gameProfile = tileEntitySkull.getGameProfile();

        for (final Property property : gameProfile.getProperties().get("textures")) {
            if (property.getName().equals("textures"))
                return property.getValue();
        }

        throw new RuntimeException("TileEntitySkull on Location " + tileEntitySkull.getPosition() + " dont have the property 'textures' in its GameProfile " + gameProfile);
    }

}
