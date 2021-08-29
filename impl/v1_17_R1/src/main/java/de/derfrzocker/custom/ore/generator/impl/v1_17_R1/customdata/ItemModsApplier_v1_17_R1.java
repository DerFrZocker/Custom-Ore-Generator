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

package de.derfrzocker.custom.ore.generator.impl.v1_17_R1.customdata;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.generator.CraftLimitedRegion;
import org.bukkit.craftbukkit.v1_17_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_17_R1.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemModsApplier_v1_17_R1 implements CustomDataApplier {

    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    @NotNull
    private final CustomData customData;

    public ItemModsApplier_v1_17_R1(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object position, @NotNull final Object blockAccess) {
        final Location location = (Location) position;
        final LimitedRegion limitedRegion = (LimitedRegion) blockAccess;
        final BlockPos blockPosition = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final WorldGenLevel generatorAccess = ((CraftLimitedRegion) limitedRegion).getHandle();

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exception?

        final String name = (String) objectOptional.get();
        final Optional<BlockConfig> blockConfigOptional = ItemMods.getPlugin().getCustomBlockManager().getBlocks().stream().filter(blockConfig -> blockConfig.getName().equals(name)).findAny();

        if (!blockConfigOptional.isPresent())
            return; //TODO maybe throw exception?

        final BlockConfig blockConfig = blockConfigOptional.get();
        final ArmorStandBlockConfig armorStandBlockConfig = blockConfig.getArmorStand();

        if (armorStandBlockConfig != null) {
            final org.bukkit.entity.ArmorStand armorStand = limitedRegion.spawn(location, org.bukkit.entity.ArmorStand.class);

            armorStand.setSmall(armorStandBlockConfig.isSmall());
            armorStand.setMarker(armorStandBlockConfig.isMarker());
            armorStand.setInvulnerable(armorStandBlockConfig.isInvulnerable());
            armorStand.setCustomNameVisible(armorStandBlockConfig.isCustomNameVisible());
            armorStand.setCustomName(armorStandBlockConfig.getCustomName());
            armorStand.setInvisible(armorStandBlockConfig.isInvisible());
            armorStand.addScoreboardTag(blockConfig.getTag());
            armorStand.setGravity(false);
            armorStand.setSilent(true);
            armorStand.setBasePlate(armorStandBlockConfig.isBasePlate());
            armorStand.getEquipment().setHelmet(armorStandBlockConfig.getHelmet());
            armorStand.getEquipment().setChestplate(armorStandBlockConfig.getChestplate());
            armorStand.getEquipment().setLeggings(armorStandBlockConfig.getLeggings());
            armorStand.getEquipment().setBoots(armorStandBlockConfig.getBoots());
            armorStand.getEquipment().setItemInMainHand(armorStandBlockConfig.getMainHand());
            armorStand.getEquipment().setItemInOffHand(armorStandBlockConfig.getOffHand());

            // Fixing ArmorStand rotating issue, I have now idea why the yaw and/or pitch is another value than 0.
            // That needs a more detailed investigation, which of the above methods changes the yaw and/or pitch,
            // but for now it works.
            armorStand.setRotation(0, 0);
            armorStand.getPersistentDataContainer().set(new NamespacedKey(ItemMods.getPlugin(), "type"), PersistentDataType.STRING, blockConfig.getTag());
        }

        final BlockEntity tileEntity = generatorAccess.getBlockEntity(blockPosition);

        if (tileEntity != null) {
            if (blockConfig.getData() != null) {
                final CompoundTag nbtTagCompound = new CompoundTag();
                tileEntity.save(nbtTagCompound);

                try {
                    final CompoundTag nbtTagCompound1 = TagParser.parseTag(blockConfig.getData());

                    nbtTagCompound.merge(nbtTagCompound1);
                } catch (final CommandSyntaxException e) {
                    throw new RuntimeException("Error while parsing String to NBTTagCompound", e);
                }

                tileEntity.load(nbtTagCompound);
            }

            if (blockConfig.getTag() != null) {
                if (tileEntity.persistentDataContainer == null) {
                    tileEntity.persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);
                }

                tileEntity.persistentDataContainer.set(new NamespacedKey(ItemMods.getPlugin(), "type"), PersistentDataType.STRING, blockConfig.getTag());
            }

            generatorAccess.getChunk(blockPosition).removeBlockEntity(blockPosition);
            generatorAccess.getChunk(blockPosition).setBlockEntity(tileEntity);
        }
    }

}
