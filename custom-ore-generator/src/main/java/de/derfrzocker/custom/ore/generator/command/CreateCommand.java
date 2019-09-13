package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.impl.OreConfigYamlImpl;
import de.derfrzocker.spigot.utils.message.MessageValue;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

import static de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages.*;

@RequiredArgsConstructor
public class CreateCommand implements TabExecutor {

    @NonNull
    private final CustomOreGenerator customOreGenerator;

    @Override //oregen create <world> <name> <material> [<ore-generator>]
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            COMMAND_CREATE_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(customOreGenerator, () -> {
            final String worldName = args[0];
            final String oreConfigName = args[1];
            final String materialName = args[2];

            final World world = Bukkit.getWorld(worldName);

            if (world == null) {
                COMMAND_WORLD_NOT_FOUND.sendMessage(sender, new MessageValue("world", worldName));
                return;
            }

            final CustomOreGeneratorService service = CustomOreGenerator.getService();

            final Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

            final WorldConfig worldConfig = worldConfigOptional.orElseGet(() -> service.createWorldConfig(world));

            final Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(oreConfigName);

            if (oreConfigOptional.isPresent()) {
                COMMAND_CREATE_ALREADY_EXISTS.sendMessage(sender, new MessageValue("ore-config", oreConfigOptional.get().getName()));
                return;
            }

            final Material material;

            try {
                material = Material.valueOf(materialName.toUpperCase());
            } catch (final IllegalArgumentException e) {
                COMMAND_MATERIAL_NOT_FOUND.sendMessage(sender, new MessageValue("material", materialName));
                return;
            }

            if (!material.isBlock()) {
                COMMAND_MATERIAL_NO_BLOCK.sendMessage(sender, new MessageValue("material", materialName));
                return;
            }

            final OreGenerator oreGenerator;

            if (args.length == 4) {
                final Optional<OreGenerator> oreGeneratorOptional = service.getOreGenerator(args[3]);
                if (!oreGeneratorOptional.isPresent()) {
                    COMMAND_ORE_GENERATOR_NOT_FOUND.sendMessage(sender, new MessageValue("ore-generator", args[3]));
                    return;
                }

                oreGenerator = oreGeneratorOptional.get();
            } else {
                oreGenerator = service.getDefaultOreGenerator();
                COMMAND_CREATE_ORE_GENERATOR_NOT_SPECIFIED.sendMessage(sender, new MessageValue("ore-generator", oreGenerator.getName()));
            }

            final OreConfig oreConfig = new OreConfigYamlImpl(oreConfigName, material, oreGenerator.getName()); //TODO create OreConfig over service

            worldConfig.addOreConfig(oreConfig);

            service.saveWorldConfig(worldConfig);
            COMMAND_CREATE_SUCCESS.sendMessage(sender,
                    new MessageValue("world", world.getName()),
                    new MessageValue("material", material),
                    new MessageValue("ore-config", oreConfig.getName()),
                    new MessageValue("ore-generator", oreGenerator.getName()));
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
