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
import java.util.function.Function;

public class HighestBlockBlockSelector extends AbstractBlockSelector {

    private final static Set<OreSetting> NEEDED_ORE_SETTINGS = Collections.unmodifiableSet(Sets.newHashSet(OreSettings.VEINS_PER_CHUNK));

    public HighestBlockBlockSelector(@NotNull Function<String, Info> infoFunction) {
        super("HIGHEST_BLOCK", NEEDED_ORE_SETTINGS, infoFunction);
    }

    @NotNull
    @Override
    public Set<Location> selectBlocks(@NotNull final ChunkInfo chunkInfo, @NotNull final OreConfig config, @NotNull final Random random) {
        final Set<Location> locations = new HashSet<>();

        final int veinsPerChunk = NumberUtil.getInt(config.getValue(OreSettings.VEINS_PER_CHUNK).orElse(0d), random);

        for (int i = 0; i < veinsPerChunk; i++) {
            final int x = random.nextInt(16);
            final int z = random.nextInt(16);

            locations.add(new Location(null, x, chunkInfo.getHighestBlock(x, z) - 1, z));
        }

        return locations;
    }

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, final double value, @NotNull final OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(getNeededOreSettings().contains(oreSetting), "The BlockSelector '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        return value >= 0;
    }

}
