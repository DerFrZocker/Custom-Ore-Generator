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

import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.api.OreSettingContainer;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@SerializableAs(value = "CustomOreGenerator#OreSettingsContainer")
public class OreSettingsContainerYamlImpl implements OreSettingContainer, ConfigurationSerializable {

    private final static String ORE_SETTINGS_KEY = "ore-settings";

    @NotNull
    private final Map<OreSetting, Double> oreSettings = new LinkedHashMap<>();

    public static OreSettingsContainerYamlImpl deserialize(@NotNull final Map<String, Object> map) {
        Validate.notNull(map, "Map can not be null");

        final OreSettingsContainerYamlImpl oreSettingContainer = new OreSettingsContainerYamlImpl();

        if (map.containsKey(ORE_SETTINGS_KEY)) {
            ((Map<String, Object>) map.get(ORE_SETTINGS_KEY)).forEach((setting, value) -> oreSettingContainer.setValue(OreSetting.createOreSetting(setting), NumberConversions.toDouble(value)));
        }

        return oreSettingContainer;
    }

    @Override
    public void setValue(@NotNull final OreSetting oreSetting, final double value) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        this.oreSettings.put(oreSetting, value);
    }

    @NotNull
    @Override
    public Optional<Double> getValue(@NotNull final OreSetting oreSetting) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        return Optional.ofNullable(this.oreSettings.get(oreSetting));
    }

    @Override
    public boolean removeValue(@NotNull final OreSetting oreSetting) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        return this.oreSettings.remove(oreSetting) != null;
    }

    @NotNull
    @Override
    public Map<OreSetting, Double> getValues() {
        return new LinkedHashMap<>(this.oreSettings);
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> serialize = new LinkedHashMap<>();

        final Map<OreSetting, Double> oreSettings = getValues();
        if (!oreSettings.isEmpty()) {
            final Map<String, Double> data = new LinkedHashMap<>();

            oreSettings.forEach((oreSetting, aDouble) -> data.put(oreSetting.getName(), aDouble));

            serialize.put(ORE_SETTINGS_KEY, data);
        }

        return serialize;
    }

}
