package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.command.set.SetCommand;
import de.derfrzocker.spigot.utils.Pair;
import de.derfrzocker.spigot.utils.command.CommandException;
import de.derfrzocker.spigot.utils.command.CommandSeparator;
import de.derfrzocker.spigot.utils.command.HelpCommand;
import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class OreGenCommand extends CommandSeparator {

    public OreGenCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages, @NotNull final Permissions permissions) {
        super(javaPlugin);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");
        Validate.notNull(permissions, "Permissions can not be null");

        registerExecutor(new SetCommand(serviceSupplier, javaPlugin, messages, permissions), "set", permissions.SET_PERMISSION, messages.COMMAND_SET_USAGE, messages.COMMAND_SET_DESCRIPTION);
        registerExecutor(new ReloadCommand(javaPlugin, messages), "reload", permissions.RELOAD_PERMISSION, messages.COMMAND_RELOAD_USAGE, messages.COMMAND_RELOAD_DESCRIPTION);
        registerExecutor(new CreateCommand(serviceSupplier, javaPlugin, messages), "create", permissions.CREATE_PERMISSION, messages.COMMAND_CREATE_USAGE, messages.COMMAND_CREATE_DESCRIPTION);

        final HelpCommand helpCommand = new HelpCommand(this, new HelpConfigImpl(messages));
        registerExecutor(helpCommand, "help", null, messages.COMMAND_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION);
        registerExecutor(helpCommand, null, null, messages.COMMAND_HELP_USAGE, messages.COMMAND_HELP_DESCRIPTION);
    }

    /**
     * returns a Pair with the WorldConfig and OreConfig for the given values, if the WorldConfig or OreConfig
     * does not exists, it throw a CommandException.
     * When messageKey and commandSender are not null it also send a message to the given commandSender
     * When it send the message, it gives the World name as the "world" placeholder and the oreConfigName as "ore-config" placeholder
     *
     * @param world         the World of the WorldConfig
     * @param oreConfigName the name the the OreConfig
     * @param service       to use
     * @param messageKey    the message to send when no WorldConfig or OreConfig was found
     * @param commandSender to send the message when no WorldConfig or OreConfig was found
     * @return a Pair which contains the WorldConfig and OreConfig for the given values, the Pair values are never null
     * @throws IllegalArgumentException when world, oreConfigName or service is null
     * @throws CommandException         when the WorldConfig or OreConfig was not found
     */
    @NotNull
    public static Pair<WorldConfig, OreConfig> getWorldAndOreConfig(@NotNull final World world, @NotNull final String oreConfigName, @NotNull final CustomOreGeneratorService service, @Nullable final MessageKey messageKey, @Nullable final CommandSender commandSender) {
        Validate.notNull(world, "World can not be null");
        Validate.notNull(oreConfigName, "OreConfig name can not be null");
        Validate.notNull(service, "CustomOreGeneratorService can not be null");

        final Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

        if (!worldConfigOptional.isPresent()) {
            if (messageKey != null && commandSender != null) {
                messageKey.sendMessage(commandSender, new MessageValue("world", world.getName()), new MessageValue("ore-config", oreConfigName));
            }
            throw new CommandException("WorldConfig for world " + world.getName() + " not found");
        }

        final WorldConfig worldConfig = worldConfigOptional.get();
        final Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(oreConfigName);

        if (!oreConfigOptional.isPresent()) {
            if (messageKey != null && commandSender != null) {
                messageKey.sendMessage(commandSender, new MessageValue("world", world.getName()), new MessageValue("ore-config", oreConfigName));
            }
            throw new CommandException("OreConfig " + oreConfigName + " for world " + world.getName() + " not found");
        }

        return new Pair<>(worldConfig, oreConfigOptional.get());
    }

    /**
     * return the OreConfig for the given name, it the OreConfig does not exists, it will throw a CommandException
     * <p>
     * When messageKey and commandSender are not null it also send a message to the given commandSender
     * When it send the message, it gives the oreConfigName as "ore-config" placeholder
     *
     * @param oreConfigName the name the the OreConfig
     * @param service       to use
     * @param messageKey    the message to send when no OreConfig was found
     * @param commandSender to send the message when no OreConfig was found
     * @return the OreConfig with the given name
     * @throws IllegalArgumentException oreConfigName or service is null
     * @throws CommandException         when the OreConfig was not found
     */
    @NotNull
    public static OreConfig getOreConfig(@NotNull final String oreConfigName, @NotNull final CustomOreGeneratorService service, @Nullable final MessageKey messageKey, @Nullable final CommandSender commandSender) {

        final Optional<OreConfig> oreConfigOptional = service.getOreConfig(oreConfigName);

        if (oreConfigOptional.isPresent())
            return oreConfigOptional.get();

        if (messageKey != null && commandSender != null) {
            messageKey.sendMessage(commandSender, new MessageValue("ore-config", oreConfigName));
        }

        throw new CommandException("OreConfig " + oreConfigName + " not found");
    }

}
