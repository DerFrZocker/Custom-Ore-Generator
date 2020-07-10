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

package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.spigot.utils.Version;
import de.derfrzocker.spigot.utils.command.CommandUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CreateCommand implements TabExecutor {

    private final static Pattern ORE_CONFIG_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");

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

    @Override //oregen create <name> <material> [<ore-generator>] [<block-selector>]
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length == 0 && sender instanceof Player && Version.v1_14_R1.isNewerOrSameVersion(Version.getCurrent())) {
            sender.sendMessage("   ");
            sender.sendMessage(ChatColor.DARK_RED + "This feature is in development!");
            sender.sendMessage(ChatColor.DARK_RED + "It is lacking some feature, information and has some bugs!");
            sender.sendMessage(ChatColor.DARK_RED + "I would highly appreciate it if you send me feedback of this feature.");
            sender.sendMessage(ChatColor.DARK_RED + "e.g. Which part you like, which part you don't like.");
            TextComponent textComponent = new TextComponent(ChatColor.DARK_PURPLE + "discord");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("<Click me>")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://discord.derfrzocker.de"));
            sender.spigot().sendMessage(new TextComponent(ChatColor.DARK_RED + "You can send me feedback by joining my "), textComponent, new TextComponent(ChatColor.DARK_RED + " server"));

            sender.sendMessage(ChatColor.DARK_RED + "Or by sending me a private message over: ");

            textComponent = new TextComponent(ChatColor.DARK_PURPLE + "    SpigotMC" + ChatColor.DARK_RED + " (english)");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("<Click me>")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/conversations/add?to=DerFrZocker"));
            sender.spigot().sendMessage(textComponent);

            textComponent = new TextComponent(ChatColor.DARK_PURPLE + "    minecraft-server.eu" + ChatColor.DARK_RED + " (german)");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("<Click me>")}));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft-server.eu/forum/conversations/add?to=DerFrZocker"));
            sender.spigot().sendMessage(textComponent);

            sender.spigot().sendMessage(new TextComponent(ChatColor.DARK_RED + "    Discord:" + ChatColor.DARK_PURPLE + " DerFrZocker#3723"));

            sender.sendMessage("   ");

            final OreConfigFactory oreConfigFactory = new OreConfigFactory(this.javaPlugin, this.serviceSupplier, (Player) sender);

            oreConfigFactory.setName();

            return true;
        }

        if (args.length < 2) {
            messages.COMMAND_CREATE_NOT_ENOUGH_ARGS.sendMessage(sender);
            return true;
        }

        CommandUtil.runAsynchronously(sender, javaPlugin, () -> {
            final String configName = args[0];
            final String materialName = args[1];


            final CustomOreGeneratorService service = serviceSupplier.get();
            final Optional<OreConfig> oreConfigOptional = service.getOreConfig(configName);

            if (oreConfigOptional.isPresent()) {
                messages.COMMAND_CREATE_ALREADY_EXISTS.sendMessage(sender, new MessageValue("ore-config", oreConfigOptional.get().getName()));
                return;
            }

            if (!ORE_CONFIG_NAME_PATTERN.matcher(configName).matches()) {
                messages.COMMAND_CREATE_NAME_NOT_VALID.sendMessage(sender, new MessageValue("ore-config", configName));
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
            if (args.length >= 3) {
                final Optional<OreGenerator> oreGeneratorOptional = service.getOreGenerator(args[2]);
                if (!oreGeneratorOptional.isPresent()) {
                    messages.COMMAND_ORE_GENERATOR_NOT_FOUND.sendMessage(sender, new MessageValue("ore-generator", args[2]));
                    return;
                }

                oreGenerator = oreGeneratorOptional.get();
            } else {
                oreGenerator = service.getDefaultOreGenerator();
                Validate.notNull(oreGenerator, "OreGenerator should not be null");
                messages.COMMAND_CREATE_ORE_GENERATOR_NOT_SPECIFIED.sendMessage(sender, new MessageValue("ore-generator", oreGenerator.getName()));
            }

            final BlockSelector blockSelector;
            if (args.length >= 4) {
                final Optional<BlockSelector> blockSelectorOptional = service.getBlockSelector(args[3]);
                if (!blockSelectorOptional.isPresent()) {
                    messages.COMMAND_BLOCK_SELECTOR_NOT_FOUND.sendMessage(sender, new MessageValue("ore-generator", args[3]));
                    return;
                }

                blockSelector = blockSelectorOptional.get();
            } else {
                blockSelector = service.getDefaultBlockSelector();
                Validate.notNull(blockSelector, "BlockSelector should not be null");
                messages.COMMAND_CREATE_BLOCK_SELECTOR_NOT_SPECIFIED.sendMessage(sender, new MessageValue("block-selector", blockSelector.getName()));
            }

            final OreConfig oreConfig = service.createOreConfig(configName, material, oreGenerator, blockSelector);

            service.saveOreConfig(oreConfig);

            messages.COMMAND_CREATE_SUCCESS.sendMessage(sender,
                    new MessageValue("material", material),
                    new MessageValue("ore-config", oreConfig.getName()),
                    new MessageValue("ore-generator", oreGenerator.getName()),
                    new MessageValue("block-selector", blockSelector.getName())
            );
        });

        return true;
    }

    @Override //oregen create <name> <material> [<ore-generator>] [<block-selector>]
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        final List<String> list = new ArrayList<>();
        final CustomOreGeneratorService service = serviceSupplier.get();

        if (args.length == 2) {

            final Optional<OreConfig> oreConfig = service.getOreConfig(args[0]);

            if (oreConfig.isPresent())
                return list;

            final String materialName = args[1].toUpperCase();

            Stream.of(Material.values()).filter(Material::isBlock).map(Enum::toString).filter(value -> value.contains(materialName)).forEach(list::add);

            return list;
        }

        if (args.length == 3) {

            final Optional<OreConfig> oreConfig = service.getOreConfig(args[0]);

            if (oreConfig.isPresent())
                return list;

            final String materialName = args[1].toUpperCase();

            try {
                final Material material = Material.valueOf(materialName);

                if (!material.isBlock())
                    return list;

            } catch (IllegalArgumentException e) {
                return list;
            }

            final String oreGeneratorName = args[2].toUpperCase();
            service.getOreGenerators().stream().map(OreGenerator::getName).filter(name -> name.contains(oreGeneratorName)).forEach(list::add);

            return list;
        }

        if (args.length == 4) {
            final Optional<OreConfig> oreConfig = service.getOreConfig(args[0]);

            if (oreConfig.isPresent())
                return list;

            final String materialName = args[1].toUpperCase();

            try {
                final Material material = Material.valueOf(materialName);

                if (!material.isBlock())
                    return list;

            } catch (IllegalArgumentException e) {
                return list;
            }

            final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(args[2].toUpperCase());

            if (!optionalOreGenerator.isPresent())
                return list;

            final String blockSelectorName = args[3].toUpperCase();
            service.getBlockSelectors().stream().map(BlockSelector::getName).filter(name -> name.contains(blockSelectorName)).forEach(list::add);

            return list;
        }

        return list;
    }

}
