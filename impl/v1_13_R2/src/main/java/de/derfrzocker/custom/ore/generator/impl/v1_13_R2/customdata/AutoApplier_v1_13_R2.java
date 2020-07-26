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

package de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractAutoCustomData;
import net.minecraft.server.v1_13_R2.*;
import org.apache.commons.lang.Validate;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AutoApplier_v1_13_R2 implements AbstractAutoCustomData.AutoApplier {

    @NotNull
    private final CustomData customData;

    public AutoApplier_v1_13_R2(@NotNull final CustomData data) {
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

        if (!(tileEntity instanceof TileEntityCommand))
            return; //TODO maybe throw exception?

        final TileEntityCommand tileCommand = (TileEntityCommand) tileEntity;
        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final boolean auto = (boolean) objectOptional.get();

        final NBTTagCompound nbtTagCompound = new NBTTagCompound();
        tileCommand.save(nbtTagCompound);
        nbtTagCompound.setBoolean("auto", auto);
        nbtTagCompound.setBoolean("conditionMet", auto);

        generatorAccess.y(blockPosition).d(blockPosition);
        generatorAccess.y(blockPosition).a(nbtTagCompound);
    }

    @Override
    public boolean getCustomData(@NotNull final CommandBlock commandBlock) {
        final WorldServer worldServer = ((CraftWorld) commandBlock.getWorld()).getHandle();
        final BlockPosition blockPosition = new BlockPosition(commandBlock.getX(), commandBlock.getY(), commandBlock.getZ());

        final TileEntity tileEntity = worldServer.getTileEntity(blockPosition);

        if (tileEntity == null) {
            return false;
        }

        if (!(tileEntity instanceof TileEntityCommand))
            return false;

        final TileEntityCommand tileEntityCommand = (TileEntityCommand) tileEntity;

        return tileEntityCommand.e();
    }

}
