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

package de.derfrzocker.custom.ore.generator.command.set.value;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.command.HelpConfigImpl;
import de.derfrzocker.spigot.utils.command.CommandSeparator;
import de.derfrzocker.spigot.utils.command.HelpCommand;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SetValueCommand extends CommandSeparator {

    public SetValueCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages, @NotNull final Permissions permissions) {
        super(javaPlugin);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");
        Validate.notNull(permissions, "Permissions can not be null");

        registerExecutor(new SetValueOreGeneratorCommand(serviceSupplier, javaPlugin, messages), "ore-generator", permissions.SET_VALUE_ORE_GENERATOR_PERMISSION, messages.COMMAND_SET_VALUE_USAGE, messages.COMMAND_SET_VALUE_DESCRIPTION);
        registerExecutor(new SetValueBlockSelectorCommand(serviceSupplier, javaPlugin, messages), "block-selector", permissions.SET_VALUE_BLOCK_SELECTOR_PERMISSION, messages.COMMAND_SET_VALUE_USAGE, messages.COMMAND_SET_VALUE_DESCRIPTION);

        final HelpCommand helpCommand = new HelpCommand(this, new HelpConfigImpl(messages));
        registerExecutor(helpCommand, "help", null, messages.COMMAND_SET_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION); //TODO
        registerExecutor(helpCommand, null, null, messages.COMMAND_SET_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION);
    }

}
