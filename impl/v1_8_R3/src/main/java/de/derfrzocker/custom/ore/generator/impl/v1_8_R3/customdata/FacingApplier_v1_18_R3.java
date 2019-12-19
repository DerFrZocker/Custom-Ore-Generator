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

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class FacingApplier_v1_18_R3 implements CustomDataApplier {

    private final static Map<String, EnumDirection> DIRECTION_MAP = new HashMap<>(6);

    static {
        DIRECTION_MAP.put("UP", EnumDirection.UP);
        DIRECTION_MAP.put("DOWN", EnumDirection.DOWN);
        DIRECTION_MAP.put("WEST", EnumDirection.WEST);
        DIRECTION_MAP.put("SOUTH", EnumDirection.SOUTH);
        DIRECTION_MAP.put("EAST", EnumDirection.EAST);
        DIRECTION_MAP.put("NORTH", EnumDirection.NORTH);
    }

    @NonNull
    private final CustomData customData;

    @Override
    public void apply(OreConfig oreConfig, Object location, Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final World world = (World) blockAccess;
        IBlockData iBlockData = world.getType(blockPosition);

        BlockStateDirection blockStateDirection = null;

        for (final IBlockState<?> iBlockState : iBlockData.getBlock().P().d()) {
            if (iBlockState.a().equals("facing")) {
                blockStateDirection = (BlockStateDirection) iBlockState;
                break;
            }
        }

        if (blockStateDirection == null)
            return; //TODO maybe throw exception?

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String facing = (String) objectOptional.get();

        iBlockData = iBlockData.set(blockStateDirection, DIRECTION_MAP.get(facing.toUpperCase()));

        world.setTypeAndData(blockPosition, iBlockData, 2);
    }

}
