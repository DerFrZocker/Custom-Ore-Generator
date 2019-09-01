package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages.*;


public class SetValueCommand implements TabExecutor {

    @Override //oregen set value <world> <config_name> <setting> <amount>
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 4) {
            SET_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CustomOreGenerator.getInstance(), () -> {
            String world_name = args[0];
            String config_name = args[1];
            String setting_name = args[2];
            String amount = args[3];

            World world = Bukkit.getWorld(world_name);

            if (world == null) {
                SET_WORLD_NOT_FOUND.sendMessage(sender, new MessageValue("world_name", world_name));
                return;
            }

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

            if (!worldConfigOptional.isPresent()) {
                sender.sendMessage("Not found! //TODO add message"); //TODO add message
                return;
            }

            WorldConfig worldConfig = worldConfigOptional.get();

            Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(config_name);

            if (!oreConfigOptional.isPresent()) {
                sender.sendMessage("Not found! //TODO add message"); //TODO add message
                return;
            }

            OreConfig oreConfig = oreConfigOptional.get();

            OreSetting setting;

            try {
                setting = OreSetting.valueOf(setting_name.toUpperCase());
            } catch (IllegalArgumentException e) {
                SET_SETTING_NOT_FOUND.sendMessage(sender, new MessageValue("setting", setting_name));
                return;
            }


            Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(oreConfig.getOreGenerator());

            if (!optionalOreGenerator.isPresent()) {
                sender.sendMessage("Not found! //TODO add message"); //TODO add message
                return;
            }

            OreGenerator generator = optionalOreGenerator.get();

            Set<OreSetting> settings = generator.getNeededOreSettings();

            if (settings.stream().noneMatch(value -> value == setting)) {
                SET_SETTING_NOT_VALID.sendMessage(sender, new MessageValue("setting", setting_name), new MessageValue("generator", generator.getName()));
                return;
            }

            int value;

            try {
                value = Integer.parseInt(amount);
            } catch (NumberFormatException e) {
                SET_NO_NUMBER.sendMessage(sender, new MessageValue("value", amount));
                return;
            }

            oreConfig.setValue(setting, value);

            service.saveWorldConfig(worldConfig);
            SET_SUCCESS.sendMessage(sender);
        });

        return true;
    }

    @Override //oregen set value <world> <config_name> <setting> <amount>
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

            final String setting_name = args[2].toUpperCase();

            final Optional<OreGenerator> oreGenerator = service.getOreGenerator(oreConfig.get().getOreGenerator());

            if (!oreGenerator.isPresent())
                return list;

            oreGenerator.get().getNeededOreSettings().stream().map(Enum::toString).filter(value -> value.contains(setting_name)).forEach(list::add);

            return list;
        }

        return list;
    }

}
