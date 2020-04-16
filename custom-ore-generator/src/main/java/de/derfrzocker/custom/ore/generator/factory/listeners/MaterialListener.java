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

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
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

import java.util.function.Supplier;

public class MaterialListener implements Listener {

    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final Player player;
    @NotNull
    private final OreConfigBuilder oreConfigBuilder;
    @NotNull
    private final Conversation conversation;

    public MaterialListener(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final Player player, @NotNull final OreConfigBuilder oreConfigBuilder, @NotNull final Conversation conversation) {
        Validate.notNull(plugin, "JavaPlugin can not be null");
        Validate.notNull(serviceSupplier, "Service Supplier can not be null");
        Validate.notNull(player, "Player can not be null");
        Validate.notNull(oreConfigBuilder, "OreConfigBuilder can not be null");
        Validate.notNull(conversation, "Conversation can not be null");

        this.plugin = plugin;
        this.serviceSupplier = serviceSupplier;
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

    public Supplier<CustomOreGeneratorService> getServiceSupplier() {
        return serviceSupplier;
    }

}