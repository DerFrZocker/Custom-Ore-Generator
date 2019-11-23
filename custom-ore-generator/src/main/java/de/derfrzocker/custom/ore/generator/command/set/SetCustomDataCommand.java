package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.spigot.utils.command.CommandUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SetCustomDataCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final CustomOreGeneratorMessages messages;

    public SetCustomDataCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.messages = messages;
    }

    @Override //oregen set customdata <config_name> <customdata> <value>
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length < 3) {
            messages.COMMAND_SET_CUSTOMDATA_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        CommandUtil.runAsynchronously(sender, javaPlugin, () -> {
            final String configName = args[0];
            final String customDataName = args[1];
            final String customDataValue = buildStrings(args);

            final CustomOreGeneratorService service = serviceSupplier.get();
            final OreConfig oreConfig = OreGenCommand.getOreConfig(configName, service, messages.COMMAND_ORE_CONFIG_NOT_FOUND, sender);

            final Optional<CustomData> customDataOptional = service.getCustomData(customDataName);

            if (!customDataOptional.isPresent()) {
                messages.COMMAND_SET_CUSTOMDATA_NOT_FOUND.sendMessage(sender, new MessageValue("customdata", customDataName));
                return;
            }

            final CustomData customData = customDataOptional.get();

            if (!customData.canApply(oreConfig)) {
                messages.COMMAND_SET_CUSTOMDATA_ORE_CONFIG_NOT_VALID.sendMessage(sender, new MessageValue("customdata", customData.getName()), new MessageValue("ore-config", oreConfig.getName()));
                return;
            }

            final Object data;

            try {
                data = parse(customDataValue, customData.getCustomDataType());
            } catch (IllegalArgumentException e) {
                messages.COMMAND_SET_CUSTOMDATA_VALUE_NOT_VALID.sendMessage(sender, new MessageValue("value", customDataValue));
                return;
            }

            if (!customData.isValidCustomData(data, oreConfig)) {
                messages.COMMAND_SET_CUSTOMDATA_VALUE_NOT_VALID.sendMessage(sender, new MessageValue("value", data));
                return;
            }

            oreConfig.setCustomData(customData, data);
            service.saveOreConfig(oreConfig);
            messages.COMMAND_SET_CUSTOMDATA_SUCCESS.sendMessage(sender);
        });

        return true;
    }

    @Override //oregen set customdata <config_name> <customdata> <value>
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = serviceSupplier.get();

        if (args.length == 1) {
            final String configName = args[0];
            service.getOreConfigs().stream().map(OreConfig::getName).filter(name -> name.contains(configName)).forEach(list::add);
            return list;
        }

        if (args.length == 2) {
            final Optional<OreConfig> oreConfig = service.getOreConfig(args[0]);

            if (!oreConfig.isPresent())
                return list;

            final String customDataName = args[1].toUpperCase();

            service.getCustomData().stream().filter(customData -> customData.canApply(oreConfig.get())).map(CustomData::getName).filter(value -> value.contains(customDataName)).forEach(list::add);

            return list;
        }

        return list;
    }

    private Object parse(final String data, final CustomDataType customDataType) throws IllegalArgumentException {
        switch (customDataType) {
            case STRING:
                return data;
            case INTEGER:
                return Integer.parseInt(data);
            case DOUBLE:
                return Double.parseDouble(data);
            case BOOLEAN:
                return Boolean.parseBoolean(data);
        }

        throw new IllegalArgumentException();
    }

    private String buildStrings(final String[] args) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            if (i != 2) {
                stringBuilder.append(" ");
            }

            stringBuilder.append(args[i]);
        }

        return stringBuilder.toString();
    }

}
