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

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.BlockSelectorGuiSettings;
import de.derfrzocker.spigot.utils.gui.PageGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockSelectorGui extends PageGui<BlockSelector> {

    private static BlockSelectorGuiSettings blockSelectorGuiSettings;

    public BlockSelectorGui(@NotNull final Plugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer) {
        super(plugin, checkSettings(plugin));


        final CustomOreGeneratorService service = serviceSupplier.get();
        final BlockSelector defaultBlockSelector = service.getDefaultBlockSelector();
        final BlockSelector[] blockSelectors = service.getBlockSelectors().toArray(new BlockSelector[0]);

        addDecorations();
        init(blockSelectors, BlockSelector[]::new, this::getItemStack, (blockSelector, inventoryClickEvent) -> {
            oreConfigFactory.getOreConfigBuilder().blockSelector(blockSelector);
            consumer.accept(oreConfigFactory);
        });

        addItem(blockSelectorGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, blockSelectorGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(blockSelectorGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, blockSelectorGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        if (defaultBlockSelector != null)
            addItem(blockSelectorGuiSettings.getDefaultBlockSelectorSlot(), MessageUtil.replaceItemStack(plugin, blockSelectorGuiSettings.getDefaultBlockSelectorItemStack(),
                    new MessageValue("description", defaultBlockSelector.getInfo().getDescription()),
                    new MessageValue("name", defaultBlockSelector.getName()),
                    new MessageValue("display-name", defaultBlockSelector.getInfo().getDisplayName())
            ), inventoryClickEvent -> {
                oreConfigFactory.getOreConfigBuilder().blockSelector(defaultBlockSelector);
                consumer.accept(oreConfigFactory);
            });

    }

    private static BlockSelectorGuiSettings checkSettings(@NotNull final Plugin plugin) {
        if (blockSelectorGuiSettings == null)
            blockSelectorGuiSettings = new BlockSelectorGuiSettings(plugin, "data/factory/gui/block-selector-gui.yml", true);

        return blockSelectorGuiSettings;
    }

    private ItemStack getItemStack(@NotNull final BlockSelector blockSelector) {
        final ItemStack itemStack = blockSelectorGuiSettings.getBlockSelectorItemStack();

        itemStack.setType(blockSelector.getInfo().getMaterial());

        return MessageUtil.replaceItemStack(getPlugin(), itemStack,
                new MessageValue("description", blockSelector.getInfo().getDescription()),
                new MessageValue("name", blockSelector.getName()),
                new MessageValue("display-name", blockSelector.getInfo().getDisplayName())
        );
    }

}
