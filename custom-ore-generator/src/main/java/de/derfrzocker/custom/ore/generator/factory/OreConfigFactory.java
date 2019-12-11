package de.derfrzocker.custom.ore.generator.factory;

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
import java.util.regex.Pattern;

public class OreConfigFactory implements Listener {
    private final static Pattern ORE_CONFIG_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");

    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final Player player;
    @NotNull
    private final OreConfigBuilder oreConfigBuilder;
    private boolean running = false;

    public OreConfigFactory(@NotNull final JavaPlugin javaPlugin, @NotNull final Player player) {
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(player, "Player can not be null");

        this.javaPlugin = javaPlugin;
        this.player = player;
        this.oreConfigBuilder = OreConfigBuilder.newBuilder();
    }

    public boolean setName(@NotNull final Consumer<OreConfigFactory> consumer) {
        if (running)
            return false;

        running = true;

        final Conversation conversation = new ConversationFactory(javaPlugin)
                .withEscapeSequence("!exit")
                .withEscapeSequence("!menu")
                .addConversationAbandonedListener(event -> {
                    running = false;
                    if (event.gracefulExit()) {
                        consumer.accept(OreConfigFactory.this);
                    }

                    final ConversationCanceller canceller = event.getCanceller();

                    if (canceller instanceof ExactMatchConversationCanceller && canceller.cancelBasedOnInput(null, "!menu")) {
                        //TODO open menu
                    }

                })
                .withFirstPrompt(new RegexPrompt(ORE_CONFIG_NAME_PATTERN) {
                    @Override
                    protected @Nullable Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String name) {
                        oreConfigBuilder.name(name);
                        running = false;
                        return END_OF_CONVERSATION;
                    }

                    @Override
                    public @NotNull String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(javaPlugin, "ore-config.factory.name.text").getMessage();
                    }

                    @Override
                    protected @Nullable String getFailedValidationText(@NotNull final ConversationContext context, @NotNull final String invalidInput) {
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
                .addConversationAbandonedListener(event -> {
                    running = false;
                    if (event.gracefulExit()) {
                        consumer.accept(OreConfigFactory.this);
                    }

                    final ConversationCanceller canceller = event.getCanceller();

                    if (canceller instanceof ExactMatchConversationCanceller && canceller.cancelBasedOnInput(null, "!menu")) {
                        //TODO open menu
                    }

                })
                .withFirstPrompt(new ValidatingPrompt() {
                    @Override
                    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String text) {
                        return text.equals("!next");
                    }

                    @Override
                    protected @Nullable Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String name) {
                        running = false;
                        return END_OF_CONVERSATION;
                    }

                    @Override
                    public @NotNull String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(javaPlugin, "ore-config.factory.material.text").getMessage();
                    }
                })
                .buildConversation(player);

        player.beginConversation(conversation);

        new MainMaterialListener(javaPlugin, player, oreConfigBuilder, conversation);
        new CommandListener(javaPlugin, player, conversation);

        return true;
    }

    public boolean setSelectMaterials(@NotNull final Consumer<OreConfigFactory> consumer){
        if (running)
            return false;

        running = true;

        final Conversation conversation = new ConversationFactory(javaPlugin)
                .withEscapeSequence("!exit")
                .withFirstPrompt(new ValidatingPrompt() {
                    @Override
                    protected boolean isInputValid(@NotNull ConversationContext conversationContext, @NotNull String text) {
                        return text.equals("!menu");
                    }

                    @Override
                    protected @Nullable Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String name) {
                        running = false;
                        //TODO open menu
                        return END_OF_CONVERSATION;
                    }

                    @Override
                    public @NotNull String getPromptText(@NotNull final ConversationContext conversationContext) {
                        return new MessageKey(javaPlugin, "ore-config.factory.material.text").getMessage();
                    }
                })
                .buildConversation(player);

        player.beginConversation(conversation);

        new MainMaterialListener(javaPlugin, player, oreConfigBuilder, conversation);

        return true;
    }


}
