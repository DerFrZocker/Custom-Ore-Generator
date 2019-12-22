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

package de.derfrzocker.custom.ore.generator.impl.blockselector;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.NumberUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CountRangeBlockSelector implements BlockSelector {

    private final Set<OreSetting> neededOreSettings = Collections.unmodifiableSet(Sets.newHashSet(OreSettings.HEIGHT_RANGE, OreSettings.MINIMUM_HEIGHT, OreSettings.VEINS_PER_CHUNK));

    @NotNull
    @Override
    public Set<Location> selectBlocks(@NotNull final ChunkInfo chunkInfo, @NotNull final OreConfig config, @NotNull final Random random) {
        final Set<Location> locations = new HashSet<>();

        final int heightRange = NumberUtil.getInt(config.getValue(OreSettings.HEIGHT_RANGE).orElse(0d), random);
        final int minimumHeight = NumberUtil.getInt(config.getValue(OreSettings.MINIMUM_HEIGHT).orElse(0d), random);
        final int veinsPerChunk = NumberUtil.getInt(config.getValue(OreSettings.VEINS_PER_CHUNK).orElse(0d), random);

        if (heightRange == 0)
            return locations;

        for (int i = 0; i < veinsPerChunk; i++) {
            final int x = random.nextInt(16);
            final int y = random.nextInt(heightRange) + minimumHeight;
            final int z = random.nextInt(16);

            locations.add(new Location(null, x, y, z));
        }

        return locations;
    }

    @NotNull
    @Override
    public Set<OreSetting> getNeededOreSettings() {
        return this.neededOreSettings;
    }

    @NotNull
    @Override
    public String getName() {
        return "COUNT_RANGE";
    }

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, final double value, @NotNull final OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(neededOreSettings.contains(oreSetting), "The BlockSelector '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        if (oreSetting == OreSettings.HEIGHT_RANGE)
            return value >= 0;

        if (oreSetting == OreSettings.MINIMUM_HEIGHT)
            return value >= 0;

        if (oreSetting == OreSettings.VEINS_PER_CHUNK)
            return value >= 0;

        throw new RuntimeException("Wtf?");
    }

}
