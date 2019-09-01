package de.derfrzocker.custom.ore.generator.command.set;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

import static de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages.SET_BIOME_NOT_ENOUGH_ARGS;
import static de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages.SET_WORLD_NOT_FOUND;

public class SetBiomeCommand implements TabExecutor {

    @Override //oregen set biome <world> <config_name> <biome> <biome> ...
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 3) {
            SET_BIOME_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(CustomOreGenerator.getInstance(), () -> {
            String world_name = args[0];
            String config_name = args[1];

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

            Set<Biome> biomes = new HashSet<>();

            for (int i = 2; i < args.length; i++) {
                try {
                    biomes.add(Biome.valueOf(args[i].toUpperCase()));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Not Biome! //TODO add message"); //TODO add message
                    return;
                }
            }

            oreConfig.getBiomes().clear();
            oreConfig.getBiomes().addAll(biomes);
            oreConfig.setGeneratedAll(false);

            service.saveWorldConfig(worldConfig);

            sender.sendMessage("success! //TODO add message"); //TODO add message
        });

        return true;
    }

    @Override //oregen set biome <world> <config_name> <biome> <biome> ...
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

        Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

        if (!world.isPresent())
            return list;

        Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

        if (!worldConfig.isPresent())
            return list;

        Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

        if (!oreConfig.isPresent())
            return list;

        Set<Biome> biomes = new HashSet<>();

        for (int i = 2; i < (args.length - 1); i++) {
            try {
                biomes.add(Biome.valueOf(args[i].toUpperCase()));
            } catch (IllegalArgumentException e) {
                return list;
            }
        }

        Set<Biome> biomeSet = Sets.newHashSet(Biome.values());

        biomeSet.removeAll(biomes);

        biomeSet.stream().map(Enum::toString).filter(value -> value.contains(args[args.length - 1])).forEach(list::add);

        return list;
    }

}
