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

package de.derfrzocker.custom.ore.generator.factory.listeners;

import de.derfrzocker.spigot.utils.message.MessageKey;
import org.apache.commons.lang.Validate;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements Listener {

    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final Player player;
    @NotNull
    private final Conversation conversation;

    public CommandListener(@NotNull final JavaPlugin plugin, @NotNull final Player player, @NotNull final Conversation conversation) {
        Validate.notNull(plugin, "JavaPlugin can not be null");
        Validate.notNull(player, "Player can not be null");
        Validate.notNull(conversation, "Conversation can not be null");

        this.plugin = plugin;
        this.player = player;
        this.conversation = conversation;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.conversation.addConversationAbandonedListener(event -> destroy());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull final PlayerCommandPreprocessEvent event) {
        if (event.getPlayer() != player)
            return;

        new MessageKey(plugin, "ore-config.factory.command-blocked").sendMessage(player);

        event.setCancelled(true);
    }

    @EventHandler
    public void onTabComplete(@NotNull final TabCompleteEvent event) {
        if (event.getSender() != player)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        if (event.getPlayer() != player)
            return;

        destroy();
    }

    public void destroy() {
        PlayerCommandPreprocessEvent.getHandlerList().unregister(this);
        TabCompleteEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

}
