package de.derfrzocker.custom.ore.generator.impl.v1_8_R1.customdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.TileEntity;
import net.minecraft.server.v1_8_R1.TileEntitySkull;
import net.minecraft.server.v1_8_R1.World;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SkullTextureApplier_v1_8_R1 implements CustomDataApplier {

    @NonNull
    private final CustomData customData;

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

}
