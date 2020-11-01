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

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.BooleanGuiSettings;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.CustomDataGuiSettings;
import de.derfrzocker.custom.ore.generator.factory.listeners.CommandListener;
import de.derfrzocker.spigot.utils.gui.BasicGui;
import de.derfrzocker.spigot.utils.gui.VerifyGui;
import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomDataGui extends BasicGui {

    private final static MessageValue NOT_SET_MESSAGE_VALUE = new MessageValue("value", "<not set>");
    private static CustomDataGuiSettings customDataGuiSettings;
    private static BooleanGuiSettings booleanGuiSettings;

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final OreConfigFactory oreConfigFactory;
    @NotNull
    private final Consumer<OreConfigFactory> consumer;
    @NotNull
    private final CustomData customData;

    public CustomDataGui(@NotNull final Plugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer, @NotNull final CustomData customData) {
        super(plugin, checkSettings(plugin));

        this.serviceSupplier = serviceSupplier;
        this.oreConfigFactory = oreConfigFactory;
        this.consumer = consumer;
        this.customData = customData;

        addDecorations();

        final MessageValue currentValue;
        if (oreConfigFactory.getOreConfigBuilder().customDatas().containsKey(customData)) {
            addItem(customDataGuiSettings.getRemoveCustomDataSlot(), MessageUtil.replaceItemStack(plugin, customDataGuiSettings.getRemoveCustomDataItemStack()), inventoryClickEvent -> {
                oreConfigFactory.getOreConfigBuilder().removeCustomData(customData);
                new CustomDatasGui(plugin, serviceSupplier, oreConfigFactory, consumer).openSync(inventoryClickEvent.getWhoClicked());
            });

            currentValue = new MessageValue("value", oreConfigFactory.getOreConfigBuilder().getCustomData(customData));
        } else {
            currentValue = NOT_SET_MESSAGE_VALUE;
        }

        addItem(customDataGuiSettings.getSetValueSlot(), MessageUtil.replaceItemStack(plugin, customDataGuiSettings.getSetValueItemStack()),
                inventoryClickEvent -> {
                    switch (customData.getCustomDataType()) {
                        case STRING:
                        case INTEGER:
                        case DOUBLE:
                            getChatInput((Player) inventoryClickEvent.getWhoClicked());
                            break;
                        case BOOLEAN: {
                            new VerifyGui(plugin,
                                    trueClickEvent -> {
                                        oreConfigFactory.getOreConfigBuilder().setCustomData(customData, true);
                                        new CustomDatasGui(plugin, serviceSupplier, oreConfigFactory, consumer).openSync(inventoryClickEvent.getWhoClicked());
                                    },
                                    falseClickEvent -> {
                                        oreConfigFactory.getOreConfigBuilder().setCustomData(customData, false);
                                        new CustomDatasGui(plugin, serviceSupplier, oreConfigFactory, consumer).openSync(inventoryClickEvent.getWhoClicked());
                                    },
                                    booleanGuiSettings).openSync(inventoryClickEvent.getWhoClicked());
                            break;
                        }
                    }
                });

        addItem(customDataGuiSettings.getFoundValueSlot(), MessageUtil.replaceItemStack(plugin, customDataGuiSettings.getFoundValueItemStack(), new MessageValue("value", oreConfigFactory.getOreConfigBuilder().getFoundCustomData(customData))),
                inventoryClickEvent -> {
                    oreConfigFactory.getOreConfigBuilder().setCustomData(customData, oreConfigFactory.getOreConfigBuilder().getFoundCustomData(customData));
                    new CustomDatasGui(plugin, serviceSupplier, oreConfigFactory, consumer).openSync(inventoryClickEvent.getWhoClicked());
                });
        addItem(customDataGuiSettings.getCurrentValueSlot(), MessageUtil.replaceItemStack(plugin, customDataGuiSettings.getCurrentValueItemStack(), currentValue));
        addItem(customDataGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, customDataGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(customDataGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, customDataGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        addItem(customDataGuiSettings.getBackSlot(), MessageUtil.replaceItemStack(plugin, customDataGuiSettings.getBackItemStack()), inventoryClickEvent -> {
            new CustomDatasGui(plugin, serviceSupplier, oreConfigFactory, consumer).openSync(inventoryClickEvent.getWhoClicked());
        });
    }

    private static CustomDataGuiSettings checkSettings(@NotNull final Plugin plugin) {
        if (customDataGuiSettings == null)
            customDataGuiSettings = new CustomDataGuiSettings(plugin, "data/factory/gui/custom-data-gui.yml", true);

        if (booleanGuiSettings == null)
            booleanGuiSettings = new BooleanGuiSettings(plugin, "data/factory/gui/boolean-gui.yml", true);

        return customDataGuiSettings;
    }

    private void getChatInput(@NotNull final Player player) {
        final OreConfigBuilder oreConfigBuilder = oreConfigFactory.getOreConfigBuilder();
        final OreConfig oreConfig = serviceSupplier.get().createOreConfig(oreConfigBuilder.name(), oreConfigBuilder.material(), oreConfigBuilder.oreGenerator(), oreConfigBuilder.blockSelector());
        oreConfigBuilder.replaceMaterial().forEach(oreConfig::addReplaceMaterial);
        oreConfigBuilder.selectMaterial().forEach(oreConfig::addSelectMaterial);
        oreConfigBuilder.biomes().forEach(oreConfig::addBiome);

        if (oreConfigBuilder.biomes().size() != 0) {
            oreConfig.setGeneratedAll(false);
        }

        oreConfigBuilder.getOreGeneratorOreSettings().getValues().forEach((oreSetting, aDouble) -> oreConfig.getOreGeneratorOreSettings().setValue(oreSetting, aDouble));
        oreConfigBuilder.getBlockSelectorOreSettings().getValues().forEach((oreSetting, aDouble) -> oreConfig.getBlockSelectorOreSettings().setValue(oreSetting, aDouble));
        oreConfigBuilder.customDatas().forEach(oreConfig::setCustomData);

        closeSync(player);
        final Conversation conversation = new ConversationFactory(getPlugin())
                .withModality(false)
                .withLocalEcho(false)
                .withFirstPrompt(new ValidatingPrompt() {
                    @Override
                    protected boolean isInputValid(@NotNull final ConversationContext conversationContext, @NotNull final String text) {
                        switch (customData.getCustomDataType()) {
                            case STRING: {
                                return customData.isValidCustomData(text, oreConfig);
                            }
                            case INTEGER: {
                                final int number;
                                try {
                                    number = Integer.parseInt(text);
                                } catch (final NumberFormatException e) {
                                    return false;
                                }

                                return customData.isValidCustomData(number, oreConfig);
                            }
                            case DOUBLE: {
                                final double number;
                                try {
                                    number = Double.parseDouble(text);
                                } catch (final NumberFormatException e) {
                                    return false;
                                }

                                return customData.isValidCustomData(number, oreConfig);
                            }
                            case BOOLEAN: {
                                // We use this method instead of Boolean.parseBoolean(String), since it return false if the given string is not 'true'
                                // and not wen the string 'false'. But we need tree states, true, false and wrong input
                                if ("TRUE".equalsIgnoreCase(text))
                                    return customData.isValidCustomData(true, oreConfig);

                                if ("FALSE".equalsIgnoreCase(text))
                                    return customData.isValidCustomData(false, oreConfig);

                                return false;
                            }
                        }

                        return false;
                    }

                    @Override
                    protected @Nullable
                    Prompt acceptValidatedInput(
                            @NotNull final ConversationContext conversationContext, @NotNull final String value) {

                        final Object parsedValue;

                        switch (customData.getCustomDataType()) {
                            case STRING: {
                                parsedValue = value;
                                break;
                            }
                            case INTEGER: {
                                parsedValue = Integer.parseInt(value);
                                break;
                            }
                            case DOUBLE: {
                                parsedValue = Double.parseDouble(value);
                                break;
                            }
                            case BOOLEAN: {
                                parsedValue = Boolean.parseBoolean(value);
                                break;
                            }
                            default:
                                throw new RuntimeException("Default case reached");
                        }

                        oreConfigBuilder.setCustomData(customData, parsedValue);
                        new CustomDatasGui(getPlugin(), serviceSupplier, oreConfigFactory, consumer).openSync(player);

                        return END_OF_CONVERSATION;
                    }

                    @Override
                    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
                        return MessageUtil.replacePlaceHolder(getPlugin(), new MessageKey(getPlugin(), "ore-config.factory.gui.custom-data.set-value." + customData.getCustomDataType() + ".not-valid").getMessage(), new MessageValue("value", invalidInput));
                    }

                    @Override
                    public @NotNull
                    String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(getPlugin(), "ore-config.factory.gui.custom-data.set-value." + customData.getCustomDataType() + ".begin").getMessage();
                    }
                }).buildConversation(player);

        new CommandListener(getPlugin(), player, conversation);

        player.beginConversation(conversation);
    }


}
