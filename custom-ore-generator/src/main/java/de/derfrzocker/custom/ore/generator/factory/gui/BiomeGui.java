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
import de.derfrzocker.custom.ore.generator.factory.gui.settings.BiomeGuiSettings;
import de.derfrzocker.spigot.utils.gui.PageGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Biome;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BiomeGui extends PageGui<Biome> {

    private static BiomeGuiSettings biomeGuiSettings;

    @NotNull
    private final OreConfigFactory oreConfigFactory;

    public BiomeGui(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer) {
        super(plugin, checkSettings(plugin));

        Validate.notNull(oreConfigFactory, "OreConfigFactory can not be null");

        this.oreConfigFactory = oreConfigFactory;

        addDecorations();
        init(Biome.values(), Biome[]::new, this::getItemStack, this::handleNormalClick);

        addItem(biomeGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, biomeGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(biomeGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, biomeGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        addItem(biomeGuiSettings.getNextSlot(), MessageUtil.replaceItemStack(plugin, biomeGuiSettings.getNextItemStack()), inventoryClickEvent -> consumer.accept(oreConfigFactory));
    }

    private static BiomeGuiSettings checkSettings(@NotNull final JavaPlugin javaPlugin) {
        if (biomeGuiSettings == null) {
            biomeGuiSettings = new BiomeGuiSettings(javaPlugin, "data/factory/gui/biome-gui.yml", true);
        }

        return biomeGuiSettings;
    }

    private ItemStack getItemStack(@NotNull final Biome biome) {
        final ItemStack itemStack;
        if (oreConfigFactory.getOreConfigBuilder().containsBiome(biome)) {
            itemStack = biomeGuiSettings.getActivatedItemStack();
        } else {
            itemStack = biomeGuiSettings.getDeactivatedItemStack();
        }

        itemStack.setType(biomeGuiSettings.getBiomeMaterial(biome.toString()));

        return MessageUtil.replaceItemStack(getPlugin(), itemStack,
                new MessageValue("name", biome)
        );
    }

    private void handleNormalClick(@NotNull final Biome biome, @NotNull final InventoryClickEvent event) {
        final OreConfigBuilder oreConfigBuilder = oreConfigFactory.getOreConfigBuilder();

        if(oreConfigBuilder.containsBiome(biome)){
            oreConfigBuilder.removeBiome(biome);
        }else{
            oreConfigBuilder.addBiome(biome);
        }

        updateItemStack(biome, getItemStack(biome));
    }

}
