package de.derfrzocker.custom.ore.generator.factory.gui.settings;

import de.derfrzocker.spigot.utils.gui.PageSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CustomDatasGuiSettings extends PageSettings {

    public CustomDatasGuiSettings(@NotNull final Plugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public CustomDatasGuiSettings(@NotNull final Plugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public CustomDatasGuiSettings(@NotNull final Plugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
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
    public ItemStack getDeactivatedCustomDataItemStack() {
        return getSection().getItemStack("custom-data.deactivated.item-stack").clone();
    }

    @NotNull
    public ItemStack getActivatedCustomDataItemStack() {
        return getSection().getItemStack("custom-data.activated.item-stack").clone();
    }

    @NotNull
    public ItemStack getNextItemStack() {
        return getSection().getItemStack("next.item-stack").clone();
    }

    public int getNextSlot() {
        return getSection().getInt("next.slot");
    }

}
