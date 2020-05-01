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

package de.derfrzocker.custom.ore.generator.utils;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.spigot.utils.Version;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class RegisterUtil {

    @NotNull
    private final Plugin plugin;
    @NotNull
    private final CustomOreGeneratorService service;
    @NotNull
    private final Version currentVersion;

    public RegisterUtil(@NotNull Plugin plugin, @NotNull final CustomOreGeneratorService service, @NotNull final Version currentVersion) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(service, "CustomOreGeneratorService can not be null");
        Validate.notNull(currentVersion, "Version can not be null");

        this.plugin = plugin;
        this.service = service;
        this.currentVersion = currentVersion;
    }

    // OreGenerator

    public void register(@NotNull final OreGenerator oreGenerator) {
        this.service.registerOreGenerator(oreGenerator);
    }

    public void register(@NotNull final OreGenerator oreGenerator, boolean defaultOreGenerator) {
        register(oreGenerator);

        if (defaultOreGenerator) {
            this.service.setDefaultOreGenerator(oreGenerator);
        }

    }

    public void register(@NotNull final Version minimalVersion, @NotNull final Version maximumVersion, @NotNull final OreGeneratorSupplier oreGenerator, final boolean defaultOreGenerator) {
        if (minimalVersion.isNewerOrSameVersion(this.currentVersion)) {
            if (maximumVersion.isOlderOrSameVersion(this.currentVersion)) {
                register(oreGenerator.get(), defaultOreGenerator);
            }
        }
    }

    public void register(@NotNull final Version minimalVersion, @NotNull final Version maximumVersion, final boolean paper, @NotNull final OreGeneratorSupplier oreGenerator, final boolean defaultOreGenerator) {
        if (minimalVersion.isNewerOrSameVersion(this.currentVersion)) {
            if (maximumVersion.isOlderOrSameVersion(this.currentVersion)) {
                if (Version.isPaper()) {
                    register(oreGenerator.get(), defaultOreGenerator);
                }
            }
        }
    }

    // BlockSelector

    public void register(@NotNull final BlockSelector blockSelector) {
        this.service.registerBlockSelector(blockSelector);
    }

    public void register(@NotNull final BlockSelector blockSelector, boolean defaultBlockSelector) {
        register(blockSelector);

        if (defaultBlockSelector) {
            this.service.setDefaultBlockSelector(blockSelector);
        }

    }

    // CustomData

    public void register(@NotNull final CustomData customData) {
        this.service.registerCustomData(customData);
    }

    public void register(@NotNull final Version minimalVersion, @NotNull final CustomDataSupplier customData) {
        if (minimalVersion.isNewerOrSameVersion(this.currentVersion)) {
            register(customData.get());
        }
    }

    public void register(@NotNull final Version minimalVersion, @NotNull final String pluginName, @NotNull final CustomDataSupplier customData) {
        if (minimalVersion.isNewerOrSameVersion(this.currentVersion)) {
            if (this.plugin.getServer().getPluginManager().getPlugin(pluginName) != null) {
                register(customData.get());
            }
        }
    }

    public void register(@NotNull final Version minimalVersion, @NotNull final Version maximumVersion, @NotNull final CustomDataSupplier customData) {
        if (minimalVersion.isNewerOrSameVersion(this.currentVersion)) {
            if (maximumVersion.isOlderOrSameVersion(this.currentVersion)) {
                register(customData.get());
            }
        }
    }

    // Supplier

    public interface OreGeneratorSupplier {

        OreGenerator get();

    }

    public interface CustomDataSupplier {

        CustomData get();

    }

}
