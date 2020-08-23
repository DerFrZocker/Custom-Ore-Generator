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
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.CustomDatasGuiSettings;
import de.derfrzocker.spigot.utils.gui.PageGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomDatasGui extends PageGui<CustomData> {

    private static CustomDatasGuiSettings customDatasGuiSettings;

    public CustomDatasGui(@NotNull final Plugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer) {
        super(plugin, checkSettings(plugin));

        final Map<CustomData, Object> customDatas = oreConfigFactory.getOreConfigBuilder().customDatas();
        final Map<CustomData, Object> foundCustomDatas = oreConfigFactory.getOreConfigBuilder().foundCustomDatas();
        final Set<CustomData> allCustomDatas = new LinkedHashSet<>(customDatas.keySet());

        allCustomDatas.addAll(foundCustomDatas.keySet());

        addDecorations();
        init(allCustomDatas.toArray(new CustomData[0]), CustomData[]::new, customData -> {
                    final MessageValue[] messageValues = new MessageValue[]{
                            new MessageValue("description", customData.getInfo().getDescription()),
                            new MessageValue("name", customData.getName()),
                            new MessageValue("display-name", customData.getInfo().getDisplayName())
                    };
                    final ItemStack itemStack;

                    if (customDatas.containsKey(customData)) {
                        itemStack = customDatasGuiSettings.getActivatedCustomDataItemStack();
                    } else {
                        itemStack = customDatasGuiSettings.getDeactivatedCustomDataItemStack();
                    }

                    itemStack.setType(customData.getInfo().getMaterial());
                    return MessageUtil.replaceItemStack(getPlugin(), itemStack, messageValues);
                }, (customData, inventoryClickEvent) -> {
                    new CustomDataGui(plugin, serviceSupplier, oreConfigFactory, consumer, customData).openSync(inventoryClickEvent.getWhoClicked());
                }
        );

        addItem(customDatasGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, customDatasGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(customDatasGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, customDatasGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        addItem(customDatasGuiSettings.getNextSlot(), MessageUtil.replaceItemStack(plugin, customDatasGuiSettings.getNextItemStack()), inventoryClickEvent -> consumer.accept(oreConfigFactory));

    }

    private static CustomDatasGuiSettings checkSettings(@NotNull final Plugin plugin) {
        if (customDatasGuiSettings == null)
            customDatasGuiSettings = new CustomDatasGuiSettings(plugin, "data/factory/gui/custom-datas-gui.yml", true);

        return customDatasGuiSettings;
    }

}
