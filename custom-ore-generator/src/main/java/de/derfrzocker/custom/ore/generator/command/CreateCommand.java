package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.stream.Stream;

public class CreateCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final CustomOreGeneratorMessages messages;

    public CreateCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.messages = messages;
    }

    @Override //oregen create <world> <name> <material> [<ore-generator>] [<block-selector>]
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length < 3) {
            messages.COMMAND_CREATE_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, () -> {
            final String worldName = args[0];
            final String oreConfigName = args[1];
            final String materialName = args[2];

            final World world = Bukkit.getWorld(worldName);

            if (world == null) {
                messages.COMMAND_WORLD_NOT_FOUND.sendMessage(sender, new MessageValue("world", worldName));
                return;
            }

            final CustomOreGeneratorService service = serviceSupplier.get();

            final Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

            final WorldConfig worldConfig = worldConfigOptional.orElseGet(() -> service.createWorldConfig(world));

            final Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(oreConfigName);

            if (oreConfigOptional.isPresent()) {
                messages.COMMAND_CREATE_ALREADY_EXISTS.sendMessage(sender, new MessageValue("ore-config", oreConfigOptional.get().getName()));
                return;
            }

            final Material material;

            try {
                material = Material.valueOf(materialName.toUpperCase());
            } catch (final IllegalArgumentException e) {
                messages.COMMAND_MATERIAL_NOT_FOUND.sendMessage(sender, new MessageValue("material", materialName));
                return;
            }

            if (!material.isBlock()) {
                messages.COMMAND_MATERIAL_NO_BLOCK.sendMessage(sender, new MessageValue("material", materialName));
                return;
            }

            final OreGenerator oreGenerator;
            if (args.length == 4) {
                final Optional<OreGenerator> oreGeneratorOptional = service.getOreGenerator(args[3]);
                if (!oreGeneratorOptional.isPresent()) {
                    messages.COMMAND_ORE_GENERATOR_NOT_FOUND.sendMessage(sender, new MessageValue("ore-generator", args[3]));
                    return;
                }

                oreGenerator = oreGeneratorOptional.get();
            } else {
                oreGenerator = service.getDefaultOreGenerator();
                Validate.notNull(oreGenerator, "OreGenerator should not be null");
                messages.COMMAND_CREATE_ORE_GENERATOR_NOT_SPECIFIED.sendMessage(sender, new MessageValue("ore-generator", oreGenerator.getName()));
            }

            final BlockSelector blockSelector;
            if (args.length == 5) {
                final Optional<BlockSelector> blockSelectorOptional = service.getBlockSelector(args[4]);
                if (!blockSelectorOptional.isPresent()) {
                    messages.COMMAND_BLOCK_SELECTOR_NOT_FOUND.sendMessage(sender, new MessageValue("ore-generator", args[4]));
                    return;
                }

                blockSelector = blockSelectorOptional.get();
            } else {
                blockSelector = service.getDefaultBlockSelector();
                Validate.notNull(blockSelector, "BlockSelector should not be null");
                messages.COMMAND_CREATE_BLOCK_SELECTOR_NOT_SPECIFIED.sendMessage(sender, new MessageValue("block-selector", blockSelector.getName()));
            }

            final OreConfig oreConfig = service.createOreConfig(oreConfigName, material, oreGenerator, blockSelector);

            worldConfig.addOreConfig(oreConfig);

            service.saveWorldConfig(worldConfig);
            messages.COMMAND_CREATE_SUCCESS.sendMessage(sender,
                    new MessageValue("world", world.getName()),
                    new MessageValue("material", material),
                    new MessageValue("ore-config", oreConfig.getName()),
                    new MessageValue("ore-generator", oreGenerator.getName()),
                    new MessageValue("block-selector", blockSelector.getName())
            );
        });

        return true;
    }

    @Override //oregen create <world> <name> <material> [<ore-generator>] [<block-selector>]
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = serviceSupplier.get();

        if (args.length == 1) {
            final String world_name = args[0].toLowerCase();
            Bukkit.getWorlds().stream().map(World::getName).filter(value -> value.toLowerCase().contains(world_name)).forEach(list::add);
            return list;
        }

        if (args.length == 3) {
            final Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            final Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (worldConfig.isPresent()) {
                final Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

                if (oreConfig.isPresent())
                    return list;
            }

            final String materialName = args[2].toUpperCase();

            Stream.of(Material.values()).filter(Material::isBlock).map(Enum::toString).filter(value -> value.contains(materialName)).forEach(list::add);

            return list;
        }

        if (args.length == 4) {
            final Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            final Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (worldConfig.isPresent()) {
                final Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

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

            final String oreGeneratorName = args[3].toUpperCase();
            service.getOreGenerators().stream().map(OreGenerator::getName).filter(name -> name.contains(oreGeneratorName)).forEach(list::add);

            return list;
        }

        if (args.length == 5) {
            final Optional<World> world = Bukkit.getWorlds().stream().filter(value -> value.getName().equalsIgnoreCase(args[0])).findAny();

            if (!world.isPresent())
                return list;

            final Optional<WorldConfig> worldConfig = service.getWorldConfig(world.get().getName());

            if (worldConfig.isPresent()) {
                final Optional<OreConfig> oreConfig = worldConfig.get().getOreConfig(args[1]);

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

            final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(args[3].toUpperCase());

            if (!optionalOreGenerator.isPresent())
                return list;

            final String blockSelectorName = args[4].toUpperCase();
            service.getBlockSelectors().stream().map(BlockSelector::getName).filter(name -> name.contains(blockSelectorName)).forEach(list::add);

            return list;
        }

        return list;
    }

}
