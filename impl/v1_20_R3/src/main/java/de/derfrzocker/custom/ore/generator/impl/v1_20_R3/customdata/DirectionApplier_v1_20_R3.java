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

package de.derfrzocker.custom.ore.generator.impl.v1_20_R3.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
public class DirectionApplier_v1_20_R3 implements CustomDataApplier {

    @NonNull
    private final CustomData customData;

    private final BlockFace blockFace;

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object position, @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;
        MultipleFacing blockData = (MultipleFacing) limitedRegion.getBlockData(location);

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        blockData.setFace(blockFace, (boolean) objectOptional.get());

        limitedRegion.setBlockData(location, blockData);
    }

}