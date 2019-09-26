package de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.block.BlockFace;

import java.util.Optional;

@RequiredArgsConstructor
public class DirectionApplier_v1_18_R3 implements CustomDataApplier {

    @NonNull
    private final CustomData customData;

    private final BlockFace blockFace;

    @Override
    public void apply(OreConfig oreConfig, Object location, Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final World world = (World) blockAccess;
        IBlockData iBlockData = world.getType(blockPosition);

        BlockStateBoolean blockStateBoolean = null;

        for (final IBlockState<?> iBlockState : iBlockData.getBlock().P().d()) {
            if (iBlockState.a().equals(blockFace.name().toLowerCase())) {
                blockStateBoolean = (BlockStateBoolean) iBlockState;
                break;
            }
        }

        if (blockStateBoolean == null)
            return; //TODO maybe throw exception?

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        iBlockData = iBlockData.set(blockStateBoolean, (boolean) objectOptional.get());

        world.setTypeAndData(blockPosition, iBlockData, 2);
    }

}