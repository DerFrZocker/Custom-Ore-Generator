package de.derfrzocker.custom.ore.generator.gui.settings;

import de.derfrzocker.spigot.utils.gui.PageSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OreConfigsGuiSettings extends PageSettings {


    public OreConfigsGuiSettings(@NotNull JavaPlugin plugin, @NotNull String file) {
        super(plugin, file);
    }

    public OreConfigsGuiSettings(@NotNull JavaPlugin plugin, @NotNull String file, boolean copy) {
        super(plugin, file, copy);
    }

    public OreConfigsGuiSettings(@NotNull JavaPlugin plugin, @NotNull Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
    }

    @NotNull
    public ItemStack getOreConfigItemStack() {
        return getSection().getItemStack("ore-config.item-stack").clone();
    }

    @NotNull
    public ItemStack getBackItemStack() {
        return getSection().getItemStack("back.item-stack").clone();
    }

    public int getBackSlot() {
        return getSection().getInt("back.slot");
    }

}
