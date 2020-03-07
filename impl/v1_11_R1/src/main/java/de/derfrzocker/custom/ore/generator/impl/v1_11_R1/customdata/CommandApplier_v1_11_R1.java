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

package de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.TileEntity;
import net.minecraft.server.v1_11_R1.TileEntityCommand;
import net.minecraft.server.v1_11_R1.World;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CommandApplier_v1_11_R1 implements CustomDataApplier {

    @NotNull
    private final CustomData customData;

    public CommandApplier_v1_11_R1(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object location, @NotNull final Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final World world = (World) blockAccess;
        final TileEntity tileEntity = world.getTileEntity(blockPosition);

        if (tileEntity == null)
            return; //TODO maybe throw exception?

        if (!(tileEntity instanceof TileEntityCommand))
            return; //TODO maybe throw exception?

        final TileEntityCommand tileCommand = (TileEntityCommand) tileEntity;
        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String command = (String) objectOptional.get();
        tileCommand.getCommandBlock().setCommand(command);
    }

}
