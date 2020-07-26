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

package de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractVariantCustomData;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.IBlockState;
import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VariantApplier_v1_8_R3 implements AbstractVariantCustomData.VariantApplier {

    @NotNull
    private final CustomData customData;

    public VariantApplier_v1_8_R3(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object location, @NotNull final Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final World world = (World) blockAccess;

        final IBlockData oldIBlockData = world.getType(blockPosition);

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final IBlockData newIBlockData = oldIBlockData.getBlock().fromLegacyData((Integer) objectOptional.get());
        final IBlockState variant = getVariantBlockState(oreConfig.getMaterial());

        if (variant == null)
            return; //TODO maybe throw exception?

        world.setTypeAndData(blockPosition, oldIBlockData.set(variant, newIBlockData.get(variant)), 2);
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return getVariantBlockState(oreConfig.getMaterial()) != null;
    }

    @Override
    public boolean isValidCustomData(@NotNull final Integer customData, @NotNull final OreConfig oreConfig) {
        final IBlockState<?> variant = getVariantBlockState(oreConfig.getMaterial());

        if (variant == null)
            return false;

        return variant.c().size() > customData;
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        final IBlockState<?> variant = getVariantBlockState(blockState.getType());

        if (variant == null)
            return false;

        final IBlockData iBlockData = ((CraftWorld) blockState.getWorld()).getHandle().getType(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()));

        return iBlockData.getBlock().toLegacyData(iBlockData) != 0;
    }

    @Override
    public int getCustomData(@NotNull final BlockState blockState) {
        final IBlockData iBlockData = ((CraftWorld) blockState.getWorld()).getHandle().getType(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()));

        return iBlockData.getBlock().toLegacyData(iBlockData);
    }

    @Nullable
    private IBlockState<?> getVariantBlockState(@NotNull final Material material) {
        IBlockState<?> variant = null;

        for (final IBlockState<?> iBlockState : CraftMagicNumbers.getBlock(material).P().d()) {
            if (iBlockState.a().equals("variant")) {
                variant = iBlockState;
                break;
            }
        }

        return variant;
    }

}
