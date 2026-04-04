package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.spigot.utils.command.HelpConfig;
import de.derfrzocker.spigot.utils.message.MessageKey;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public class HelpConfigImpl implements HelpConfig {

    @NotNull
    private final CustomOreGeneratorMessages messages;

    public HelpConfigImpl(@NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.messages = messages;
    }

    @NotNull
    @Override
    public MessageKey getSeparatorMessageFormat() {
        return messages.COMMAND_HELP_SEPARATOR_FORMAT;
    }

    @NotNull
    @Override
    public MessageKey getHeaderMessageFormat() {
        return messages.COMMAND_HELP_HEADER_FORMAT;
    }

    @NotNull
    @Override
    public MessageKey getFooterMessageFormat() {
        return messages.COMMAND_HELP_FOOTER_FORMAT;
    }

    @NotNull
    @Override
    public MessageKey getPermissionMessageFormat() {
        return messages.COMMAND_HELP_PERMISSION_FORMAT;
    }

    @NotNull
    @Override
    public MessageKey getUsageMessageFormat() {
        return messages.COMMAND_HELP_USAGE_FORMAT;
    }

    @NotNull
    @Override
    public MessageKey getDescriptionMessageFormat() {
        return messages.COMMAND_HELP_DESCRIPTION_FORMAT;
    }

    @NotNull
    @Override
    public MessageKey getShortHelpMessageFormat() {
        return messages.COMMAND_HELP_SHORT_FORMAT;
    }

}
