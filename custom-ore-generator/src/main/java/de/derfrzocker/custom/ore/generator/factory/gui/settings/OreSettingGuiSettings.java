package de.derfrzocker.custom.ore.generator.factory.gui.settings;

import de.derfrzocker.spigot.utils.gui.BasicSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class OreSettingGuiSettings extends BasicSettings {
    public OreSettingGuiSettings(@NotNull final Plugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public OreSettingGuiSettings(@NotNull final Plugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public OreSettingGuiSettings(@NotNull final Plugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
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
    public ItemStack getOreSettingItemStack() {
        return getSection().getItemStack("ore-setting.item-stack").clone();
    }

    public int getOreSettingSlot() {
        return getSection().getInt("ore-setting.slot");
    }

    @NotNull
    public Set<ItemStackValues> getItemStackValues() {
        final Set<ItemStackValues> set = new HashSet<>();

        getSection().getConfigurationSection("items").
                getKeys(false).stream().
                map(value -> getSection().getConfigurationSection("items." + value)).
                map(value -> new ItemStackValues(value.getInt("slot", 0), value.getDouble("value", 0), value.getItemStack("item-stack").clone())).
                forEach(set::add);

        return set;
    }

    public static final class ItemStackValues {
        private final int slot;
        private final double value;
        @NotNull
        private final ItemStack itemStack;

        private ItemStackValues(final int slot, final double value, @NotNull final ItemStack itemStack) {
            this.slot = slot;
            this.value = value;
            this.itemStack = itemStack;
        }

        public int getSlot() {
            return slot;
        }

        public double getValue() {
            return value;
        }

        @NotNull
        public ItemStack getItemStack() {
            return itemStack;
        }

    }

}
