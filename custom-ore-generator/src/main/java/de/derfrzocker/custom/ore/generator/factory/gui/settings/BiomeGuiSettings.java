package de.derfrzocker.custom.ore.generator.factory.gui.settings;

import de.derfrzocker.spigot.utils.gui.PageSettings;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BiomeGuiSettings extends PageSettings {
    private final Plugin plugin;

    public BiomeGuiSettings(@NotNull final Plugin plugin, @NotNull final String file) {
        super(plugin, file);
        this.plugin = plugin;
    }

    public BiomeGuiSettings(@NotNull final Plugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
        this.plugin = plugin;
    }

    public BiomeGuiSettings(@NotNull final Plugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
        this.plugin = plugin;
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
    public ItemStack getDeactivatedItemStack() {
        return getSection().getItemStack("deactivate.item-stack").clone();
    }

    @NotNull
    public ItemStack getActivatedItemStack() {
        return getSection().getItemStack("activate.item-stack").clone();
    }

    @NotNull
    public Material getBiomeMaterial(@NotNull final String biome) {
        String value = getSection().getString("biomes." + biome);

        if (value == null) {
            plugin.getLogger().warning("There is no material for the biome " + biome + " using default Material " + Material.STONE);
            return Material.STONE;
        }

        return Material.valueOf(value);
    }

    @NotNull
    public ItemStack getNextItemStack() {
        return getSection().getItemStack("next.item-stack").clone();
    }

    public int getNextSlot() {
        return getSection().getInt("next.slot");
    }

}
