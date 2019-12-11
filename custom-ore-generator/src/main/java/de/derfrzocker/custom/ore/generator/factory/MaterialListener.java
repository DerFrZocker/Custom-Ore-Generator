package de.derfrzocker.custom.ore.generator.factory;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MaterialListener implements Listener {

    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final Player player;
    @NotNull
    private final OreConfigBuilder oreConfigBuilder;
    @NotNull
    private final Conversation conversation;

    public MaterialListener(@NotNull final JavaPlugin plugin, @NotNull final Player player, @NotNull final OreConfigBuilder oreConfigBuilder, @NotNull final Conversation conversation) {
        Validate.notNull(plugin, "JavaPlugin can not be null");
        Validate.notNull(player, "Player can not be null");
        Validate.notNull(oreConfigBuilder, "OreConfigBuilder can not be null");
        Validate.notNull(conversation, "Conversation can not be null");

        this.plugin = plugin;
        this.player = player;
        this.oreConfigBuilder = oreConfigBuilder;
        this.conversation = conversation;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.conversation.addConversationAbandonedListener(event -> destroy());
    }

    @EventHandler
    public void onPlayerInteract(@NotNull final PlayerInteractEvent event) {
        if (event.getPlayer() != this.player)
            return;

        event.setCancelled(true);

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (this.player.isSneaking()) {
                onBlockShiftLeftClick(event.getClickedBlock());
            } else {
                onBlockLeftClick(event.getClickedBlock());
            }

            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            if (this.player.isSneaking()) {
                onAirShiftLeftClick();
            } else {
                onAirLeftClick();
            }

            return;
        }
    }

    public void onBlockLeftClick(@NotNull final Block block) {
    }

    public void onBlockShiftLeftClick(@NotNull final Block block) {
    }

    public void onAirLeftClick() {
    }

    public void onAirShiftLeftClick() {
    }


    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        if (event.getPlayer() != player)
            return;

        destroy();
    }

    public void destroy() {
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    public OreConfigBuilder getOreConfigBuilder() {
        return oreConfigBuilder;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Player getPlayer() {
        return player;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

}