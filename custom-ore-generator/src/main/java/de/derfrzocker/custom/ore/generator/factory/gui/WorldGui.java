package de.derfrzocker.custom.ore.generator.factory.gui;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.WorldGuiSettings;
import de.derfrzocker.spigot.utils.gui.PageGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class WorldGui extends PageGui<World> {

    private static WorldGuiSettings worldGuiSettings;

    @NotNull
    private final OreConfigFactory oreConfigFactory;

    public WorldGui(@NotNull final Plugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer) {
        super(plugin, checkSettings(plugin));

        Validate.notNull(oreConfigFactory, "OreConfigFactory can not be null");

        this.oreConfigFactory = oreConfigFactory;

        addDecorations();
        init(Bukkit.getWorlds().toArray(new World[0]), World[]::new, this::getItemStack, this::handleNormalClick);

        addItem(worldGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, worldGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(worldGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, worldGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        addItem(worldGuiSettings.getNextSlot(), MessageUtil.replaceItemStack(plugin, worldGuiSettings.getNextItemStack()), inventoryClickEvent -> consumer.accept(oreConfigFactory));
    }

    private static WorldGuiSettings checkSettings(@NotNull final Plugin plugin) {
        if (worldGuiSettings == null) {
            worldGuiSettings = new WorldGuiSettings(plugin, "data/factory/gui/world-gui.yml", true);
        }

        return worldGuiSettings;
    }

    private ItemStack getItemStack(@NotNull final World world) {
        final ItemStack itemStack;
        if (oreConfigFactory.getOreConfigBuilder().containsWorld(world)) {
            itemStack = worldGuiSettings.getActivatedItemStack();
        } else {
            itemStack = worldGuiSettings.getDeactivatedItemStack();
        }

        return MessageUtil.replaceItemStack(getPlugin(), itemStack, new MessageValue("name", world.getName()));
    }

    private void handleNormalClick(@NotNull final World world, @NotNull final InventoryClickEvent event) {
        final OreConfigBuilder oreConfigBuilder = oreConfigFactory.getOreConfigBuilder();

        if (oreConfigBuilder.containsWorld(world)) {
            oreConfigBuilder.removeWorld(world);
        } else {
            oreConfigBuilder.addWorld(world);
        }

        updateItemStack(world, getItemStack(world));
    }

}
