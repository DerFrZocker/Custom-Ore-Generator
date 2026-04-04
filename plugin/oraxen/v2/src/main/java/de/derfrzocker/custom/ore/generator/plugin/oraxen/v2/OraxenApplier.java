package de.derfrzocker.custom.ore.generator.plugin.oraxen.v2;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import io.th0rgal.oraxen.mechanics.MechanicsManager;
import io.th0rgal.oraxen.mechanics.provided.gameplay.custom_block.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.custom_block.noteblock.NoteBlockMechanicFactory;
import io.th0rgal.oraxen.mechanics.provided.gameplay.custom_block.stringblock.StringBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.custom_block.stringblock.StringBlockMechanicFactory;
import java.util.Optional;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OraxenApplier implements CustomDataApplier {

    private final static BlockFace[] BLOCK_FACES = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH,
            BlockFace.NORTH, BlockFace.DOWN, BlockFace.UP};

    @NotNull
    private final CustomData customData;
    @Nullable
    private NoteBlockMechanicFactory noteBlockMechanicFactory;
    @Nullable
    private StringBlockMechanicFactory stringBlockMechanicFactory;

    public OraxenApplier(@NotNull CustomData customData) {
        Validate.notNull(customData, "CustomData can not be null");

        this.customData = customData;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object position, @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;

        BlockData blockData = limitedRegion.getBlockData(location);

        Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String name = (String) objectOptional.get();

        if (noteBlockMechanicFactory == null) {
            noteBlockMechanicFactory = (NoteBlockMechanicFactory) MechanicsManager.getMechanicFactory("noteblock");
        }
        if (stringBlockMechanicFactory == null) {
            stringBlockMechanicFactory = (StringBlockMechanicFactory) MechanicsManager.getMechanicFactory("stringblock");
        }

        if (noteBlockMechanicFactory != null) {
            NoteBlockMechanic noteBlockMechanic = noteBlockMechanicFactory.getMechanic(name);
            if (noteBlockMechanic != null) {
                blockData = noteBlockMechanic.blockData();
            }
        }

        if (stringBlockMechanicFactory != null) {
            StringBlockMechanic stringBlockMechanic = stringBlockMechanicFactory.getMechanic(name);
            if (stringBlockMechanic != null) {
                blockData = stringBlockMechanic.blockData();
            }
        }

        limitedRegion.setBlockData(location, blockData);
    }

}
