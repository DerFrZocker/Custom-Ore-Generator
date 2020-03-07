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
 */

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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class WorldGui extends PageGui<World> {

    private static WorldGuiSettings worldGuiSettings;

    @NotNull
    private final OreConfigFactory oreConfigFactory;

    public WorldGui(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer) {
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

    private static WorldGuiSettings checkSettings(@NotNull final JavaPlugin javaPlugin) {
        if (worldGuiSettings == null) {
            worldGuiSettings = new WorldGuiSettings(javaPlugin, "data/factory/gui/world-gui.yml", true);
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
