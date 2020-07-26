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

import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.MenuGuiSettings;
import de.derfrzocker.spigot.utils.gui.BasicGui;
import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class MenuGui extends BasicGui {

    private static MenuGuiSettings menuGuiSettings;
    private boolean ready = true;

    public MenuGui(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory) {
        super(plugin, checkSettings(plugin));

        addDecorations();

        final OreConfigBuilder oreConfigBuilder = oreConfigFactory.getOreConfigBuilder();

        {
            final String name = oreConfigBuilder.name();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "name"));

            if (name != null) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
                messageValues.add(new MessageValue("name", name));
            } else {
                ready = false;
                itemStack = menuGuiSettings.getStatusNeededItemStack();
            }

            addItem(menuGuiSettings.getStepNameSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> {
                        closeSync(inventoryClickEvent.getWhoClicked());
                        oreConfigFactory.setName(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer()));
                    });
        }

        {
            final Material material = oreConfigBuilder.material();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "material"));

            if (material != null) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
                messageValues.add(new MessageValue("material", material));
            } else {
                ready = false;
                itemStack = menuGuiSettings.getStatusNeededItemStack();
            }
            addItem(menuGuiSettings.getStepMaterialSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> {
                        closeSync(inventoryClickEvent.getWhoClicked());
                        oreConfigFactory.setMaterial(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer()));
                    });
        }

        {
            final Set<Material> replaceMaterial = oreConfigBuilder.replaceMaterial();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "replace-material"));

            if (!replaceMaterial.isEmpty()) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
            } else {
                ready = false;
                itemStack = menuGuiSettings.getStatusNeededItemStack();
            }
            addItem(menuGuiSettings.getStepReplaceMaterialsSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> {
                        closeSync(inventoryClickEvent.getWhoClicked());
                        oreConfigFactory.setReplaceMaterials(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer()));
                    });
        }

        {
            final Set<Material> selectMaterial = oreConfigBuilder.selectMaterial();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "select-material"));

            if (!selectMaterial.isEmpty()) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
            } else {
                itemStack = menuGuiSettings.getStatusNotPresentNotNeededItemStack();
            }
            addItem(menuGuiSettings.getStepSelectMaterialsSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> {
                        closeSync(inventoryClickEvent.getWhoClicked());
                        oreConfigFactory.setSelectMaterials(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer()));
                    });
        }

        {
            final OreGenerator oreGenerator = oreConfigBuilder.oreGenerator();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "ore-generator"));

            if (oreGenerator != null) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
                messageValues.add(new MessageValue("ore-generator", oreGenerator.getName()));
            } else {
                ready = false;
                itemStack = menuGuiSettings.getStatusNeededItemStack();
            }
            addItem(menuGuiSettings.getStepOreGeneratorSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> oreConfigFactory.setOreGenerator(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer())));
        }

        {
            final BlockSelector blockSelector = oreConfigBuilder.blockSelector();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "block-selector"));

            if (blockSelector != null) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
                messageValues.add(new MessageValue("block-selector", blockSelector.getName()));
            } else {
                ready = false;
                itemStack = menuGuiSettings.getStatusNeededItemStack();
            }
            addItem(menuGuiSettings.getStepBlockSelectorSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> oreConfigFactory.setBlockSelector(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer())));
        }

        {
            final Set<Biome> biomes = oreConfigBuilder.biomes();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "biome"));

            if (!biomes.isEmpty()) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
            } else {
                itemStack = menuGuiSettings.getStatusNotPresentNotNeededItemStack();
            }
            addItem(menuGuiSettings.getStepBiomeSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> oreConfigFactory.setBiomes(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer())));
        }

        {//Ore Generator ore settings
            final Map<OreSetting, Double> oreSettings = oreConfigBuilder.getOreGeneratorOreSettings().getValues();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;
            boolean setAction = true;

            messageValues.add(new MessageValue("step", "ore-generator-ore-setting"));

            final OreGenerator oreGenerator = oreConfigBuilder.oreGenerator();

            if (oreGenerator == null) {
                itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                setAction = false;
            } else if (!oreGenerator.getNeededOreSettings().isEmpty()) {
                if (oreSettings.isEmpty()) {
                    itemStack = menuGuiSettings.getStatusNotPresentNotNeededItemStack();
                } else {
                    itemStack = menuGuiSettings.getStatusPresentItemStack();
                }
            } else {
                itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                setAction = false;
            }

            if (setAction) {
                addItem(menuGuiSettings.getStepOreGeneratorOreSettingsSlot(),
                        MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                        inventoryClickEvent -> oreConfigFactory.setOreGeneratorOreSettings(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer())));
            } else {
                addItem(menuGuiSettings.getStepOreGeneratorOreSettingsSlot(),
                        MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])));
            }
        }

        {//Block Selector ore settings
            final Map<OreSetting, Double> oreSettings = oreConfigBuilder.getBlockSelectorOreSettings().getValues();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;
            boolean setAction = true;

            messageValues.add(new MessageValue("step", "block-selector-ore-setting"));

            final BlockSelector blockSelector = oreConfigBuilder.blockSelector();

            if (blockSelector == null) {
                itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                setAction = false;
            } else if (!blockSelector.getNeededOreSettings().isEmpty()) {
                if (oreSettings.isEmpty()) {
                    itemStack = menuGuiSettings.getStatusNotPresentNotNeededItemStack();
                } else {
                    itemStack = menuGuiSettings.getStatusPresentItemStack();
                }
            } else {
                itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                setAction = false;
            }

            if (setAction) {
                addItem(menuGuiSettings.getStepBlockSelectorOreSettingsSlot(),
                        MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                        inventoryClickEvent -> oreConfigFactory.setBlockSelectorOreSettings(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer())));
            } else {
                addItem(menuGuiSettings.getStepBlockSelectorOreSettingsSlot(),
                        MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])));
            }
        }

        {
            final Map<CustomData, Object> customDatas = oreConfigBuilder.customDatas();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;
            boolean customDataReady = true;

            messageValues.add(new MessageValue("step", "custom-data"));

            if (!customDatas.isEmpty()) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
            } else check:{
                if (oreConfigBuilder.foundCustomDatas().isEmpty()) {
                    itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                    customDataReady = false;
                    break check;
                }

                if (oreConfigBuilder.name() == null) {
                    itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                    customDataReady = false;
                    break check;
                }

                if (oreConfigBuilder.material() == null) {
                    itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                    customDataReady = false;
                    break check;
                }

                if (oreConfigBuilder.replaceMaterial().isEmpty()) {
                    itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                    customDataReady = false;
                    break check;
                }

                if (oreConfigBuilder.oreGenerator() == null) {
                    itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                    customDataReady = false;
                    break check;
                }

                if (oreConfigBuilder.blockSelector() == null) {
                    itemStack = menuGuiSettings.getStatusNotSetAbleItemStack();
                    customDataReady = false;
                    break check;
                }

                itemStack = menuGuiSettings.getStatusNotPresentNotNeededItemStack();
            }

            if (customDataReady) {
                addItem(menuGuiSettings.getStepCustomDatasSlot(), MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                        inventoryClickEvent -> oreConfigFactory.setCustomDatas(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer())));
            } else {
                addItem(menuGuiSettings.getStepCustomDatasSlot(), MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])));
            }
        }

        {
            final Set<World> worlds = oreConfigBuilder.worlds();
            final Set<MessageValue> messageValues = new LinkedHashSet<>();
            final ItemStack itemStack;

            messageValues.add(new MessageValue("step", "world"));

            if (!worlds.isEmpty()) {
                itemStack = menuGuiSettings.getStatusPresentItemStack();
            } else {
                itemStack = menuGuiSettings.getStatusNotPresentNotNeededItemStack();
            }
            addItem(menuGuiSettings.getStepWorldSlot(),
                    MessageUtil.replaceItemStack(plugin, itemStack, messageValues.toArray(new MessageValue[0])),
                    inventoryClickEvent -> oreConfigFactory.setWorlds(oreConfigFactory1 -> new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(oreConfigFactory1.getPlayer())));
        }

        {
            if (ready) {
                addItem(menuGuiSettings.getOreConfigItemStackSlot(),
                        MessageUtil.replaceItemStack(plugin, menuGuiSettings.getOreConfigReadyItemStack()),
                        inventoryClickEvent -> {
                            final CustomOreGeneratorService service = serviceSupplier.get();
                            final OreConfig oreConfig = service.createOreConfig(oreConfigBuilder.name(), oreConfigBuilder.material(), oreConfigBuilder.oreGenerator(), oreConfigBuilder.blockSelector());

                            oreConfigBuilder.replaceMaterial().forEach(oreConfig::addReplaceMaterial);
                            oreConfigBuilder.selectMaterial().forEach(oreConfig::addSelectMaterial);
                            oreConfigBuilder.biomes().forEach(oreConfig::addBiome);

                            if (oreConfigBuilder.biomes().size() != 0) {
                                oreConfig.setGeneratedAll(false);
                            }

                            oreConfigBuilder.getOreGeneratorOreSettings().getValues().forEach((oreSetting, aDouble) -> oreConfig.getOreGeneratorOreSettings().setValue(oreSetting, aDouble));
                            oreConfigBuilder.getBlockSelectorOreSettings().getValues().forEach((oreSetting, aDouble) -> oreConfig.getBlockSelectorOreSettings().setValue(oreSetting, aDouble));
                            oreConfigBuilder.customDatas().forEach(oreConfig::setCustomData);

                            service.saveOreConfig(oreConfig);

                            for (final World world : oreConfigBuilder.worlds()) {
                                final WorldConfig worldConfig = service.createWorldConfig(world);

                                worldConfig.addOreConfig(oreConfig);

                                service.saveWorldConfig(worldConfig);
                            }
                            closeSync(inventoryClickEvent.getWhoClicked());
                            oreConfigFactory.setRunning(false);
                            new MessageKey(getPlugin(), "ore-config.factory.success").sendMessage(inventoryClickEvent.getWhoClicked());
                        });
            } else {
                addItem(menuGuiSettings.getOreConfigItemStackSlot(),
                        MessageUtil.replaceItemStack(plugin, menuGuiSettings.getOreConfigNotReadyItemStack()));
            }
        }

        addItem(menuGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, menuGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
    }

    private static MenuGuiSettings checkSettings(@NotNull final JavaPlugin javaPlugin) {
        if (menuGuiSettings == null)
            menuGuiSettings = new MenuGuiSettings(javaPlugin, "data/factory/gui/menu-gui.yml", true);

        return menuGuiSettings;
    }

}
