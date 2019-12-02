package de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata;

import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.itemmods.main.Main;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.EntityArmorStand;
import net.minecraft.server.v1_13_R1.EnumItemSlot;
import net.minecraft.server.v1_13_R1.GeneratorAccess;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftChatMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemModsApplier_v1_13_R1 implements CustomDataApplier {

    @NotNull
    private final CustomData customData;

    public ItemModsApplier_v1_13_R1(@NotNull final CustomData data) {
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
        final Optional<BlockConfig> blockConfigOptional = Main.getPlugin().getCustomBlockManager().getBlockConfigs().stream().filter(blockConfig -> blockConfig.getName().equals(name)).findAny();

        if (!blockConfigOptional.isPresent())
            return; //TODO maybe throw exception?

        final BlockConfig blockConfig = blockConfigOptional.get();
        final EntityArmorStand entityArmorStand = new EntityArmorStand(generatorAccess.getMinecraftWorld(), blockPosition.getX() + 0.5, blockPosition.getY(), blockPosition.getZ() + 0.5);

        entityArmorStand.setSmall(blockConfig.isSmall());
        entityArmorStand.setMarker(blockConfig.isMarker());
        entityArmorStand.setInvulnerable(blockConfig.isInvulnerable());
        entityArmorStand.setCustomNameVisible(blockConfig.isCustomNameVisible());
        entityArmorStand.setCustomName(CraftChatMessage.fromStringOrNull(blockConfig.getCustomName()));
        entityArmorStand.setInvisible(blockConfig.isInvisible());
        entityArmorStand.getScoreboardTags().add(blockConfig.getTag());
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setBasePlate(blockConfig.isBasePlate());
        entityArmorStand.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(blockConfig.getHelmet()));
        entityArmorStand.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(blockConfig.getChestplate()));
        entityArmorStand.setSlot(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(blockConfig.getChestplate()));
        entityArmorStand.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(blockConfig.getLeggings()));
        entityArmorStand.setSlot(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(blockConfig.getBoots()));
        entityArmorStand.setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(blockConfig.getMainHand()));
        entityArmorStand.setSlot(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(blockConfig.getOffHand()));

        generatorAccess.addEntity(entityArmorStand);
    }

}
