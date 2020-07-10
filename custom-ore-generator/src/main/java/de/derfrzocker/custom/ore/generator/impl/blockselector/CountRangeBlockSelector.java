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
import java.util.function.BiFunction;
import java.util.function.Function;

public class CountRangeBlockSelector extends AbstractBlockSelector {

    private final static OreSetting HEIGHT_RANGE = OreSetting.createOreSetting("HEIGHT_RANGE");
    private final static OreSetting MINIMUM_HEIGHT = OreSetting.createOreSetting("MINIMUM_HEIGHT");
    private final static OreSetting VEINS_PER_CHUNK = OreSetting.createOreSetting("VEINS_PER_CHUNK");
    private final static Set<OreSetting> NEEDED_ORE_SETTINGS = Collections.unmodifiableSet(Sets.newHashSet(HEIGHT_RANGE, MINIMUM_HEIGHT, VEINS_PER_CHUNK));

    /**
     * The infoFunction gives the name of the BlockSelector as value.
     * The oreSettingInfo gives the name of the BlockSelector and the OreSetting as values.
     *
     * @param infoFunction   function to get the info object of this BlockSelector
     * @param oreSettingInfo biFunction to get the info object of a given OreSetting
     * @throws IllegalArgumentException if one of the arguments are null
     */
    public CountRangeBlockSelector(@NotNull final Function<String, Info> infoFunction, @NotNull final BiFunction<String, OreSetting, Info> oreSettingInfo) {
        super("COUNT_RANGE", NEEDED_ORE_SETTINGS, infoFunction, oreSettingInfo);
    }

    @NotNull
    @Override
    public Set<Location> selectBlocks(@NotNull final ChunkInfo chunkInfo, @NotNull final OreConfig config, @NotNull final Random random) {
        final Set<Location> locations = new HashSet<>();
        final OreSettingContainer oreSettingContainer = config.getBlockSelectorOreSettings();

        final int heightRange = NumberUtil.getInt(oreSettingContainer.getValue(HEIGHT_RANGE).orElse(0d), random);
        final int minimumHeight = NumberUtil.getInt(oreSettingContainer.getValue(MINIMUM_HEIGHT).orElse(0d), random);
        final int veinsPerChunk = NumberUtil.getInt(oreSettingContainer.getValue(VEINS_PER_CHUNK).orElse(0d), random);

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

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, final double value, @NotNull final OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(getNeededOreSettings().contains(oreSetting), "The BlockSelector '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        if (oreSetting == HEIGHT_RANGE)
            return value >= 0;

        if (oreSetting == MINIMUM_HEIGHT)
            return value >= 0;

        if (oreSetting == VEINS_PER_CHUNK)
            return value >= 0;

        throw new RuntimeException("Wtf?");
    }

}
