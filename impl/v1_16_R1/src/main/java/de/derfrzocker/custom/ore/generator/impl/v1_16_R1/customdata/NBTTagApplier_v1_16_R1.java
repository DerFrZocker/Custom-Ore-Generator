/*
 * MIT License
 *
 * Copyright (c) 2019 Marvin (DerFrZocker)
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

package de.derfrzocker.custom.ore.generator.impl.v1_16_R1.customdata;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractNBTTagCustomData;
import net.minecraft.server.v1_16_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NBTTagApplier_v1_16_R1 implements AbstractNBTTagCustomData.NBTTagApplier {

    @NotNull
    private final CustomData customData;

    public NBTTagApplier_v1_16_R1(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object location, @NotNull final Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final GeneratorAccess generatorAccess = (GeneratorAccess) blockAccess;
        final TileEntity tileEntity = generatorAccess.getTileEntity(blockPosition);

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
        } catch (final CommandSyntaxException e) {
            throw new RuntimeException("Error while parsing String to NBTTagCompound", e);
        }

        tileEntity.load(generatorAccess.getType(blockPosition), nbtTagCompound);

        generatorAccess.z(blockPosition).removeTileEntity(blockPosition);
        generatorAccess.z(blockPosition).setTileEntity(blockPosition, tileEntity);
    }

    @Override
    public boolean isValidCustomData(@NotNull final String customData, @NotNull final OreConfig oreConfig) {
        try {
            MojangsonParser.parse(customData);
        } catch (final CommandSyntaxException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return CraftMagicNumbers.getBlock(oreConfig.getMaterial()).isTileEntity();
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        final TileEntity tileEntity = ((CraftWorld) blockState.getWorld()).getHandle().getTileEntity(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()));

        if (tileEntity == null)
            return false;

        final NBTTagCompound nbtTagCompound = new NBTTagCompound();

        tileEntity.save(nbtTagCompound);

        // A standard TileEntity has 4 keys, "id", "x", "y" and "z"
        // ,since we dont wont this keys in the customData, we return false if only this 4 keys are present
        return nbtTagCompound.e() > 4;
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
