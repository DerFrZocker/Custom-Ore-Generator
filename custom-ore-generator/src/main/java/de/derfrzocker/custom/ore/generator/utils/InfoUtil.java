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

package de.derfrzocker.custom.ore.generator.utils;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.impl.InfoImpl;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InfoUtil {

    @NotNull
    public static Info getBlockSelectorInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name) {
        return new InfoImpl(javaPlugin, "data/info/block-selector/" + name + "/" + name + ".yml");
    }

    @NotNull
    public static Info getOreGeneratorInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name) {
        return new InfoImpl(javaPlugin, "data/info/ore-generator/" + name + "/" + name + ".yml");
    }

    @NotNull
    public static Info getCustomDataInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name) {
        return new InfoImpl(javaPlugin, "data/info/custom-data/" + name + ".yml");
    }

    @NotNull
    public static Info getBlockSelectorOreSettingInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name, @NotNull final OreSetting oreSetting) {
        return new InfoImpl(javaPlugin, "data/info/block-selector/" + name + "/ore-setting/" + oreSetting.getName() + ".yml");
    }

    @NotNull
    public static Info getOreGeneratorOreSettingInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name, @NotNull final OreSetting oreSetting) {
        return new InfoImpl(javaPlugin, "data/info/ore-generator/" + name + "/ore-setting/" + oreSetting.getName() + ".yml");
    }

}
