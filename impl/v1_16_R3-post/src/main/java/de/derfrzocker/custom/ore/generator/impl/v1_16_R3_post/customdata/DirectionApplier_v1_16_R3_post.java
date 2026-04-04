package de.derfrzocker.custom.ore.generator.impl.v1_16_R3_post.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

public class DirectionApplier_v1_16_R3_post implements CustomDataApplier {

    private final CustomData customData;

    private final BlockFace blockFace;

    public DirectionApplier_v1_16_R3_post(CustomData customData, BlockFace blockFace) {
        this.customData = customData;
        this.blockFace = blockFace;
    }

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