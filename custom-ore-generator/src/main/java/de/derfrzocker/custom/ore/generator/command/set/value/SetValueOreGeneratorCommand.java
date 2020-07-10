/*
 * MIT License
 *
 * Copyright (c) 2019 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.custom.ore.generator.command.set.value;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
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

public class SetValueOreGeneratorCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final CustomOreGeneratorMessages messages;

    public SetValueOreGeneratorCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.messages = messages;
    }

    @Override //oregen set value <config_name> <setting> <amount>
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length != 3) {
            messages.COMMAND_SET_VALUE_ORE_GENERATOR_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        CommandUtil.runAsynchronously(sender, javaPlugin, () -> {
            final String configName = args[0];
            final String settingName = args[1];
            final String amount = args[2];

            final CustomOreGeneratorService service = serviceSupplier.get();
            final OreConfig oreConfig = OreGenCommand.getOreConfig(configName, service, messages.COMMAND_ORE_CONFIG_NOT_FOUND, sender);

            final OreSetting setting = OreSetting.getOreSetting(settingName.toUpperCase());

            if (setting == null) {
                messages.COMMAND_SET_VALUE_ORE_GENERATOR_SETTING_NOT_FOUND.sendMessage(sender, new MessageValue("setting", settingName));
                return;
            }

            final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(oreConfig.getOreGenerator());

            if (!optionalOreGenerator.isPresent()) {
                messages.COMMAND_ORE_GENERATOR_NOT_FOUND.sendMessage(sender, new MessageValue("ore-generator", oreConfig.getOreGenerator()));
                return;
            }

            final OreGenerator generator = optionalOreGenerator.get();

            if (generator.getNeededOreSettings().stream().noneMatch(value -> value == setting)) {
                messages.COMMAND_SET_VALUE_ORE_GENERATOR_SETTING_NOT_VALID.sendMessage(sender,
                        new MessageValue("setting", settingName),
                        new MessageValue("ore-generator", generator.getName())
                );
                return;
            }

            final double value;

            try {
                value = Double.parseDouble(amount);
            } catch (NumberFormatException e) {
                messages.COMMAND_SET_VALUE_ORE_GENERATOR_VALUE_NOT_VALID.sendMessage(sender, new MessageValue("value", amount));
                return;
            }

            oreConfig.getOreGeneratorOreSettings().setValue(setting, value);

            service.saveOreConfig(oreConfig);
            messages.COMMAND_SET_VALUE_ORE_GENERATOR_SUCCESS.sendMessage(sender, new MessageValue("value", value));
        });

        return true;
    }

    @Override //oregen set value <config_name> <setting> <amount>
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

            final String settingName = args[1].toUpperCase();

            final Optional<OreGenerator> oreGenerator = service.getOreGenerator(oreConfig.get().getOreGenerator());
            oreGenerator.ifPresent(generator -> generator.getNeededOreSettings().stream().map(OreSetting::getName).filter(value -> value.contains(settingName)).forEach(list::add));

            return list;
        }

        return list;
    }

}
