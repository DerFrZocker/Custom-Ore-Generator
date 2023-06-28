/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
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

package de.derfrzocker.custom.ore.generator.impl.v1_20_R1.customdata;

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
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OraxenApplier_v1_20_R1 implements CustomDataApplier {

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

    public OraxenApplier_v1_20_R1(@NotNull CustomData customData) {
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
                int code = blockMechanic.getCustomVariation();

                for (int i = 0; i < BLOCK_FACES.length; i++) {
                    ((MultipleFacing) blockData).setFace(BLOCK_FACES[i], (code & 0x1 << i) != 0);
                }
            }
        }

        if (noteBlockMechanicFactory != null) {
            NoteBlockMechanic noteBlockMechanic = (NoteBlockMechanic) noteBlockMechanicFactory.getMechanic(name);
            if (noteBlockMechanic != null) {
                blockData = NoteBlockMechanicFactory.createNoteBlockData(noteBlockMechanic.getCustomVariation());
            }
        }

        if (stringBlockMechanicFactory != null) {
            StringBlockMechanic stringBlockMechanic = (StringBlockMechanic) stringBlockMechanicFactory.getMechanic(name);
            if (stringBlockMechanic != null) {
                blockData = StringBlockMechanicFactory.createTripwireData(stringBlockMechanic.getCustomVariation());
            }
        }

        limitedRegion.setBlockData(location, blockData);
    }

}
