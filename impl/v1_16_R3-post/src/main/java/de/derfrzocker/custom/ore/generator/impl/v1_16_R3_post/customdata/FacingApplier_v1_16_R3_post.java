package de.derfrzocker.custom.ore.generator.impl.v1_16_R3_post.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;

public class FacingApplier_v1_16_R3_post implements CustomDataApplier {

    private final static Map<String, BlockFace> DIRECTION_MAP = new HashMap<>(6);

    static {
        DIRECTION_MAP.put("UP", BlockFace.UP);
        DIRECTION_MAP.put("DOWN", BlockFace.DOWN);
        DIRECTION_MAP.put("WEST", BlockFace.WEST);
        DIRECTION_MAP.put("SOUTH", BlockFace.SOUTH);
        DIRECTION_MAP.put("EAST", BlockFace.EAST);
        DIRECTION_MAP.put("NORTH", BlockFace.NORTH);
    }

    private final CustomData customData;

    public FacingApplier_v1_16_R3_post(CustomData customData) {
        this.customData = customData;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object position, @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;
        Directional blockData = (Directional) limitedRegion.getBlockData(location);

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String facing = (String) objectOptional.get();

        blockData.setFacing(DIRECTION_MAP.get(facing.toUpperCase()));

        limitedRegion.setBlockData(location, blockData);
    }

}
