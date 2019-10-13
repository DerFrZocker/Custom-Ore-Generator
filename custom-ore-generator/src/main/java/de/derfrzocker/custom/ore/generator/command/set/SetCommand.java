package de.derfrzocker.custom.ore.generator.command.set;

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

public class SetCommand extends CommandSeparator {

    public SetCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages, @NotNull final Permissions permissions) {
        super(javaPlugin);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");
        Validate.notNull(permissions, "Permissions can not be null");

        registerExecutor(new SetValueCommand(serviceSupplier, javaPlugin, messages), "value", permissions.SET_VALUE_PERMISSION, messages.COMMAND_SET_VALUE_USAGE, messages.COMMAND_SET_VALUE_DESCRIPTION);
        registerExecutor(new SetBiomeCommand(serviceSupplier, javaPlugin, messages), "biome", permissions.SET_BIOME_PERMISSION, messages.COMMAND_SET_BIOME_USAGE, messages.COMMAND_SET_BIOME_DESCRIPTION);
        registerExecutor(new SetCustomDataCommand(serviceSupplier, javaPlugin, messages), "customdata", permissions.SET_CUSTOMDATA_PERMISSION, messages.COMMAND_SET_CUSTOMDATA_USAGE, messages.COMMAND_SET_CUSTOMDATA_DESCRIPTION);
        registerExecutor(new SetReplaceMaterialCommand(serviceSupplier, javaPlugin, messages), "replacematerial", permissions.SET_REPLACEMATERIAL_PERMISSION, messages.COMMAND_SET_REPLACEMATERIAL_USAGE, messages.COMMAND_SET_REPLACEMATERIAL_DESCRIPTION);

        final HelpCommand helpCommand = new HelpCommand(this, new HelpConfigImpl(messages));
        registerExecutor(helpCommand, "help", null, messages.COMMAND_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION);
        registerExecutor(helpCommand, null, null, messages.COMMAND_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION);
    }

}
