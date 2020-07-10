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

import java.util.function.Supplier;

public class MenuGuiSettings extends BasicSettings {

    public MenuGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public MenuGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public MenuGuiSettings(@NotNull final JavaPlugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
    }

    @NotNull
    public ItemStack getAbortItemStack() {
        return getSection().getItemStack("abort.item-stack").clone();
    }

    public int getAbortSlot() {
        return getSection().getInt("abort.slot");
    }

    @NotNull
    public ItemStack getOreConfigReadyItemStack() {
        return getSection().getItemStack("ore-config.ready").clone();
    }

    @NotNull
    public ItemStack getOreConfigNotReadyItemStack() {
        return getSection().getItemStack("ore-config.not-ready").clone();
    }

    @NotNull
    public ItemStack getStatusNeededItemStack() {
        return getSection().getItemStack("status.needed").clone();
    }

    @NotNull
    public ItemStack getStatusPresentItemStack() {
        return getSection().getItemStack("status.present").clone();
    }

    @NotNull
    public ItemStack getStatusNotPresentNotNeededItemStack() {
        return getSection().getItemStack("status.not-present-not-needed").clone();
    }

    @NotNull
    public ItemStack getStatusNotSetAbleItemStack() {
        return getSection().getItemStack("status.not-set-able").clone();
    }

    public int getOreConfigItemStackSlot() {
        return getSection().getInt("ore-config.slot");
    }

    public int getStepNameSlot() {
        return getSection().getInt("step.name");
    }

    public int getStepMaterialSlot() {
        return getSection().getInt("step.material");
    }

    public int getStepReplaceMaterialsSlot() {
        return getSection().getInt("step.replace-materials");
    }

    public int getStepSelectMaterialsSlot() {
        return getSection().getInt("step.select-materials");
    }

    public int getStepOreGeneratorSlot() {
        return getSection().getInt("step.ore-generator");
    }

    public int getStepBlockSelectorSlot() {
        return getSection().getInt("step.block-selector");
    }

    public int getStepBiomeSlot() {
        return getSection().getInt("step.biome");
    }

    public int getStepOreGeneratorOreSettingsSlot() {
        return getSection().getInt("step.ore-generator-ore-settings");
    }

    public int getStepBlockSelectorOreSettingsSlot() {
        return getSection().getInt("step.block-selector-ore-settings");
    }

    public int getStepCustomDatasSlot() {
        return getSection().getInt("step.custom-datas");
    }

    public int getStepWorldSlot() {
        return getSection().getInt("step.world");
    }

}
