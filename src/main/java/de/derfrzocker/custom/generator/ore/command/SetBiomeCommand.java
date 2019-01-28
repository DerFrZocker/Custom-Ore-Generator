package de.derfrzocker.custom.generator.ore.command;

import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.Permissions;
import de.derfrzocker.custom.generator.ore.api.*;
import de.derfrzocker.custom.generator.ore.impl.BiomeConfigYamlImpl;
import de.derfrzocker.custom.generator.ore.impl.OreConfigYamlImpl;
import de.derfrzocker.custom.generator.ore.util.MessageValue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static de.derfrzocker.custom.generator.ore.CustomOreGeneratorMessages.*;

public class SetBiomeCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Permissions.SET_BIOME_PERMISSION.hasPermission(sender))
            return false;

        if (args.length != 5) {
            SET_BIOME_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CustomOreGenerator.getInstance(), () -> {
            String biome_name = args[0];
            String world_name = args[1];
            String material_name = args[2];
            String setting_name = args[3];
            String amount = args[4];

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

            Biome biome;

            try{
                biome = Biome.valueOf(biome_name.toUpperCase());
            }catch (IllegalArgumentException e){
                SET_BIOME_NOT_FOUND.sendMessage(sender, new MessageValue("biome", biome_name));
                return;
            }

            OreSetting setting;

            try {
                setting = OreSetting.valueOf(setting_name.toUpperCase());
            } catch (IllegalArgumentException e) {
                SET_SETTING_NOT_FOUND.sendMessage(sender, new MessageValue("setting", setting_name));
                return;
            }

            int value;

            try {
                value = Integer.valueOf(amount);
            } catch (NumberFormatException e) {
                SET_NO_NUMBER.sendMessage(sender, new MessageValue("value", amount));
                return;
            }

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            WorldConfig worldConfig = service.getWorldConfig(world.getName()).orElseGet(() -> service.createWorldConfig(world));


            BiomeConfig biomeConfig = worldConfig.getBiomeConfig(biome).orElseGet(() -> {
                BiomeConfig config = new BiomeConfigYamlImpl(biome);
                worldConfig.addBiomeConfig(config);
                return config;
            });

            Optional<OreConfig> config = biomeConfig.getOreConfig(material);

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

            if (settings.stream().noneMatch(value2 -> value2 == setting)) {
                SET_SETTING_NOT_VALID.sendMessage(sender, new MessageValue("setting", setting_name), new MessageValue("generator", generator.getName()), new MessageValue("material", material_name));
                return;
            }

            OreConfig oreConfig;

            if (!config.isPresent()) {
                oreConfig = new OreConfigYamlImpl(material, generator.getName());
                biomeConfig.addOreConfig(oreConfig);
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

        if (!Permissions.SET_BIOME_PERMISSION.hasPermission(sender))
            return list;

        if(args.length == 2){
            final String biome_name = args[1].toLowerCase();
            Stream.of(Biome.values()).map(Enum::toString).map(String::toLowerCase).filter(value -> value.contains(biome_name)).forEach(list::add);
            return list;
        }

        if (args.length == 3) {
           if(Stream.of(Biome.values()).map(Enum::toString).noneMatch(value -> value.equalsIgnoreCase(args[1])))
               return list;

            final String world_name = args[2].toLowerCase();
            Bukkit.getWorlds().stream().map(World::getName).filter(value -> value.toLowerCase().contains(world_name)).forEach(list::add);
            return list;
        }

        if (args.length == 4) {
            if(Stream.of(Biome.values()).map(Enum::toString).noneMatch(value -> value.equalsIgnoreCase(args[1])))
                return list;

            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[2])).findAny();

            if (!world.isPresent())
                return list;

            final String material_name = args[3].toUpperCase();
            Stream.of(Material.values()).filter(Material::isBlock).map(Enum::toString).filter(value -> value.contains(material_name)).map(String::toLowerCase).forEach(list::add);
            return list;
        }

        if (args.length == 5) {
            Optional<Biome> biome = Stream.of(Biome.values()).filter(value -> value.toString().equalsIgnoreCase(args[1])).findAny();

            if(!biome.isPresent())
                return list;

            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[2])).findAny();

            if (!world.isPresent())
                return list;

            Optional<Material> material = Stream.of(Material.values()).filter(Material::isBlock).filter(value -> value.toString().equalsIgnoreCase(args[3])).findAny();

            if (!material.isPresent())
                return list;

            final String setting_name = args[4].toUpperCase();

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            Set<OreSetting> settings = service.getWorldConfig(world.get().getName()).
                    map(value -> value.getBiomeConfig(biome.get())).
                    map(value2 -> value2.
                            map(value3 -> value3.getOreConfig(material.get())).
                            map(value4 ->
                                    value4.map(value5-> service.getOreGenerator(value5.getOreGenerator()).
                                    map(OreGenerator::getNeededOreSettings).
                                            orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                                    orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                            orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                    orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings());

            settings.stream().map(Enum::toString).filter(value -> value.contains(setting_name)).map(String::toLowerCase).forEach(list::add);

            return list;
        }

        if (args.length == 6) {
            Optional<Biome> biome = Stream.of(Biome.values()).filter(value -> value.toString().equalsIgnoreCase(args[1])).findAny();

            if(!biome.isPresent())
                return list;

            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[1])).findAny();

            if (!world.isPresent())
                return list;

            Optional<Material> material = Stream.of(Material.values()).filter(Material::isBlock).filter(value -> value.toString().equalsIgnoreCase(args[2])).findAny();

            if (!material.isPresent())
                return list;

            CustomOreGeneratorService service = CustomOreGenerator.getService();

            Optional<WorldConfig> config = service.getWorldConfig(world.get().getName());

            Set<OreSetting> settings =  config.map(value -> value.getBiomeConfig(biome.get())).
                    map(value2 -> value2.
                            map(value3 -> value3.getOreConfig(material.get())).
                            map(value4 ->
                                    value4.map(value5-> service.getOreGenerator(value5.getOreGenerator()).
                                            map(OreGenerator::getNeededOreSettings).
                                            orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                                            orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                            orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings())).
                    orElseGet(() -> service.getDefaultOreGenerator().getNeededOreSettings());

            Optional<OreSetting> setting = settings.stream().filter(value -> value.toString().equalsIgnoreCase(args[3])).findAny();

            if (!setting.isPresent())
                return list;

            if (!config.isPresent())
                return list;

            Optional<BiomeConfig> biomeConfig = config.get().getBiomeConfig(biome.get());

            if(!biomeConfig.isPresent())
                return list;

            Optional<OreConfig> oreConfig = biomeConfig.get().getOreConfig(material.get());

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
