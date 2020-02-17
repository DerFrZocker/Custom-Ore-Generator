/*
 * MIT License
 *
 * Copyright (c) 2019 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.custom.ore.generator.factory.gui.settings;

import de.derfrzocker.spigot.utils.gui.BasicSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class OreSettingGuiSettings extends BasicSettings {
    public OreSettingGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public OreSettingGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public OreSettingGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
    }

    @NotNull
    public ItemStack getMenuItemStack(){
        return getSection().getItemStack("menu.item-stack").clone();
    }

    public int getMenuSlot(){
        return getSection().getInt("menu.slot");
    }

    @NotNull
    public ItemStack getAbortItemStack(){
        return getSection().getItemStack("abort.item-stack").clone();
    }

    public int getAbortSlot(){
        return getSection().getInt("abort.slot");
    }

    @NotNull
    public ItemStack getBackItemStack(){
        return getSection().getItemStack("back.item-stack").clone();
    }

    public int getBackSlot(){
        return getSection().getInt("back.slot");
    }

    @NotNull
    public ItemStack getOreSettingItemStack(){
        return getSection().getItemStack("ore-setting.item-stack").clone();
    }

    public int getOreSettingSlot(){
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
