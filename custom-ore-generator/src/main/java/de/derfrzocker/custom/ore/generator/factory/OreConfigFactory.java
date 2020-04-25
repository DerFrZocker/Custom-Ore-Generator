package de.derfrzocker.custom.ore.generator.factory;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
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

    public boolean setOreSettings(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        new OreSettingsGui(javaPlugin, serviceSupplier, this, oreConfigFactory -> {
            running = false;
            consumer.accept(oreConfigFactory);
        }).openSync(player);

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

}
