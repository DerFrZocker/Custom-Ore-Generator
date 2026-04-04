package de.derfrzocker.custom.ore.generator.factory.listeners;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MainMaterialListener extends MaterialListener {


    public MainMaterialListener(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final Player player, @NotNull final OreConfigBuilder oreConfigBuilder, @NotNull final Conversation conversation) {
        super(plugin, serviceSupplier, player, oreConfigBuilder, conversation);
    }

    @Override
    public void onAirLeftClick() {
        getOreConfigBuilder().material(Material.AIR);
        new MessageKey(getPlugin(), "ore-config.factory.material.set").sendMessage(getPlayer(), new MessageValue("material", Material.AIR));
    }

    @Override
    public void onBlockLeftClick(@NotNull Block block) {
        getOreConfigBuilder().material(block.getType());
        getOreConfigBuilder().foundCustomDatas().clear();

        final BlockState blockState = block.getState();
        getServiceSupplier().get().getCustomData().stream().filter(customData -> customData.hasCustomData(blockState)).forEach(customData -> getOreConfigBuilder().setFoundCustomData(customData, customData.getCustomData(blockState)));

        new MessageKey(getPlugin(), "ore-config.factory.material.set").sendMessage(getPlayer(), new MessageValue("material", block.getType()));
    }
}
