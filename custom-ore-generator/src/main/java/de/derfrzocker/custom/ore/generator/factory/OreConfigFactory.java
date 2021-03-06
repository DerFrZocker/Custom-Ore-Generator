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

package de.derfrzocker.custom.ore.generator.factory;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.factory.gui.*;
import de.derfrzocker.custom.ore.generator.factory.listeners.CommandListener;
import de.derfrzocker.custom.ore.generator.factory.listeners.MainMaterialListener;
import de.derfrzocker.custom.ore.generator.factory.listeners.ReplaceMaterialListener;
import de.derfrzocker.custom.ore.generator.factory.listeners.SelectMaterialListener;
import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OreConfigFactory implements Listener {
    private final static Pattern ORE_CONFIG_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");

    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final Player player;
    @NotNull
    private final OreConfigBuilder oreConfigBuilder;
    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    private boolean running = false;

    public OreConfigFactory(@NotNull final JavaPlugin javaPlugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final Player player) {
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(serviceSupplier, "Service Supplier can not be null");
        Validate.notNull(player, "Player can not be null");

        this.javaPlugin = javaPlugin;
        this.serviceSupplier = serviceSupplier;
        this.player = player;
        this.oreConfigBuilder = OreConfigBuilder.newBuilder();
    }

    @NotNull
    public OreConfigBuilder getOreConfigBuilder() {
        return this.oreConfigBuilder;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public boolean setName(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        final Conversation conversation = new ConversationFactory(javaPlugin)
                .withModality(false)
                .withEscapeSequence("!exit")
                .withEscapeSequence("!menu")
                .withLocalEcho(false)
                .addConversationAbandonedListener(event -> {
                    running = false;
                    if (event.gracefulExit()) {
                        consumer.accept(OreConfigFactory.this);
                        return;
                    }

                    final ConversationCanceller canceller = event.getCanceller();

                    if (canceller instanceof ExactMatchConversationCanceller && canceller.cancelBasedOnInput(null, "!menu")) {
                        new MenuGui(javaPlugin, serviceSupplier, this).openSync(player);
                    }

                })
                .withFirstPrompt(new RegexPrompt(ORE_CONFIG_NAME_PATTERN) {

                    @Override
                    protected boolean isInputValid(@NotNull final ConversationContext context, @NotNull final String input) {
                        if (super.isInputValid(context, input)) {
                            final CustomOreGeneratorService service = serviceSupplier.get();

                            if (service.getOreConfig(input).isPresent()) {
                                new MessageKey(javaPlugin, "ore-config.factory.name.already-exists").sendMessage(player, new MessageValue("ore-config", input));
                                return false;
                            }

                            return true;
                        }

                        return false;
                    }

                    @Override
                    protected @Nullable
                    Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String name) {
                        oreConfigBuilder.name(name);
                        running = false;
                        new MessageKey(javaPlugin, "ore-config.factory.name.success").sendMessage(getPlayer(), new MessageValue("value", name));
                        return END_OF_CONVERSATION;
                    }

                    @Override
                    public @NotNull
                    String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(javaPlugin, "ore-config.factory.name.text").getMessage();
                    }

                    @Override
                    protected @Nullable
                    String getFailedValidationText(@NotNull final ConversationContext context, @NotNull final String invalidInput) {
                        return new MessageKey(javaPlugin, "ore-config.factory.name.invalid").getMessage(new MessageValue("value", invalidInput));
                    }
                })
                .buildConversation(player);

        player.beginConversation(conversation);
        new CommandListener(javaPlugin, player, conversation);

        return true;
    }

    public boolean setMaterial(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        final Conversation conversation = new ConversationFactory(javaPlugin)
                .withModality(false)
                .withEscapeSequence("!exit")
                .withEscapeSequence("!menu")
                .withLocalEcho(false)
                .addConversationAbandonedListener(event -> {
                    running = false;
                    if (event.gracefulExit()) {
                        consumer.accept(OreConfigFactory.this);
                        return;
                    }

                    final ConversationCanceller canceller = event.getCanceller();

                    if (canceller instanceof ExactMatchConversationCanceller && canceller.cancelBasedOnInput(null, "!menu")) {
                        new MenuGui(javaPlugin, serviceSupplier, this).openSync(player);
                    }

                })
                .withFirstPrompt(new ValidatingPrompt() {
                    @Override
                    protected boolean isInputValid(@NotNull final ConversationContext conversationContext, @NotNull final String text) {
                        return text.equals("!next");
                    }

                    @Override
                    protected @Nullable
                    Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String name) {
                        running = false;
                        return END_OF_CONVERSATION;
                    }

                    @Override
                    public @NotNull
                    String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(javaPlugin, "ore-config.factory.material.text").getMessage();
                    }
                })
                .buildConversation(player);

        player.beginConversation(conversation);

        new MainMaterialListener(javaPlugin, serviceSupplier, player, oreConfigBuilder, conversation);
        new CommandListener(javaPlugin, player, conversation);

        return true;
    }

    public boolean setReplaceMaterials(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        final Conversation conversation = new ConversationFactory(javaPlugin)
                .withModality(false)
                .withEscapeSequence("!exit")
                .withEscapeSequence("!menu")
                .withLocalEcho(false)
                .addConversationAbandonedListener(event -> {
                    running = false;
                    if (event.gracefulExit()) {
                        consumer.accept(OreConfigFactory.this);
                        return;
                    }

                    final ConversationCanceller canceller = event.getCanceller();

                    if (canceller instanceof ExactMatchConversationCanceller && canceller.cancelBasedOnInput(null, "!menu")) {
                        new MenuGui(javaPlugin, serviceSupplier, this).openSync(player);
                    }

                })
                .withFirstPrompt(new ValidatingPrompt() {
                    @Override
                    protected boolean isInputValid(@NotNull final ConversationContext conversationContext, @NotNull final String text) {
                        return text.equals("!next");
                    }

                    @Override
                    protected @Nullable
                    Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String name) {
                        running = false;
                        return END_OF_CONVERSATION;
                    }

                    @Override
                    public @NotNull
                    String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(javaPlugin, "ore-config.factory.replace-material.text").getMessage();
                    }
                })
                .buildConversation(player);

        player.beginConversation(conversation);

        new ReplaceMaterialListener(javaPlugin, serviceSupplier, player, oreConfigBuilder, conversation);
        new CommandListener(javaPlugin, player, conversation);

        return true;
    }

    public boolean setSelectMaterials(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        final Conversation conversation = new ConversationFactory(javaPlugin)
                .withModality(false)
                .withEscapeSequence("!exit")
                .withEscapeSequence("!menu")
                .withLocalEcho(false)
                .addConversationAbandonedListener(event -> {
                    running = false;
                    if (event.gracefulExit()) {
                        consumer.accept(OreConfigFactory.this);
                        return;
                    }

                    final ConversationCanceller canceller = event.getCanceller();

                    if (canceller instanceof ExactMatchConversationCanceller && canceller.cancelBasedOnInput(null, "!menu")) {
                        new MenuGui(javaPlugin, serviceSupplier, this).openSync(player);
                    }

                })
                .withFirstPrompt(new ValidatingPrompt() {
                    @Override
                    protected boolean isInputValid(@NotNull final ConversationContext conversationContext, @NotNull final String text) {
                        return text.equals("!next");
                    }

                    @Override
                    protected @Nullable
                    Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String name) {
                        running = false;
                        return END_OF_CONVERSATION;
                    }

                    @Override
                    public @NotNull
                    String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(javaPlugin, "ore-config.factory.select-material.text").getMessage();
                    }
                })
                .buildConversation(player);

        player.beginConversation(conversation);

        new SelectMaterialListener(javaPlugin, serviceSupplier, player, oreConfigBuilder, conversation);
        new CommandListener(javaPlugin, player, conversation);

        return true;
    }

    public boolean setOreGenerator(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new OreGeneratorGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }).openSync(player);

        return true;
    }

    public boolean setBlockSelector(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new BlockSelectorGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }).openSync(player);

        return true;
    }

    public boolean setBiomes(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new BiomeGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }).openSync(player);

        return true;
    }

    public boolean setOreGeneratorOreSettings(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new OreSettingsGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }, oreConfigBuilder.oreGenerator(), oreConfigBuilder.getOreGeneratorOreSettings()
        ).openSync(player);

        return true;
    }

    public boolean setBlockSelectorOreSettings(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new OreSettingsGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }, oreConfigBuilder.blockSelector(), oreConfigBuilder.getBlockSelectorOreSettings()
        ).openSync(player);

        return true;
    }

    public boolean setCustomDatas(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new CustomDatasGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }).openSync(player);

        return true;
    }

    public boolean setWorlds(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new WorldGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }).openSync(player);

        return true;
    }

    public boolean setName() {
        return setName(OreConfigFactory::setMaterial);
    }

    public boolean setMaterial() {
        return setMaterial(OreConfigFactory::setReplaceMaterials);
    }

    public boolean setReplaceMaterials() {
        return setReplaceMaterials(OreConfigFactory::setSelectMaterials);
    }

    public boolean setSelectMaterials() {
        return setSelectMaterials(OreConfigFactory::setOreGenerator);
    }

    public boolean setOreGenerator() {
        return setOreGenerator(OreConfigFactory::setBlockSelector);
    }

    public boolean setBlockSelector() {
        return setBlockSelector(OreConfigFactory::setBiomes);
    }

    public boolean setBiomes() {
        return setBiomes(factory -> {
            final OreConfigBuilder oreConfigBuilder = factory.getOreConfigBuilder();
            final OreGenerator oreGenerator = oreConfigBuilder.oreGenerator();

            if (oreGenerator != null) {
                if (!oreGenerator.getNeededOreSettings().isEmpty()) {
                    setOreGeneratorOreSettings();
                    return;
                }
            }

            final BlockSelector blockSelector = oreConfigBuilder.blockSelector();

            if (blockSelector != null) {
                if (!blockSelector.getNeededOreSettings().isEmpty()) {
                    setBlockSelectorOreSettings();
                    return;
                }
            }

            checkCustomDatas(oreConfigBuilder);
        });
    }

    public boolean setOreGeneratorOreSettings() {
        return setOreGeneratorOreSettings(factory -> {
            final OreConfigBuilder oreConfigBuilder = factory.getOreConfigBuilder();
            final BlockSelector blockSelector = oreConfigBuilder.blockSelector();

            if (blockSelector != null) {
                if (!blockSelector.getNeededOreSettings().isEmpty()) {
                    setBlockSelectorOreSettings();
                    return;
                }
            }

            checkCustomDatas(oreConfigBuilder);
        });
    }

    public boolean setBlockSelectorOreSettings() {
        return setBlockSelectorOreSettings(factory -> checkCustomDatas(factory.getOreConfigBuilder()));
    }

    public boolean setCustomDatas() {
        return setCustomDatas(OreConfigFactory::setWorlds);
    }

    public boolean setWorlds() {
        return setWorlds(factory -> {
            new MenuGui(this.javaPlugin, this.serviceSupplier, factory).openSync(factory.getPlayer());
        });
    }

    private void checkCustomDatas(OreConfigBuilder oreConfigBuilder) {
        if (oreConfigBuilder.foundCustomDatas().isEmpty()) {
            setWorlds();
            return;
        }

        if (oreConfigBuilder.name() == null) {
            setWorlds();
            return;
        }

        if (oreConfigBuilder.material() == null) {
            setWorlds();
            return;
        }

        if (oreConfigBuilder.replaceMaterial().isEmpty()) {
            setWorlds();
            return;
        }

        if (oreConfigBuilder.oreGenerator() == null) {
            setWorlds();
            return;
        }

        if (oreConfigBuilder.blockSelector() == null) {
            setWorlds();
            return;
        }

        setCustomDatas();
    }

}
