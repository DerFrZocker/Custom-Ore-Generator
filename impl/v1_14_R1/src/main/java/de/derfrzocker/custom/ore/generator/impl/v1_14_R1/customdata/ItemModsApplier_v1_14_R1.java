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

package de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumItemSlot;
import net.minecraft.server.v1_14_R1.GeneratorAccess;
import net.minecraft.server.v1_14_R1.IRegistry;
import net.minecraft.server.v1_14_R1.Item;
import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import net.minecraft.server.v1_14_R1.MobSpawnerAbstract;
import net.minecraft.server.v1_14_R1.MobSpawnerData;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.TileEntity;
import net.minecraft.server.v1_14_R1.TileEntityMobSpawner;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_14_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_14_R1.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemModsApplier_v1_14_R1 implements CustomDataApplier {

    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    @NotNull
    private final CustomData customData;

    public ItemModsApplier_v1_14_R1(@NotNull final CustomData data) {
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
        PackObject packObject = new PackObject(name);
        BlockAsset blockAsset = packObject.getBlock();

        if (blockAsset == null) {
            return; //TODO maybe throw exception?
        }

        ModelAsset modelAsset = packObject.getModel();

        if (modelAsset == null) {
            return; //TODO maybe throw exception?
        }

        final TileEntity tileEntity = generatorAccess.getTileEntity(blockPosition);

        if (tileEntity != null) {
            TileEntityMobSpawner spawnerBlock = (TileEntityMobSpawner) tileEntity;
            MobSpawnerAbstract spawner = spawnerBlock.getSpawner();
            spawner.requiredPlayerRange = 0;
            spawner.spawnCount = 0;

            EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, null);
            Item item = IRegistry.ITEM.get(new MinecraftKey(modelAsset.getFallbackTexture().getKey().toString()));
            ItemStack itemStack = new ItemStack(item);
            NBTTagCompound compoundTag = itemStack.getOrCreateTag();
            compoundTag.setInt("CustomModelData", packObject.getCustomModel());
            armorStand.setSilent(true);
            armorStand.setSlot(EnumItemSlot.MAINHAND, itemStack);

            NBTTagCompound saved = new NBTTagCompound();
            armorStand.save(saved);
            spawner.spawnData = new MobSpawnerData(1, saved);
            if (tileEntity.persistentDataContainer == null) {
                tileEntity.persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);
            }

            tileEntity.persistentDataContainer.set(new NamespacedKey(ItemMods.getPlugin(), "custom_block_type"), PersistentDataType.STRING, packObject.toString());

            generatorAccess.w(blockPosition).removeTileEntity(blockPosition);
            generatorAccess.w(blockPosition).setTileEntity(blockPosition, tileEntity);
        }
    }

}
