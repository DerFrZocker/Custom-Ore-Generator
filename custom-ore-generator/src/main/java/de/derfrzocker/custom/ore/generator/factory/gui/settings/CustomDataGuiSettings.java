/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
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
