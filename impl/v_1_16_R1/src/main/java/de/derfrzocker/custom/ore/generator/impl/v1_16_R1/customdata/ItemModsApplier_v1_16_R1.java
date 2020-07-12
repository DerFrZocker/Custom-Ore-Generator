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

package de.derfrzocker.custom.ore.generator.impl.v1_16_R1.customdata;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import net.minecraft.server.v1_16_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_16_R1.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemModsApplier_v1_16_R1 implements CustomDataApplier {

    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    @NotNull
    private final CustomData customData;

    public ItemModsApplier_v1_16_R1(@NotNull final CustomData data) {
        Validate.notNull(data, "CustomData can not be null");

        customData = data;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object location, @NotNull final Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final GeneratorAccess generatorAccess = (GeneratorAccess) blockAccess;

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
            final EntityArmorStand entityArmorStand = new EntityArmorStand(generatorAccess.getMinecraftWorld(), blockPosition.getX() + 0.5, blockPosition.getY(), blockPosition.getZ() + 0.5);

            entityArmorStand.setSmall(armorStandBlockConfig.isSmall());
            entityArmorStand.setMarker(armorStandBlockConfig.isMarker());
            entityArmorStand.setInvulnerable(armorStandBlockConfig.isInvulnerable());
            entityArmorStand.setCustomNameVisible(armorStandBlockConfig.isCustomNameVisible());
            entityArmorStand.setCustomName(CraftChatMessage.fromStringOrNull(armorStandBlockConfig.getCustomName()));
            entityArmorStand.setInvisible(armorStandBlockConfig.isInvisible());
            entityArmorStand.getScoreboardTags().add(blockConfig.getTag());
            entityArmorStand.setNoGravity(true);
            entityArmorStand.setSilent(true);
            entityArmorStand.setBasePlate(armorStandBlockConfig.isBasePlate());
            entityArmorStand.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(armorStandBlockConfig.getHelmet()));
            entityArmorStand.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(armorStandBlockConfig.getChestplate()));
            entityArmorStand.setSlot(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(armorStandBlockConfig.getChestplate()));
            entityArmorStand.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(armorStandBlockConfig.getLeggings()));
            entityArmorStand.setSlot(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(armorStandBlockConfig.getBoots()));
            entityArmorStand.setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(armorStandBlockConfig.getMainHand()));
            entityArmorStand.setSlot(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(armorStandBlockConfig.getOffHand()));

            // Fixing ArmorStand rotating issue, I have now idea why the yaw and/or pitch is another value than 0.
            // That needs a more detailed investigation, which of the above methods changes the yaw and/or pitch,
            // but for now it works.
            entityArmorStand.yaw = 0;
            entityArmorStand.pitch = 0;
            generatorAccess.addEntity(entityArmorStand);
        }

        final TileEntity tileEntity = generatorAccess.getTileEntity(blockPosition);

        if (tileEntity != null) {
            if (blockConfig.getData() != null) {
                final NBTTagCompound nbtTagCompound = new NBTTagCompound();
                tileEntity.save(nbtTagCompound);

                try {
                    final NBTTagCompound nbtTagCompound1 = MojangsonParser.parse(blockConfig.getData());

                    nbtTagCompound.a(nbtTagCompound1);
                } catch (final CommandSyntaxException e) {
                    throw new RuntimeException("Error while parsing String to NBTTagCompound", e);
                }

                tileEntity.load(generatorAccess.getType(blockPosition), nbtTagCompound);
            }

            if (blockConfig.getTag() != null) {
                if (tileEntity.persistentDataContainer == null) {
                    tileEntity.persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);
                }

                tileEntity.persistentDataContainer.set(new NamespacedKey(ItemMods.getPlugin(), "type"), PersistentDataType.STRING, blockConfig.getTag());
            }

            generatorAccess.z(blockPosition).removeTileEntity(blockPosition);
            generatorAccess.z(blockPosition).setTileEntity(blockPosition, tileEntity);
        }
    }

}
