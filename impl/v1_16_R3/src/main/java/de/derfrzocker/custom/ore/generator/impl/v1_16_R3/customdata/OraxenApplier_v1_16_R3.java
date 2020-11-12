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

package de.derfrzocker.custom.ore.generator.impl.v1_16_R3.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import io.th0rgal.oraxen.mechanics.MechanicsManager;
import io.th0rgal.oraxen.mechanics.provided.block.BlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.block.BlockMechanicFactory;
import net.minecraft.server.v1_16_R3.*;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OraxenApplier_v1_16_R3 implements CustomDataApplier {

    private final static EnumDirection[] DIRECTIONS = new EnumDirection[]{EnumDirection.EAST, EnumDirection.WEST, EnumDirection.SOUTH,
            EnumDirection.NORTH, EnumDirection.DOWN, EnumDirection.UP};

    @NotNull
    private final CustomData customData;
    @Nullable
    private BlockMechanicFactory mechanicFactory;

    public OraxenApplier_v1_16_R3(@NotNull CustomData customData) {
        Validate.notNull(customData, "CustomData can not be null");

        this.customData = customData;
    }

    @Override
    public void apply(@NotNull OreConfig oreConfig, @NotNull Object location, @NotNull Object blockAccess) {
        BlockPosition blockPosition = (BlockPosition) location;
        GeneratorAccess generatorAccess = (GeneratorAccess) blockAccess;
        IBlockData iBlockData = generatorAccess.getType(blockPosition);

        Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String name = (String) objectOptional.get();

        if (mechanicFactory == null) {
            mechanicFactory = (BlockMechanicFactory) MechanicsManager.getMechanicFactory("block");
        }

        BlockMechanic mechanic = (BlockMechanic) mechanicFactory.getMechanic(name);

        int code = mechanic.getCustomVariation();

        for (int i = 0; i < DIRECTIONS.length; i++) {
            EnumDirection direction = DIRECTIONS[i];
            BlockStateBoolean blockStateBoolean = (BlockStateBoolean) iBlockData.getBlock().getStates().a(direction.name().toLowerCase());
            iBlockData = iBlockData.set(blockStateBoolean, (code & 0x1 << i) != 0);
        }

        generatorAccess.setTypeAndData(blockPosition, iBlockData, 2);
    }

}
