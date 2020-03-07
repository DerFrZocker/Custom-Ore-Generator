package de.derfrzocker.custom.ore.generator.gui.settings;

import de.derfrzocker.spigot.utils.gui.BasicSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class StartGuiSettings extends BasicSettings {

    public StartGuiSettings(@NotNull JavaPlugin plugin, @NotNull String file) {
        super(plugin, file);
    }

    public StartGuiSettings(@NotNull JavaPlugin plugin, @NotNull String file, boolean copy) {
        super(plugin, file, copy);
    }

    public StartGuiSettings(@NotNull JavaPlugin plugin, @NotNull Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
    }

    @NotNull
    public ItemStack getWorldConfigItemStack() {
        return getSection().getItemStack("world-config.item-stack").clone();
    }

    public int getWorldConfigSlot() {
        return getSection().getInt("world-config.slot");
    }

    @NotNull
    public ItemStack getOreConfigItemStack() {
        return getSection().getItemStack("ore-config.item-stack").clone();
    }

    public int getOreConfigSlot() {
        return getSection().getInt("ore-config.slot");
    }

}
