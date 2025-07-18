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

package de.derfrzocker.custom.ore.generator.impl.v1_21_R5.customdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractSkullTextureCustomData;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R5.generator.CraftLimitedRegion;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

public class SkullTextureApplier_v1_21_R5 implements AbstractSkullTextureCustomData.SkullTextureApplier {

    private final CustomData customData;

    public SkullTextureApplier_v1_21_R5(CustomData customData) {
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
        final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), "");

        gameProfile.getProperties().put("textures", new Property("textures", texture));

        skull.setOwner(new ResolvableProfile(gameProfile));

        final CompoundTag nbtTagCompound = skull.saveWithFullMetadata(generatorAccess.registryAccess());

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

        final ResolvableProfile gameProfile = ((SkullBlockEntity) tileEntity).owner;

        if (gameProfile == null)
            return false;

        final Collection<Property> propertyCollection = gameProfile.properties().get("textures");

        if (propertyCollection == null || propertyCollection.isEmpty())
            return false;

        for (final Property property : propertyCollection) {
            if (property.name().equals("textures"))
                return true;
        }

        return false;
    }

    @Override
    public String getCustomData(@NotNull final Skull skull) {
        final SkullBlockEntity tileEntitySkull = (SkullBlockEntity) ((CraftWorld) skull.getWorld()).getHandle().getBlockEntity(new BlockPos(skull.getX(), skull.getY(), skull.getZ()));

        final ResolvableProfile gameProfile = tileEntitySkull.owner;

        for (final Property property : gameProfile.properties().get("textures")) {
            if (property.name().equals("textures"))
                return property.value();
        }

        throw new RuntimeException("TileEntitySkull on Location " + tileEntitySkull.getBlockPos() + " dont have the property 'textures' in its GameProfile " + gameProfile);
    }

}
