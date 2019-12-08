package de.derfrzocker.custom.ore.generator.command.add;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.spigot.utils.command.CommandUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class AddOreConfigCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final CustomOreGeneratorMessages messages;

    public AddOreConfigCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.messages = messages;
    }

    @Override  //oregen add ore-config <world> <ore-config> [<position>]
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length < 2 || args.length > 3) {
            messages.COMMAND_ADD_ORE_CONFIG_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        CommandUtil.runAsynchronously(sender, javaPlugin, () -> {
            final String worldName = args[0];
            final String configName = args[1];
            final String positionString = args.length == 3 ? args[2] : null;

            final CustomOreGeneratorService service = serviceSupplier.get();

            final Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(worldName);
            final WorldConfig worldConfig;

            if (worldConfigOptional.isPresent()) {
                worldConfig = worldConfigOptional.get();
            } else {
                final World world = Bukkit.getWorld(worldName);

                if (world == null) {
                    messages.COMMAND_WORLD_NOT_FOUND.sendMessage(sender, new MessageValue("world", worldName));
                    return;
                }

                worldConfig = service.createWorldConfig(world);
            }

            if (worldConfig.getOreConfig(configName).isPresent()) {
                messages.COMMAND_ADD_ORE_CONFIG_PRESENT.sendMessage(sender, new MessageValue("ore-config", configName));
                return;
            }

            final Optional<OreConfig> oreConfigOptional = service.getOreConfig(configName);

            if (!oreConfigOptional.isPresent()) {
                messages.COMMAND_ORE_CONFIG_NOT_FOUND.sendMessage(sender, new MessageValue("ore-config", configName));
                return;
            }

            final OreConfig oreConfig = oreConfigOptional.get();

            if (positionString != null) {
                final int position;

                try {
                    position = Integer.parseInt(positionString);
                } catch (final NumberFormatException e) {
                    messages.COMMAND_ADD_ORE_CONFIG_VALUE_NOT_VALID.sendMessage(sender, new MessageValue("value", positionString));
                    return;
                }
                worldConfig.addOreConfig(oreConfig, position);
            } else {
                worldConfig.addOreConfig(oreConfig);
            }

            service.saveWorldConfig(worldConfig);
            messages.COMMAND_ADD_ORE_CONFIG_SUCCESS.sendMessage(sender);
        });

        return true;
    }

    @Nullable
    @Override  //oregen add ore-config <world> <ore-config> [<position>]
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = serviceSupplier.get();

        if (args.length == 1) {
            final String worldName = args[0].toLowerCase();
            Bukkit.getWorlds().stream().map(World::getName).filter(value -> value.toLowerCase().contains(worldName)).forEach(list::add);
            return list;
        }

        if (args.length == 2) {
            final Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            final Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());
            final Set<OreConfig> oreConfigs = new HashSet<>(service.getOreConfigs());
            final String configName = args[1];

            worldConfig.ifPresent(worldConfig1 -> oreConfigs.removeAll(worldConfig1.getOreConfigs()));

            oreConfigs.stream().map(OreConfig::getName).filter(name -> name.contains(configName)).forEach(list::add);

            return list;
        }

        return list;
    }

}
