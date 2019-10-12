package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.spigot.utils.CommandSeparator;
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

        registerExecutor(new SetValueCommand(serviceSupplier, javaPlugin, messages), "value", permissions.SET_VALUE_PERMISSION);
        registerExecutor(new SetBiomeCommand(serviceSupplier, javaPlugin, messages), "biome", permissions.SET_BIOME_PERMISSION);
        registerExecutor(new SetCustomDataCommand(serviceSupplier, javaPlugin, messages), "customdata", permissions.SET_CUSTOMDATA_PERMISSION);
    }

}
