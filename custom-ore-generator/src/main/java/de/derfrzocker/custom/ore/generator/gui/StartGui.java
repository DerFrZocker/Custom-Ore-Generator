package de.derfrzocker.custom.ore.generator.gui;

import de.derfrzocker.custom.ore.generator.gui.settings.StartGuiSettings;
import de.derfrzocker.spigot.utils.gui.BasicGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class StartGui extends BasicGui {

    public StartGui(@NotNull final JavaPlugin plugin, @NotNull final StartGuiSettings settings) {
        super(plugin, settings);

        addDecorations();
        addItem(settings.getOreConfigSlot(), MessageUtil.replaceItemStack(plugin, settings.getOreConfigItemStack()), event -> {
            //TODO open other inventory
        });
        addItem(settings.getWorldConfigSlot(), MessageUtil.replaceItemStack(plugin, settings.getWorldConfigItemStack()), event -> {
            //TODO open other inventory
        });
    }

}
