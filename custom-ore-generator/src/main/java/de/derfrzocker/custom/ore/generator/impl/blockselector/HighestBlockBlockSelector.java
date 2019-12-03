package de.derfrzocker.custom.ore.generator.impl.blockselector;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.NumberUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HighestBlockBlockSelector implements BlockSelector {

    private final Set<OreSetting> neededOreSettings = Collections.unmodifiableSet(Sets.newHashSet(OreSettings.VEINS_PER_CHUNK));

    @NotNull
    @Override
    public Set<Location> selectBlocks(@NotNull final ChunkInfo chunkInfo, @NotNull final OreConfig config, @NotNull final Random random) {
        final Set<Location> locations = new HashSet<>();

        final int veinsPerChunk = NumberUtil.getInt(config.getValue(OreSettings.VEINS_PER_CHUNK).orElse(OreSettings.VEINS_PER_CHUNK.getSaveValue()), random);

        for (int i = 0; i < veinsPerChunk; i++) {
            final int x = random.nextInt(16);
            final int z = random.nextInt(16);

            locations.add(new Location(null, x, chunkInfo.getHighestBlock(x, z) - 1, z));
        }

        return locations;
    }

    @NotNull
    @Override
    public Set<OreSetting> getNeededOreSettings() {
        return neededOreSettings;
    }

    @NotNull
    @Override
    public String getName() {
        return "HIGHEST_BLOCK";
    }

}
