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

package de.derfrzocker.custom.ore.generator.factory.gui;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.api.OreSettingContainer;
import de.derfrzocker.custom.ore.generator.api.OreSettingsAble;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.OreSettingGuiSettings;
import de.derfrzocker.spigot.utils.gui.BasicGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OreSettingGui extends BasicGui {

    private static OreSettingGuiSettings oreSettingGuiSettings;

    private final OreSetting oreSetting;
    private final OreConfigBuilder oreConfigBuilder;
    private final OreSettingsAble oreSettingsAble;
    private final OreSettingContainer oreSettingContainer;
    private final int oreSettingSlot;

    public OreSettingGui(@NotNull final JavaPlugin plugin,
                         @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier,
                         @NotNull final OreConfigFactory oreConfigFactory,
                         @NotNull final Consumer<OreConfigFactory> consumer,
                         @NotNull final OreSetting oreSetting,
                         @NotNull final OreSettingsAble oreSettingsAble,
                         @NotNull final OreSettingContainer oreSettingContainer) {
        super(plugin, checkSettings(plugin));

        this.oreSetting = oreSetting;
        this.oreConfigBuilder = oreConfigFactory.getOreConfigBuilder();
        this.oreSettingsAble = oreSettingsAble;
        this.oreSettingContainer = oreSettingContainer;
        this.oreSettingSlot = oreSettingGuiSettings.getOreSettingSlot();

        addDecorations();
        addItem(oreSettingGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, oreSettingGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(oreSettingGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, oreSettingGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        addItem(oreSettingGuiSettings.getBackSlot(), MessageUtil.replaceItemStack(plugin, oreSettingGuiSettings.getBackItemStack()), inventoryClickEvent -> {
            new OreSettingsGui(plugin, serviceSupplier, oreConfigFactory, consumer, oreSettingsAble, oreSettingContainer).openSync(inventoryClickEvent.getWhoClicked());
        });

        final Double tempCurrent = oreSettingContainer.getValue(oreSetting).orElse(0d);
        updateItemStack(tempCurrent);
        oreSettingGuiSettings.getItemStackValues().forEach(value -> addItem(value.getSlot(), value.getItemStack(), new SettingConsumer(value.getValue())));
    }

    private static OreSettingGuiSettings checkSettings(@NotNull final JavaPlugin javaPlugin) {
        if (oreSettingGuiSettings == null)
            oreSettingGuiSettings = new OreSettingGuiSettings(javaPlugin, "data/factory/gui/ore-setting-gui.yml", true);

        return oreSettingGuiSettings;
    }

    private void updateItemStack(final double value) {
        final ItemStack itemStack = oreSettingGuiSettings.getOreSettingItemStack();

        itemStack.setType(oreSettingsAble.getOreSettingInfo(oreSetting).getMaterial());

        addItem(oreSettingSlot, MessageUtil.replaceItemStack(getPlugin(), itemStack, new MessageValue("value", value), new MessageValue("name", oreSettingsAble.getOreSettingInfo(oreSetting).getDisplayName())));
    }

    private final class SettingConsumer implements Consumer<InventoryClickEvent> {

        private final double value;

        private SettingConsumer(final double value) {
            this.value = value;
        }

        @Override
        public void accept(@NotNull final InventoryClickEvent event) {
            final double current = oreSettingContainer.getValue(oreSetting).orElse(0d);
            final double newValue = Double.parseDouble(String.format(Locale.ENGLISH, "%1.2f", current + value));

            oreSettingContainer.setValue(oreSetting, newValue);
            updateItemStack(newValue);
            //TODO add save check
        }

    }

}
