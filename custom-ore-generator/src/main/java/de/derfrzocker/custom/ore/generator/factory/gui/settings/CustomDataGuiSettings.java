package de.derfrzocker.custom.ore.generator.factory.gui.settings;

import de.derfrzocker.spigot.utils.gui.BasicSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CustomDataGuiSettings extends BasicSettings {

    public CustomDataGuiSettings(@NotNull final Plugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public CustomDataGuiSettings(@NotNull final Plugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public CustomDataGuiSettings(@NotNull final Plugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
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
    public ItemStack getBackItemStack() {
        return getSection().getItemStack("back.item-stack").clone();
    }

    public int getBackSlot() {
        return getSection().getInt("back.slot");
    }

    @NotNull
    public ItemStack getCurrentValueItemStack() {
        return getSection().getItemStack("value.current.item-stack").clone();
    }

    public int getCurrentValueSlot() {
        return getSection().getInt("value.current.slot");
    }

    @NotNull
    public ItemStack getFoundValueItemStack() {
        return getSection().getItemStack("value.found.item-stack").clone();
    }

    public int getFoundValueSlot() {
        return getSection().getInt("value.found.slot");
    }

    @NotNull
    public ItemStack getSetValueItemStack() {
        return getSection().getItemStack("value.set.item-stack").clone();
    }

    public int getSetValueSlot() {
        return getSection().getInt("value.set.slot");
    }

    @NotNull
    public ItemStack getRemoveCustomDataItemStack() {
        return getSection().getItemStack("remove.item-stack").clone();
    }

    public int getRemoveCustomDataSlot() {
        return getSection().getInt("remove.set.slot");
    }

}
