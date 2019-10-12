package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.command.set.SetCommand;
import de.derfrzocker.spigot.utils.CommandSeparator;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OreGenCommand extends CommandSeparator {

    public OreGenCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages, @NotNull final Permissions permissions) {
        super(javaPlugin);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");
        Validate.notNull(permissions, "Permissions can not be null");

        registerExecutor(new SetCommand(serviceSupplier, javaPlugin, messages, permissions), "set", permissions.SET_PERMISSION);
        registerExecutor(new ReloadCommand(javaPlugin, messages), "reload", permissions.RELOAD_PERMISSION);
        registerExecutor(new CreateCommand(serviceSupplier, javaPlugin, messages), "create", permissions.CREATE_PERMISSION);
    }

}
