package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.*;
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

public class SetValueCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final CustomOreGeneratorMessages messages;

    public SetValueCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.messages = messages;
    }

    @Override //oregen set value <world> <config_name> <setting> <amount>
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length != 4) {
            messages.COMMAND_SET_VALUE_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, () -> {
            final String worldName = args[0];
            final String configName = args[1];
            final String settingName = args[2];
            final String amount = args[3];

            final World world = Bukkit.getWorld(worldName);

            if (world == null) {
                messages.COMMAND_WORLD_NOT_FOUND.sendMessage(sender, new MessageValue("world", worldName));
                return;
            }

            final CustomOreGeneratorService service = serviceSupplier.get();

            final Optional<WorldConfig> worldConfigOptional = service.getWorldConfig(world.getName());

            if (!worldConfigOptional.isPresent()) {
                messages.COMMAND_ORE_CONFIG_NOT_FOUND.sendMessage(sender, new MessageValue("ore-config", configName));
                return;
            }

            final WorldConfig worldConfig = worldConfigOptional.get();

            final Optional<OreConfig> oreConfigOptional = worldConfig.getOreConfig(configName);

            if (!oreConfigOptional.isPresent()) {
                messages.COMMAND_ORE_CONFIG_NOT_FOUND.sendMessage(sender, new MessageValue("ore-config", configName));
                return;
            }

            final OreConfig oreConfig = oreConfigOptional.get();

            final OreSetting setting = OreSetting.getOreSetting(settingName.toUpperCase());

            if (setting == null) {
                messages.COMMAND_SET_VALUE_SETTING_NOT_FOUND.sendMessage(sender, new MessageValue("setting", settingName));
                return;
            }

            final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(oreConfig.getOreGenerator());

            if (!optionalOreGenerator.isPresent()) {
                messages.COMMAND_ORE_GENERATOR_NOT_FOUND.sendMessage(sender, new MessageValue("ore-generator", oreConfig.getOreGenerator()));
                return;
            }

            final Optional<BlockSelector> optionalBlockSelector = service.getBlockSelector(oreConfig.getBlockSelector());

            if (!optionalBlockSelector.isPresent()) {
                messages.COMMAND_BLOCK_SELECTOR_NOT_FOUND.sendMessage(sender, new MessageValue("block-selector", oreConfig.getBlockSelector()));
                return;
            }

            final OreGenerator generator = optionalOreGenerator.get();
            final BlockSelector blockSelector = optionalBlockSelector.get();

            if (generator.getNeededOreSettings().stream().noneMatch(value -> value == setting) && blockSelector.getNeededOreSettings().stream().noneMatch(value -> value == setting)) {
                messages.COMMAND_SET_VALUE_SETTING_NOT_VALID.sendMessage(sender,
                        new MessageValue("setting", settingName),
                        new MessageValue("ore-generator", generator.getName()),
                        new MessageValue("block-selector", blockSelector.getName())
                );
                return;
            }

            final int value;

            try {
                value = Integer.parseInt(amount);
            } catch (NumberFormatException e) {
                messages.COMMAND_SET_VALUE_VALUE_NOT_VALID.sendMessage(sender, new MessageValue("value", amount));
                return;
            }

            oreConfig.setValue(setting, value);

            service.saveWorldConfig(worldConfig);
            messages.COMMAND_SET_VALUE_SUCCESS.sendMessage(sender, new MessageValue("value", value));
        });

        return true;
    }

    @Override //oregen set value <world> <config_name> <setting> <amount>
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

            final String settingName = args[2].toUpperCase();

            final Optional<OreGenerator> oreGenerator = service.getOreGenerator(oreConfig.get().getOreGenerator());
            oreGenerator.ifPresent(generator -> generator.getNeededOreSettings().stream().map(OreSetting::getName).filter(value -> value.contains(settingName)).forEach(list::add));
            final Optional<BlockSelector> blockSelector = service.getBlockSelector(oreConfig.get().getBlockSelector());
            blockSelector.ifPresent(selector -> selector.getNeededOreSettings().stream().map(OreSetting::getName).filter(value -> value.contains(settingName)).forEach(list::add));

            return list;
        }

        return list;
    }

}
