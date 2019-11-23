package de.derfrzocker.custom.ore.generator.command.add;

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

public class AddCommand extends CommandSeparator {

    public AddCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages, @NotNull final Permissions permissions) {
        super(javaPlugin);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");
        Validate.notNull(permissions, "Permissions can not be null");

        registerExecutor(new AddOreConfigCommand(serviceSupplier, javaPlugin, messages), "ore-config", permissions.ADD_ORE_CONFIG_PERMISSION, messages.COMMAND_ADD_ORE_CONFIG_USAGE, messages.COMMAND_ADD_ORE_CONFIG_DESCRIPTION);

        final HelpCommand helpCommand = new HelpCommand(this, new HelpConfigImpl(messages));
        registerExecutor(helpCommand, "help", null, messages.COMMAND_ADD_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION);
        registerExecutor(helpCommand, null, null, messages.COMMAND_ADD_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION);
    }

}
