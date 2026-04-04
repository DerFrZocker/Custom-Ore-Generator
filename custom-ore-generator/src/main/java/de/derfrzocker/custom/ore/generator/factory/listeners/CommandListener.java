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
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements Listener {

    @NotNull
    private final Plugin plugin;
    @NotNull
    private final Player player;
    @NotNull
    private final Conversation conversation;

    public CommandListener(@NotNull final Plugin plugin, @NotNull final Player player, @NotNull final Conversation conversation) {
        Validate.notNull(plugin, "plugin can not be null");
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
