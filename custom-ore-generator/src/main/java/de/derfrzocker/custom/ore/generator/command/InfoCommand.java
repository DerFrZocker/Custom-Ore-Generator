/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
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

import de.derfrzocker.spigot.utils.command.CommandUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand implements TabExecutor {

    private final BaseComponent[] space = new BaseComponent[]{new TextComponent(" ")};

    @NotNull
    private final Plugin plugin;

    public InfoCommand(@NotNull final Plugin plugin) {
        Validate.notNull(plugin, "Plugin cannot be null");

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        CommandUtil.runAsynchronously(sender, plugin, () -> {

            final BaseComponent[] source = buildUrlButton("Source", "https://github.com/DerFrZocker/Custom-Ore-Generator");

            final BaseComponent[] spaceSource = combineBaseComponent(space, source);
            final BaseComponent[] spaceSourceSpace = combineBaseComponent(spaceSource, space);

            sender.sendMessage("--- " + ChatColor.BLUE + "Custom-Ore-Generator" + ChatColor.RESET + " ---");
            sender.spigot().sendMessage(spaceSourceSpace);
            sender.sendMessage("    ");
            sender.sendMessage("      Version: " + ChatColor.BLUE + plugin.getDescription().getVersion());
            sender.sendMessage("      Author: " + ChatColor.BLUE + plugin.getDescription().getAuthors().iterator().next());
            sender.sendMessage("    ");
            sender.sendMessage("--- " + ChatColor.BLUE + "Custom-Ore-Generator" + ChatColor.RESET + " ---");
        });

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        return new ArrayList<>();
    }

    private BaseComponent[] buildUrlButton(String text, String url) {
        final BaseComponent[] begin = new ComponentBuilder("[").color(ChatColor.DARK_RED).create();
        final BaseComponent[] end = new ComponentBuilder("]").color(ChatColor.DARK_RED).create();

        final BaseComponent[] buttons = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', text));
        final BaseComponent[] hoverEventMessage = new ComponentBuilder("Click me").create();
        final HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverEventMessage);
        final ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);

        for (final BaseComponent button : buttons) {
            button.setHoverEvent(hoverEvent);
            button.setClickEvent(clickEvent);
        }

        final BaseComponent[] first = combineBaseComponent(begin, buttons);

        return combineBaseComponent(first, end);
    }

    private static BaseComponent[] combineBaseComponent(@NotNull final BaseComponent[] baseComponents, @NotNull final BaseComponent... baseComponents1) {
        final int firstLength = baseComponents.length;
        final int secondLength = baseComponents1.length;
        final BaseComponent[] result = new BaseComponent[firstLength + secondLength];

        System.arraycopy(baseComponents, 0, result, 0, firstLength);
        System.arraycopy(baseComponents1, 0, result, firstLength, secondLength);

        return result;
    }

}
