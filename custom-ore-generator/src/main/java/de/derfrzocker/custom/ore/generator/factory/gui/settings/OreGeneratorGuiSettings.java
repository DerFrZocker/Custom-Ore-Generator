package de.derfrzocker.custom.ore.generator.factory.gui.settings;

import de.derfrzocker.spigot.utils.gui.PageSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OreGeneratorGuiSettings extends PageSettings {

    public OreGeneratorGuiSettings(@NotNull final Plugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public OreGeneratorGuiSettings(@NotNull final Plugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public OreGeneratorGuiSettings(@NotNull final Plugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
    }

    @NotNull
    public ItemStack getMenuItemStack() {
        return getSection().getItemStack("menu.item-stack").clone();
    }

    public int getMenuSlot() {
        return getSection().getInt("menu.slot");
    }

    @NotNull
    public ItemStack getAbortItemStack() {
        return getSection().getItemStack("abort.item-stack").clone();
    }

    public int getAbortSlot() {
        return getSection().getInt("abort.slot");
    }

    @NotNull
    public ItemStack getDefaultOreGeneratorItemStack() {
        return getSection().getItemStack("default-ore-generator.item-stack").clone();
    }

    public int getDefaultOreGeneratorSlot() {
        return getSection().getInt("default-ore-generator.slot");
    }

    @NotNull
    public ItemStack getOreGeneratorItemStack() {
        return getSection().getItemStack("ore-generator.item-stack").clone();
    }

}
