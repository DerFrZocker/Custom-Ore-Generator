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

package de.derfrzocker.custom.ore.generator.impl.v1_21_R2.customdata;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractNBTTagCustomData;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.generator.CraftLimitedRegion;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftMagicNumbers;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

public class NBTTagApplier_v1_21_R2 implements AbstractNBTTagCustomData.NBTTagApplier {

    @NotNull
    private final CustomData customData;

    public NBTTagApplier_v1_21_R2(@NotNull final CustomData data) {
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

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String nbtTag = (String) objectOptional.get();

        final CompoundTag nbtTagCompound = tileEntity.saveWithFullMetadata(generatorAccess.registryAccess());

        try {
            final CompoundTag nbtTagCompound1 = TagParser.parseTag(nbtTag);

            nbtTagCompound.merge(nbtTagCompound1);
        } catch (final CommandSyntaxException e) {
            throw new RuntimeException("Error while parsing String to NBTTagCompound", e);
        }

        tileEntity.loadWithComponents(nbtTagCompound, generatorAccess.registryAccess());

        generatorAccess.getChunk(blockPosition).removeBlockEntity(blockPosition);
        generatorAccess.getChunk(blockPosition).setBlockEntity(tileEntity);
    }

    @Override
    public boolean isValidCustomData(@NotNull final String customData, @NotNull final OreConfig oreConfig) {
        try {
            TagParser.parseTag(customData);
        } catch (final CommandSyntaxException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return CraftMagicNumbers.getBlock(oreConfig.getMaterial()).defaultBlockState().hasBlockEntity();
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        final BlockEntity tileEntity = ((CraftWorld) blockState.getWorld()).getHandle().getBlockEntity(new BlockPos(blockState.getX(), blockState.getY(), blockState.getZ()));

        if (tileEntity == null)
            return false;

        return tileEntity.saveWithoutMetadata(((CraftWorld) blockState.getWorld()).getHandle().registryAccess()).size() != 0;
    }

    @NotNull
    @Override
    public String getCustomData(@NotNull final BlockState blockState) {
        final BlockEntity tileEntity = ((CraftWorld) blockState.getWorld()).getHandle().getBlockEntity(new BlockPos(blockState.getX(), blockState.getY(), blockState.getZ()));
        return tileEntity.saveWithoutMetadata(((CraftWorld) blockState.getWorld()).getHandle().registryAccess()).toString();
    }

}
