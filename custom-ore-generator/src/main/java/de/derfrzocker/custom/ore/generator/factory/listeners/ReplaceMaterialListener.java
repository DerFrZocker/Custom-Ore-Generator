package de.derfrzocker.custom.ore.generator.factory.listeners;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ReplaceMaterialListener extends MaterialListener {

    private final MessageKey alreadyAdd;
    private final MessageKey add;
    private final MessageKey notAdd;
    private final MessageKey remove;

    public ReplaceMaterialListener(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final Player player, @NotNull final OreConfigBuilder oreConfigBuilder, @NotNull final Conversation conversation) {
        super(plugin, serviceSupplier, player, oreConfigBuilder, conversation);

        alreadyAdd = new MessageKey(plugin, "ore-config.factory.replace-material.already-add");
        add = new MessageKey(plugin, "ore-config.factory.replace-material.add");
        notAdd = new MessageKey(plugin, "ore-config.factory.replace-material.not-add");
        remove = new MessageKey(plugin, "ore-config.factory.replace-material.remove");
    }

    @Override
    public void onAirLeftClick() {
        final OreConfigBuilder oreConfigBuilder = getOreConfigBuilder();

        if (oreConfigBuilder.containsReplaceMaterial(Material.AIR)) {
            alreadyAdd.sendMessage(getPlayer(), new MessageValue("material", Material.AIR));
            return;
        }

        oreConfigBuilder.addReplaceMaterial(Material.AIR);
        add.sendMessage(getPlayer(), new MessageValue("material", Material.AIR));
    }

    @Override
    public void onAirShiftLeftClick() {
        final OreConfigBuilder oreConfigBuilder = getOreConfigBuilder();

        if (!oreConfigBuilder.containsReplaceMaterial(Material.AIR)) {
            notAdd.sendMessage(getPlayer(), new MessageValue("material", Material.AIR));
            return;
        }

        oreConfigBuilder.removeReplaceMaterial(Material.AIR);
        remove.sendMessage(getPlayer(), new MessageValue("material", Material.AIR));
    }

    @Override
    public void onBlockLeftClick(@NotNull final Block block) {
        final Material material = block.getType();
        final OreConfigBuilder oreConfigBuilder = getOreConfigBuilder();

        if (oreConfigBuilder.containsReplaceMaterial(material)) {
            alreadyAdd.sendMessage(getPlayer(), new MessageValue("material", material));
            return;
        }

        oreConfigBuilder.addReplaceMaterial(material);
        add.sendMessage(getPlayer(), new MessageValue("material", material));
    }

    @Override
    public void onBlockShiftLeftClick(@NotNull Block block) {
        final Material material = block.getType();
        final OreConfigBuilder oreConfigBuilder = getOreConfigBuilder();

        if (!oreConfigBuilder.containsReplaceMaterial(material)) {
            notAdd.sendMessage(getPlayer(), new MessageValue("material", material));
            return;
        }

        oreConfigBuilder.removeReplaceMaterial(material);
        remove.sendMessage(getPlayer(), new MessageValue("material", material));
    }

}
