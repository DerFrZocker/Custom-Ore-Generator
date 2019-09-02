package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.impl.OreConfigYamlImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CreateCommand implements TabExecutor {

    @Override //oregen create <world> <name> <material> [<ore-generator>]
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Not enough args! //TODO add message"); //TODO add message
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CustomOreGenerator.getInstance(), () -> {
            final String worldName = args[0];
            final String oreConfigName = args[1];
            final String materialName = args[2];

            final World world = Bukkit.getWorld(worldName);

            if (world == null) {
                sender.sendMessage("World not found! //TODO add message"); //TODO add message
                return;
            }

            final CustomOreGeneratorService service = CustomOreGenerator.getService();

            final Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

            final WorldConfig worldConfig = worldConfigOptional.orElseGet(() -> service.createWorldConfig(world));

            final Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(oreConfigName);

            if (oreConfigOptional.isPresent()) {
                sender.sendMessage("already present! //TODO add message"); //TODO add message
                return;
            }

            final Material material;

            try {
                material = Material.valueOf(materialName.toUpperCase());
            } catch (final IllegalArgumentException e) {
                sender.sendMessage("no material! //TODO add message"); //TODO add message
                return;
            }

            if (!material.isBlock()) {
                sender.sendMessage("no block! //TODO add message"); //TODO add message
                return;
            }

            final OreGenerator oreGenerator;

            if (args.length == 4) {
                final Optional<OreGenerator> oreGeneratorOptional = service.getOreGenerator(args[3]);
                if (!oreGeneratorOptional.isPresent()) {
                    sender.sendMessage("no ore generator! //TODO add message"); //TODO add message
                    return;
                }

                oreGenerator = oreGeneratorOptional.get();
            } else {
                sender.sendMessage("no ore generator specified, use default! //TODO add message"); //TODO add message
                oreGenerator = service.getDefaultOreGenerator();
            }

            final OreConfig oreConfig = new OreConfigYamlImpl(oreConfigName, material, oreGenerator.getName()); //TODO create OreConfig over service

            worldConfig.addOreConfig(oreConfig);

            service.saveWorldConfig(worldConfig);
            sender.sendMessage("success! //TODO add message"); //TODO add message
        });

        return true;
    }

    @Override //oregen create <world> <name> <material> [<ore-generator>]
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = CustomOreGenerator.getService();

        if (args.length == 1) {
            final String world_name = args[0].toLowerCase();
            Bukkit.getWorlds().stream().map(World::getName).filter(value -> value.toLowerCase().contains(world_name)).forEach(list::add);
            return list;
        }

        if (args.length == 3) {
            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (worldConfig.isPresent()) {
                Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

                if (oreConfig.isPresent())
                    return list;
            }

            final String materialName = args[2].toUpperCase();

            Stream.of(Material.values()).filter(Material::isBlock).map(Enum::toString).filter(value -> value.contains(materialName)).forEach(list::add);

            return list;
        }

        if (args.length == 4) {
            Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (worldConfig.isPresent()) {
                Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

                if (oreConfig.isPresent())
                    return list;
            }

            final String materialName = args[2].toUpperCase();

            try {
                final Material material = Material.valueOf(materialName);

                if (!material.isBlock())
                    return list;

            } catch (IllegalArgumentException e) {
                return list;
            }

            service.getOreGenerators().stream().map(OreGenerator::getName).filter(name -> name.contains(args[3])).forEach(list::add);

            return list;
        }


        return list;
    }

}
