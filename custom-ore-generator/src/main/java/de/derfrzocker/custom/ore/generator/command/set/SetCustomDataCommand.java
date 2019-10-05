package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.CommandUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages.*;

public class SetCustomDataCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;

    public SetCustomDataCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
    }

    @Override //oregen set customdata <world> <config_name> <customdata> <value>
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length != 4) {
            COMMAND_SET_CUSTOMDATA_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        CommandUtil.runAsynchronously(sender, javaPlugin, () -> {
            final String worldName = args[0];
            final String configName = args[1];
            final String customDataName = args[2];
            final String customDataValue = args[3];

            final World world = Bukkit.getWorld(worldName);

            if (world == null) {
                COMMAND_WORLD_NOT_FOUND.sendMessage(sender, new MessageValue("world", worldName));
                return;
            }

            final CustomOreGeneratorService service = serviceSupplier.get();

            final Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

            if (!worldConfigOptional.isPresent()) {
                COMMAND_ORE_CONFIG_NOT_FOUND.sendMessage(sender, new MessageValue("ore-config", configName));
                return;
            }

            final WorldConfig worldConfig = worldConfigOptional.get();

            final Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(configName);

            if (!oreConfigOptional.isPresent()) {
                COMMAND_ORE_CONFIG_NOT_FOUND.sendMessage(sender, new MessageValue("ore-config", configName));
                return;
            }
            final OreConfig oreConfig = oreConfigOptional.get();

            final Optional<CustomData> customDataOptional = service.getCustomData(customDataName);

            if (!customDataOptional.isPresent()) {
                COMMAND_SET_CUSTOMDATA_NOT_FOUND.sendMessage(sender, new MessageValue("customdata", customDataName));
                return;
            }

            final CustomData customData = customDataOptional.get();

            if (!customData.canApply(oreConfig)) {
                COMMAND_SET_CUSTOMDATA_ORE_CONFIG_NOT_VALID.sendMessage(sender, new MessageValue("customdata", customData.getName()), new MessageValue("ore-config", oreConfig.getName()));
                return;
            }

            final Object data;

            try {
                data = parse(customDataValue, customData.getCustomDataType());
            } catch (IllegalArgumentException e) {
                COMMAND_SET_CUSTOMDATA_VALUE_NOT_VALID.sendMessage(sender, new MessageValue("value", customDataValue));
                return;
            }

            if (!customData.isValidCustomData(data, oreConfig)) {
                COMMAND_SET_CUSTOMDATA_VALUE_NOT_VALID.sendMessage(sender, new MessageValue("value", data));
                return;
            }

            oreConfig.setCustomData(customData, data);
            service.saveWorldConfig(worldConfig);
            COMMAND_SET_CUSTOMDATA_SUCCESS.sendMessage(sender);
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = serviceSupplier.get();

        if (args.length == 1) {
            final String world_name = args[0].toLowerCase();
            Bukkit.getWorlds().stream().map(World::getName).filter(value -> value.toLowerCase().contains(world_name)).forEach(list::add);
            return list;
        }

        if (args.length == 2) {
            final Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            final Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (!worldConfig.isPresent())
                return list;

            final String config_name = args[1];
            worldConfig.get().getOreConfigs().stream().map(OreConfig::getName).filter(name -> name.contains(config_name)).forEach(list::add);
            return list;
        }

        if (args.length == 3) {
            final Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            final Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (!worldConfig.isPresent())
                return list;

            final Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

            if (!oreConfig.isPresent())
                return list;

            final String customDataName = args[2].toUpperCase();

            service.getCustomData().stream().filter(customData -> customData.canApply(oreConfig.get())).map(CustomData::getName).filter(value -> value.contains(customDataName)).forEach(list::add);

            return list;
        }

        return list;
    }

    private Object parse(final String data, final CustomDataType customDataType) throws IllegalArgumentException {
        switch (customDataType) {
            case STRING:
                return data;
            case INTEGER:
                return Integer.parseInt(data);
            case DOUBLE:
                return Double.parseDouble(data);
            case BOOLEAN:
                return Boolean.parseBoolean(data);
        }

        throw new IllegalArgumentException();
    }

}
