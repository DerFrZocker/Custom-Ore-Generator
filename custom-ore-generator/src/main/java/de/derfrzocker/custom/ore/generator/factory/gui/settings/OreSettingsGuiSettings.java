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

import de.derfrzocker.spigot.utils.gui.PageSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OreSettingsGuiSettings extends PageSettings {

    public OreSettingsGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public OreSettingsGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public OreSettingsGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
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
    public ItemStack getNextItemStack(){
        return getSection().getItemStack("next.item-stack").clone();
    }

    public int getNextSlot(){
        return getSection().getInt("next.slot");
    }

    @NotNull
    public ItemStack getOreSettingItemStack(){
        return getSection().getItemStack("ore-setting.item-stack").clone();
    }

}
