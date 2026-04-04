package de.derfrzocker.custom.ore.generator.impl.v1_16_R2.customdata;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.EntityArmorStand;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.EnumItemSlot;
import net.minecraft.server.v1_16_R2.GeneratorAccess;
import net.minecraft.server.v1_16_R2.IRegistry;
import net.minecraft.server.v1_16_R2.Item;
import net.minecraft.server.v1_16_R2.ItemStack;
import net.minecraft.server.v1_16_R2.MinecraftKey;
import net.minecraft.server.v1_16_R2.MobSpawnerAbstract;
import net.minecraft.server.v1_16_R2.MobSpawnerData;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.TileEntity;
import net.minecraft.server.v1_16_R2.TileEntityMobSpawner;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_16_R2.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemModsApplier_v1_16_R2 implements CustomDataApplier {

    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    @NotNull
    private final CustomData customData;

    public ItemModsApplier_v1_16_R2(@NotNull final CustomData data) {
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

            generatorAccess.z(blockPosition).removeTileEntity(blockPosition);
            generatorAccess.z(blockPosition).setTileEntity(blockPosition, tileEntity);
        }
    }

}
