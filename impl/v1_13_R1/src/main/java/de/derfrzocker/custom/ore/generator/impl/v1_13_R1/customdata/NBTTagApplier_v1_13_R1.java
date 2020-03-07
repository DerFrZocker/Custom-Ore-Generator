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

package de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractNBTTagCustomData;
import net.minecraft.server.v1_13_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NBTTagApplier_v1_13_R1 implements AbstractNBTTagCustomData.NBTTagApplier {

    @NotNull
    private final CustomData customData;

    public NBTTagApplier_v1_13_R1(@NotNull final CustomData data) {
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

        tileEntity.load(nbtTagCompound);

        generatorAccess.y(blockPosition).d(blockPosition);
        generatorAccess.y(blockPosition).a(blockPosition, tileEntity);
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return CraftMagicNumbers.getBlock(oreConfig.getMaterial()).isTileEntity();
    }

}
