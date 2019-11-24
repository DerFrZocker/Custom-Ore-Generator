package de.derfrzocker.custom.ore.generator.command.set;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.spigot.utils.command.CommandUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class SetSelectMaterialCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final CustomOreGeneratorMessages messages;

    public SetSelectMaterialCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.messages = messages;
    }

    @Override //oregen set select-material <config_name> <material> <material> ...
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length < 2) {
            messages.COMMAND_SET_REPLACE_MATERIAL_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        CommandUtil.runAsynchronously(sender, javaPlugin, () -> {
            final String configName = args[0];

            final CustomOreGeneratorService service = serviceSupplier.get();
            final OreConfig oreConfig = OreGenCommand.getOreConfig(configName, service, messages.COMMAND_ORE_CONFIG_NOT_FOUND, sender);
            final Set<Material> materials = new HashSet<>();

            for (int i = 1; i < args.length; i++) {
                try {
                    final Material material = Material.valueOf(args[i].toUpperCase());
                    if (!material.isBlock()) {
                        messages.COMMAND_MATERIAL_NO_BLOCK.sendMessage(sender, new MessageValue("material", args[i]));
                        return;
                    }

                    materials.add(material);
                } catch (IllegalArgumentException e) {
                    messages.COMMAND_MATERIAL_NOT_FOUND.sendMessage(sender, new MessageValue("material", args[i]));
                    return;
                }
            }

            oreConfig.getSelectMaterials().forEach(oreConfig::removeSelectMaterial);
            materials.forEach(oreConfig::addSelectMaterial);

            service.saveOreConfig(oreConfig);
            messages.COMMAND_SET_SELECT_MATERIAL_SUCCESS.sendMessage(sender);
        });

        return true;
    }

    @Nullable
    @Override //oregen set select-material <config_name> <material> <material> ...
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = serviceSupplier.get();

        if (args.length == 1) {
            final String configName = args[0];
            service.getOreConfigs().stream().map(OreConfig::getName).filter(name -> name.contains(configName)).forEach(list::add);
            return list;
        }

        final Optional<OreConfig> oreConfig = service.getOreConfig(args[0]);

        if (!oreConfig.isPresent())
            return list;

        final Set<Material> materials = new HashSet<>();

        for (int i = 1; i < (args.length - 1); i++) {
            try {
                final Material material = Material.valueOf(args[i].toUpperCase());
                if (!material.isBlock()) {
                    return list;
                }

                materials.add(material);
            } catch (IllegalArgumentException e) {
                return list;
            }
        }

        final Set<Material> materialSet = Sets.newHashSet(Material.values());

        materialSet.removeAll(materials);

        materialSet.stream().filter(Material::isBlock).map(Enum::toString).filter(value -> value.contains(args[args.length - 1])).forEach(list::add);

        return list;
    }

}
