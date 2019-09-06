package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.CommandUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SetCustomDataCommand implements TabExecutor {

    private final CustomOreGenerator customOreGenerator;

    @Override //oregen set customdata <world> <config_name> <customdata> <value>
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 4) {
            sender.sendMessage("Not enough args! //TODO add message"); //TODO add message
            return true;
        }

        CommandUtil.runAsynchronously(sender, customOreGenerator, () -> {
            final String worldName = args[0];
            final String configName = args[1];
            final String customDataName = args[2];
            final String customDataValue = args[3];

            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                sender.sendMessage("World not found! //TODO add message"); //TODO add message
                return;
            }

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

            if (!worldConfigOptional.isPresent()) {
                sender.sendMessage("WorldConfig not found! //TODO add message"); //TODO add message
                return;
            }

            WorldConfig worldConfig = worldConfigOptional.get();

            Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(configName);

            if (!oreConfigOptional.isPresent()) {
                sender.sendMessage("OreConfig not found! //TODO add message"); //TODO add message
                return;
            }
            OreConfig oreConfig = oreConfigOptional.get();

            Optional<CustomData> customDataOptional = service.getCustomData(customDataName);

            if (!customDataOptional.isPresent()) {
                sender.sendMessage("CustomData not found! //TODO add message"); //TODO add message
                return;
            }

            CustomData customData = customDataOptional.get();

            if (!customData.canApply(oreConfig)) {
                sender.sendMessage("CustomData not valid! //TODO add message"); //TODO add message
                return;
            }

            Object data;

            try {
                data = parse(customDataValue, customData.getCustomDataType());
            } catch (IllegalArgumentException e) {
                sender.sendMessage("CustomData not valid! //TODO add message"); //TODO add message
                return;
            }

            if (!customData.isValidCustomData(data, oreConfig)) {
                sender.sendMessage("CustomData not valid! //TODO add message"); //TODO add message
                return;
            }

            oreConfig.setCustomData(customData, data);

            service.saveWorldConfig(worldConfig);
        });

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = CustomOreGenerator.getService();

        if (args.length == 1) {
            final String world_name = args[0].toLowerCase();
            Bukkit.getWorlds().stream().map(World::getName).filter(value -> value.toLowerCase().contains(world_name)).forEach(list::add);
            return list;
        }

        if (args.length == 2) {
            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (!worldConfig.isPresent())
                return list;

            final String config_name = args[1];
            worldConfig.get().getOreConfigs().stream().map(OreConfig::getName).filter(name -> name.contains(config_name)).forEach(list::add);
            return list;
        }

        if (args.length == 3) {
            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (!worldConfig.isPresent())
                return list;

            Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

            if (!oreConfig.isPresent())
                return list;

            final String customDataName = args[2].toUpperCase();

            service.getCustomData().stream().map(CustomData::getName).filter(value -> value.contains(customDataName)).forEach(list::add);

            return list;
        }

        return list;
    }

    private Object parse(String data, CustomDataType customDataType) throws IllegalArgumentException {
        switch (customDataType) {
            case STRING:
                return data;
            case INTEGER:
                return Integer.parseInt(data);
            case DOUBLE:
                return Double.parseDouble(data);
        }

        throw new IllegalArgumentException();
    }

}
