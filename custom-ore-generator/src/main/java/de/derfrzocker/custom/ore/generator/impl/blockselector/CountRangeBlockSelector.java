package de.derfrzocker.custom.ore.generator.impl.blockselector;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CountRangeBlockSelector implements BlockSelector {

    private final Set<OreSetting>  neededOreSettings = Collections.unmodifiableSet(Sets.newHashSet(OreSettings.HEIGHT_RANGE, OreSettings.MINIMUM_HEIGHT, OreSettings.VEINS_PER_CHUNK));

    @NotNull
    @Override
    public Set<Location> selectBlocks(@NotNull final OreConfig config, @NotNull final Random random) {
        final Set<Location> locations = new HashSet<>();

        final int heightRange = config.getValue(OreSettings.HEIGHT_RANGE).orElse(OreSettings.HEIGHT_RANGE.getSaveValue());
        final int minimumHeight = config.getValue(OreSettings.MINIMUM_HEIGHT).orElse(OreSettings.MINIMUM_HEIGHT.getSaveValue());
        final int veinsPerChunk = config.getValue(OreSettings.VEINS_PER_CHUNK).orElse(OreSettings.VEINS_PER_CHUNK.getSaveValue());

        for(int i = 0; i < veinsPerChunk; i++){
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

}
