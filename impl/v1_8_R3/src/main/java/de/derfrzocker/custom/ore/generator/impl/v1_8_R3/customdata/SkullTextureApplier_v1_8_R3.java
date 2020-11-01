/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractSkullTextureCustomData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class SkullTextureApplier_v1_8_R3 implements AbstractSkullTextureCustomData.SkullTextureApplier {

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
