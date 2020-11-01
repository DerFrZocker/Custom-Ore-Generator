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

package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.spigot.utils.Config;
import de.derfrzocker.spigot.utils.ReloadAble;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class InfoImpl implements Info, ReloadAble {

    @NotNull
    private final Supplier<ConfigurationSection> configurationSectionSupplier;
    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private ConfigurationSection section;

    public InfoImpl(@NotNull final JavaPlugin plugin, @NotNull final String file) {
        this(plugin, file, true);
    }

    public InfoImpl(@NotNull final JavaPlugin plugin, @NotNull final String file, final boolean copy) {
        this(plugin, () -> copy ? Config.getConfig(plugin, file) : new Config(plugin.getResource(file)));
    }

    public InfoImpl(@NotNull final JavaPlugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
        Validate.notNull(plugin, "JavaPlugin can not be null");
        Validate.notNull(configurationSectionSupplier, "Supplier can not be null");

        this.configurationSectionSupplier = configurationSectionSupplier;
        this.plugin = plugin;
        this.section = configurationSectionSupplier.get();
        RELOAD_ABLES.add(this);
        reload();
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return MessageUtil.replacePlaceHolder(plugin, section.getString("display-name"));
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return Material.valueOf(section.getString("material"));
    }

    @NotNull
    @Override
    public String getDescription() {
        return MessageUtil.replacePlaceHolder(plugin, section.getString("description"));
    }

    @Override
    public void reload() {
        section = configurationSectionSupplier.get();
    }

}
