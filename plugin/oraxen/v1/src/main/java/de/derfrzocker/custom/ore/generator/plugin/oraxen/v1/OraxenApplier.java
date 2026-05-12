package de.derfrzocker.custom.ore.generator.plugin.oraxen.v1;

import de.derfrzocker.custom.ore.generator.api.DebugLogger;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import io.th0rgal.oraxen.mechanics.MechanicsManager;
import io.th0rgal.oraxen.mechanics.provided.gameplay.block.BlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.block.BlockMechanicFactory;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanicFactory;
import io.th0rgal.oraxen.mechanics.provided.gameplay.stringblock.StringBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.stringblock.StringBlockMechanicFactory;
import java.util.Optional;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OraxenApplier implements CustomDataApplier {

    private final static BlockFace[] BLOCK_FACES = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH,
            BlockFace.NORTH, BlockFace.DOWN, BlockFace.UP};

    @NotNull
    private final CustomData customData;
    @Nullable
    private BlockMechanicFactory blockMechanicFactory;
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
        DebugLogger.log("Using Oraxen V1 Applier");
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;

        BlockData blockData = limitedRegion.getBlockData(location);

        Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent()) {
            DebugLogger.log("No Oraxen custom data found");
            return; //TODO maybe throw exception?
        }

        final String name = (String) objectOptional.get();
        DebugLogger.log("Found oraxen custom data: " + name);

        if (blockMechanicFactory == null) {
            blockMechanicFactory = (BlockMechanicFactory) MechanicsManager.getMechanicFactory("block");
        }
        if (noteBlockMechanicFactory == null) {
            noteBlockMechanicFactory = (NoteBlockMechanicFactory) MechanicsManager.getMechanicFactory("noteblock");
        }
        if (stringBlockMechanicFactory == null) {
            stringBlockMechanicFactory = (StringBlockMechanicFactory) MechanicsManager.getMechanicFactory("stringblock");
        }

        if (blockMechanicFactory != null) {
            BlockMechanic blockMechanic = (BlockMechanic) blockMechanicFactory.getMechanic(name);
            if (blockMechanic != null) {
                DebugLogger.log("Found block mechanic " + blockMechanic + " with custom variation " + blockMechanic.getCustomVariation());
                int code = blockMechanic.getCustomVariation();

                for (int i = 0; i < BLOCK_FACES.length; i++) {
                    ((MultipleFacing) blockData).setFace(BLOCK_FACES[i], (code & 0x1 << i) != 0);
                }
            } else {
                DebugLogger.log("No block mechanic found for custom data.");
            }
        } else {
            DebugLogger.log("No block mechanic factory found.");
        }

        if (noteBlockMechanicFactory != null) {
            NoteBlockMechanic noteBlockMechanic = noteBlockMechanicFactory.getMechanic(name);
            if (noteBlockMechanic != null) {
                DebugLogger.log("Found note mechanic " + noteBlockMechanic + " with custom variation " + noteBlockMechanic.getCustomVariation());
                blockData = NoteBlockMechanicFactory.createNoteBlockData(noteBlockMechanic.getCustomVariation());
            } else {
                DebugLogger.log("No note block mechanic found for custom data.");
            }
        } else {
            DebugLogger.log("No note block mechanic factory found.");
        }

        if (stringBlockMechanicFactory != null) {
            StringBlockMechanic stringBlockMechanic = stringBlockMechanicFactory.getMechanic(name);
            if (stringBlockMechanic != null) {
                DebugLogger.log("Found string mechanic " + stringBlockMechanic + " with custom variation " + stringBlockMechanic.getCustomVariation());
                blockData = StringBlockMechanicFactory.createTripwireData(stringBlockMechanic.getCustomVariation());
            } else {
                DebugLogger.log("No string block mechanic found for custom data.");
            }
        } else {
            DebugLogger.log("No string block mechanic factory found.");
        }

        DebugLogger.log("Setting block data to " + blockData);
        limitedRegion.setBlockData(location, blockData);
    }

}
