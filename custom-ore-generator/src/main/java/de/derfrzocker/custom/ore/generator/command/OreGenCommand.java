package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.command.set.SetCommand;
import de.derfrzocker.spigot.utils.CommandSeparator;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OreGenCommand extends CommandSeparator {

    public OreGenCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin) {
        super(javaPlugin);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        registerExecutor(new SetCommand(serviceSupplier, javaPlugin), "set", Permissions.SET_PERMISSION);
        registerExecutor(new ReloadCommand(javaPlugin), "reload", Permissions.RELOAD_PERMISSION);
        registerExecutor(new CreateCommand(serviceSupplier, javaPlugin), "create", Permissions.CREATE_PERMISSION);

    }
}
