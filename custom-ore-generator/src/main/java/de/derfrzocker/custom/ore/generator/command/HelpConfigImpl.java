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
