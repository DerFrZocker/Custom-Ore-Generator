package de.derfrzocker.custom.ore.generator.factory;

import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainMaterialListener extends MaterialListener {


    public MainMaterialListener(@NotNull final JavaPlugin plugin, @NotNull final Player player, @NotNull final OreConfigBuilder oreConfigBuilder, @NotNull final Conversation conversation) {
        super(plugin, player, oreConfigBuilder, conversation);
    }

    @Override
    public void onAirLeftClick() {
        getOreConfigBuilder().material(Material.AIR);
        new MessageKey(getPlugin(), "ore-config.factory.material.set").sendMessage(getPlayer(), new MessageValue("material", Material.AIR));
    }

    @Override
    public void onBlockLeftClick(@NotNull Block block) {
        getOreConfigBuilder().material(block.getType());
        //TODO check for custom data
        new MessageKey(getPlugin(), "ore-config.factory.material.set").sendMessage(getPlayer(), new MessageValue("material", block.getType()));
    }
}
