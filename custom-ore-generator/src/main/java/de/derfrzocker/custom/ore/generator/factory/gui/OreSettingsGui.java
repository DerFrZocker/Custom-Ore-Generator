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

package de.derfrzocker.custom.ore.generator.factory.gui;

import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.OreSettingsGuiSettings;
import de.derfrzocker.spigot.utils.gui.PageGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OreSettingsGui extends PageGui<OreSetting> {

    private static OreSettingsGuiSettings oreSettingsGuiSettings;

    @NotNull
    private final OreSettingsAble oreSettingsAble;

    public OreSettingsGui(@NotNull final Plugin plugin,
                          @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier,
                          @NotNull final OreConfigFactory oreConfigFactory,
                          @NotNull final Consumer<OreConfigFactory> consumer,
                          @NotNull final OreSettingsAble oreSettingsAble,
                          @NotNull final OreSettingContainer oreSettingContainer) {
        super(plugin, checkSettings(plugin));

        this.oreSettingsAble = oreSettingsAble;

        final OreConfigBuilder oreConfigBuilder = oreConfigFactory.getOreConfigBuilder();
        final OreGenerator oreGenerator = oreConfigBuilder.oreGenerator();
        final BlockSelector blockSelector = oreConfigBuilder.blockSelector();
        final Set<OreSetting> settings = oreSettingsAble.getNeededOreSettings();

        if (settings.isEmpty())
            throw new IllegalStateException("Cant create OreSettingsGui with out settings");

        addDecorations();
        init(settings.toArray(new OreSetting[0]), OreSetting[]::new, this::getItemStack, (oreSetting, inventoryClickEvent) ->
                new OreSettingGui(getPlugin(), serviceSupplier, oreConfigFactory, consumer, oreSetting, oreSettingsAble, oreSettingContainer).openSync(inventoryClickEvent.getWhoClicked())
        );

        addItem(oreSettingsGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, oreSettingsGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(oreSettingsGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, oreSettingsGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        addItem(oreSettingsGuiSettings.getNextSlot(), MessageUtil.replaceItemStack(plugin, oreSettingsGuiSettings.getNextItemStack()), inventoryClickEvent -> {
            consumer.accept(oreConfigFactory);
        });

    }

    private static OreSettingsGuiSettings checkSettings(@NotNull final Plugin plugin) {
        if (oreSettingsGuiSettings == null)
            oreSettingsGuiSettings = new OreSettingsGuiSettings(plugin, "data/factory/gui/ore-settings-gui.yml", true);

        return oreSettingsGuiSettings;
    }

    private ItemStack getItemStack(@NotNull final OreSetting oreSetting) {
        final ItemStack itemStack = oreSettingsGuiSettings.getOreSettingItemStack();

        itemStack.setType(this.oreSettingsAble.getOreSettingInfo(oreSetting).getMaterial());

        return MessageUtil.replaceItemStack(getPlugin(), itemStack, new MessageValue("name", oreSettingsAble.getOreSettingInfo(oreSetting).getDisplayName()));
    }

}
