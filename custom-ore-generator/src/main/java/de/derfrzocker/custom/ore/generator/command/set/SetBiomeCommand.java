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

package de.derfrzocker.custom.ore.generator.command.set;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.spigot.utils.command.CommandUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class SetBiomeCommand implements TabExecutor {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final CustomOreGeneratorMessages messages;

    public SetBiomeCommand(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final CustomOreGeneratorMessages messages) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(messages, "CustomOreGeneratorMessages can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.messages = messages;
    }

    @Override //oregen set biome <config_name> <biome> <biome> ...
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length < 2) {
            messages.COMMAND_SET_BIOME_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        CommandUtil.runAsynchronously(sender, javaPlugin, () -> {
            final String configName = args[0];

            final CustomOreGeneratorService service = serviceSupplier.get();
            final OreConfig oreConfig = OreGenCommand.getOreConfig(configName, service, messages.COMMAND_ORE_CONFIG_NOT_FOUND, sender);
            final Set<Biome> biomes = new HashSet<>();

            for (int i = 2; i < args.length; i++) {
                try {
                    biomes.add(Biome.valueOf(args[i].toUpperCase()));
                } catch (IllegalArgumentException e) {
                    messages.COMMAND_BIOME_NOT_FOUND.sendMessage(sender, new MessageValue("biome", args[i]));
                    return;
                }
            }

            oreConfig.getBiomes().forEach(oreConfig::removeBiome);
            biomes.forEach(oreConfig::addBiome);
            oreConfig.setGeneratedAll(false);

            service.saveOreConfig(oreConfig);
            messages.COMMAND_SET_BIOME_SUCCESS.sendMessage(sender);
        });

        return true;
    }

    @Override //oregen set biome <config_name> <biome> <biome> ...
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

        final Set<Biome> biomes = new HashSet<>();

        for (int i = 1; i < (args.length - 1); i++) {
            try {
                biomes.add(Biome.valueOf(args[i].toUpperCase()));
            } catch (IllegalArgumentException e) {
                return list;
            }
        }

        final Set<Biome> biomeSet = Sets.newHashSet(Biome.values());

        biomeSet.removeAll(biomes);

        biomeSet.stream().map(Enum::toString).filter(value -> value.contains(args[args.length - 1])).forEach(list::add);

        return list;
    }

}
