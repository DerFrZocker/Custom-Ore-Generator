package de.derfrzocker.custom.genrator.ore.command;


import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.Permissions;
import de.derfrzocker.custom.generator.ore.api.*;
import de.derfrzocker.custom.generator.ore.impl.OreConfigYamlImpl;
import de.derfrzocker.custom.generator.ore.util.MessageValue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static de.derfrzocker.custom.generator.ore.CustomOreGeneratorMessages.*;


public class SetCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Permissions.SET_PERMISSION.hasPermission(sender))
            return false;

        if (args.length != 4) {
            SET_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CustomOreGenerator.getInstance(), () -> {
            String world_name = args[0];
            String material_name = args[1];
            String setting_name = args[2];
            String amount = args[3];

            World world = Bukkit.getWorld(world_name);

            if (world == null) {
                SET_WORLD_NOT_FOUND.sendMessage(sender, new MessageValue("world_name", world_name));
                return;
            }

            Material material;

            try {
                material = Material.valueOf(material_name.toUpperCase());
            } catch (IllegalArgumentException e) {
                SET_MATERIAL_NOT_FOUND.sendMessage(sender, new MessageValue("material", material_name));
                return;
            }

            if (!material.isBlock()) {
                SET_NO_BLOCK_MATERIAL.sendMessage(sender, new MessageValue("material", material_name));
                return;
            }

            OreSetting setting;

            try {
                setting = OreSetting.valueOf(setting_name.toUpperCase());
            } catch (IllegalArgumentException e) {
                SET_SETTING_NOT_FOUND.sendMessage(sender, new MessageValue("setting", setting_name));
                return;
            }

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            WorldConfig worldConfig = service.getWorldConfig(world.getName()).orElseGet(() -> service.createWorldConfig(world));


            Optional<OreConfig> config = worldConfig.getOreConfig(material);

            Set<OreSetting> settings;
            OreGenerator generator = null;

            if (config.isPresent()) {
                Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(config.get().getOreGenerator());
                if (optionalOreGenerator.isPresent()) {
                    settings = optionalOreGenerator.get().getNeededOreSettings();
                    generator = optionalOreGenerator.get();
                } else settings = service.getDefaultOreGenerator().getNeededOreSettings();
            } else settings = service.getDefaultOreGenerator().getNeededOreSettings();

            if (generator == null)
                generator = service.getDefaultOreGenerator();

            if (settings.stream().noneMatch(value -> value == setting)) {
                SET_SETTING_NOT_VALID.sendMessage(sender, new MessageValue("setting", setting_name), new MessageValue("generator", generator.getName()), new MessageValue("material", material_name));
                return;
            }

            int value;

            try {
                value = Integer.valueOf(amount);
            } catch (NumberFormatException e) {
                SET_NO_NUMBER.sendMessage(sender, new MessageValue("value", amount));
                return;
            }

            OreConfig oreConfig;

            if (!config.isPresent()) {
                oreConfig = new OreConfigYamlImpl(material, generator.getName());
                worldConfig.addOreConfig(oreConfig);
            } else oreConfig = config.get();

            oreConfig.setValue(setting, value);

            service.saveWorldConfig(worldConfig);
            SET_SUCCESS.sendMessage(sender);
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> list = new ArrayList<>();

        if (!Permissions.SET_PERMISSION.hasPermission(sender))
            return list;

        if (args.length == 2) {
            final String world_name = args[1].toLowerCase();
            Bukkit.getWorlds().stream().map(World::getName).filter(value -> value.toLowerCase().contains(world_name)).forEach(list::add);
            return list;
        }

        if (args.length == 3) {
            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[1])).findAny();

            if (!world.isPresent())
                return list;

            final String material_name = args[2].toUpperCase();
            Stream.of(Material.values()).filter(Material::isBlock).map(Enum::toString).filter(value -> value.contains(material_name)).map(String::toLowerCase).forEach(list::add);
            return list;
        }

        if (args.length == 4) {
            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[1])).findAny();

            if (!world.isPresent())
                return list;

            Optional<Material> material = Stream.of(Material.values()).filter(Material::isBlock).filter(value -> value.toString().equalsIgnoreCase(args[2])).findAny();

            if (!material.isPresent())
                return list;

            final String setting_name = args[3].toUpperCase();

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            Set<OreSetting> settings = service.getWorldConfig(world.get().getName()).
                    map(value -> value.getOreConfig(material.get()).
                            map(value2 -> service.getOreGenerator(value2.getOreGenerator())).
                            map(value2 -> value2.
                                    map(OreGenerator::getNeededOreSettings).
                                    orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                            orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                    orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings());

            settings.stream().map(Enum::toString).filter(value -> value.contains(setting_name)).map(String::toLowerCase).forEach(list::add);

            return list;
        }

        if (args.length == 5) {

            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[1])).findAny();

            if (!world.isPresent())
                return list;

            Optional<Material> material = Stream.of(Material.values()).filter(Material::isBlock).filter(value -> value.toString().equalsIgnoreCase(args[2])).findAny();

            if (!material.isPresent())
                return list;

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            Optional<WorldConfig> config = service.getWorldConfig(world.get().getName());

            Set<OreSetting> settings = config.
                    map(value -> value.getOreConfig(material.get()).
                            map(value2 -> service.getOreGenerator(value2.getOreGenerator())).
                            map(value2 -> value2.
                                    map(OreGenerator::getNeededOreSettings).
                                    orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                            orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                    orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings());

            Optional<OreSetting> setting = settings.stream().filter(value -> value.toString().equalsIgnoreCase(args[3])).findAny();

            if (!setting.isPresent())
                return list;

            if (!config.isPresent())
                return list;

            Optional<OreConfig> oreConfig = config.get().getOreConfig(material.get());

            if (!oreConfig.isPresent())
                return list;

            Optional<Integer> value = oreConfig.get().getValue(setting.get());

            if (!value.isPresent())
                return list;

            list.add("current: " + value.get());

            return list;
        }

        return list;
    }

}
