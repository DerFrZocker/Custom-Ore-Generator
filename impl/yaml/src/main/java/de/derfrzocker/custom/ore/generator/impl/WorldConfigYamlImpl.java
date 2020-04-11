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

package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.BiomeConfig;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.spigot.utils.ReloadAble;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

@SerializableAs(value = "CustomOreGenerator#WorldConfig")
public class WorldConfigYamlImpl implements WorldConfig, ConfigurationSerializable, ReloadAble {

    private final static Pattern ORE_CONFIG_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");
    @Deprecated
    private final static String WORLD_KEY = "world";
    private final static String NAME_KEY = "name";
    private final static String ORE_CONFIG_KEY = "ore-config";

    @NotNull
    private final String name;

    private final Set<String> allOreConfigs = new LinkedHashSet<>();
    private final Map<String, OreConfig> oreConfigs = new LinkedHashMap<>();

    public WorldConfigYamlImpl(@NotNull final String name) {
        Validate.notNull(name, "Name can not be null");

        this.name = name;
    }

    public static WorldConfigYamlImpl deserialize(@NotNull final Map<String, Object> map) {
        if (map.containsKey(NAME_KEY)) { //newest format
            final WorldConfigYamlImpl worldConfig = new WorldConfigYamlImpl((String) map.get(NAME_KEY));
            final Object oreConfigs = map.get(ORE_CONFIG_KEY);

            if (oreConfigs != null)
                ((List<?>) oreConfigs).forEach(oreConfig -> worldConfig.allOreConfigs.add((String) oreConfig));

            return worldConfig;
        }

        final WorldConfigYamlImpl worldConfig = new WorldConfigYamlImpl((String) map.get(WORLD_KEY));
        final CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);

        Validate.notNull(service, "CustomOreGeneratorService can not be null");

        if (!map.containsKey(ORE_CONFIG_KEY)) {
            // old format version
            service.getLogger().info("Found very old WorldConfig format, replacing it with new one");

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                if (!(value instanceof OreConfigYamlImpl))
                    continue;

                if (value instanceof DummyOreConfig) {

                    int index = 0;

                    while (worldConfig.oreConfigs.containsKey("ore-config_" + index)) {
                        index++;
                    }

                    final DummyOreConfig source = (DummyOreConfig) value;

                    final OreConfigYamlImpl oreConfig = new OreConfigYamlImpl("ore-config_" + index, source.getMaterial(), source.getOreGenerator(), source.getBlockSelector());

                    OreConfigYamlImpl.copyData(source, oreConfig);

                    worldConfig.addOreConfig(oreConfig);
                } else if (ORE_CONFIG_NAME_PATTERN.matcher(worldConfig.getName()).matches()) {
                    worldConfig.addOreConfig((OreConfig) value);
                } else {
                    // give it a new name, when it not match the name convention
                    int index = 0;

                    while (worldConfig.oreConfigs.containsKey("ore-config_" + index)) {
                        index++;
                    }
                    final OreConfigYamlImpl source = (OreConfigYamlImpl) value;

                    final OreConfigYamlImpl oreConfig = new OreConfigYamlImpl("ore-config_" + index, source.getMaterial(), source.getOreGenerator(), source.getBlockSelector());

                    OreConfigYamlImpl.copyData(source, oreConfig);

                    worldConfig.addOreConfig(oreConfig);
                }
            }

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!isBiomeConfig(entry))
                    continue;

                final Set<OreConfig> oreConfigs = worldConfig.convertBiomeConfig((BiomeConfig) entry.getValue());

                oreConfigs.forEach(worldConfig::addOreConfig);
            }

        } else {
            //old format version
            service.getLogger().info("Found old WorldConfig format, replacing it with new one");
            final List<?> oreConfigs = (List<?>) map.get(ORE_CONFIG_KEY);
            oreConfigs.forEach(oreConfig -> worldConfig.addOreConfig((OreConfig) oreConfig));
        }

        for (final OreConfig oreConfig : worldConfig.getOreConfigs()) {
            if (service.getOreConfig(oreConfig.getName()).isPresent()) {
                int index = 0;

                while (service.getOreConfig(oreConfig.getName() + "_" + index).isPresent()) {
                    index++;
                }

                final OreConfigYamlImpl newOreConfig = new OreConfigYamlImpl(oreConfig.getName() + "_" + index, oreConfig.getMaterial(), oreConfig.getOreGenerator(), oreConfig.getBlockSelector());

                OreConfigYamlImpl.copyData(oreConfig, newOreConfig);

                service.saveOreConfig(newOreConfig);

                continue;
            }

            service.saveOreConfig(oreConfig);
        }

        service.saveWorldConfig(worldConfig);

        return worldConfig;
    }

    /**
     * Copy's all values from the given WorldConfig to the second given WorldConfig
     *
     * @param toCopy the source of the data
     * @param target to which the data should be get copy
     * @throws IllegalArgumentException if toCopy or target is null
     */
    public static void copyData(@NotNull final WorldConfig toCopy, @NotNull final WorldConfigYamlImpl target) {
        Validate.notNull(toCopy, "ToCopy WorldConfig can not be null");
        Validate.notNull(target, "Target WorldConfig can not be null");


        target.allOreConfigs.clear();
        target.oreConfigs.clear();
        target.allOreConfigs.addAll(toCopy.getAllOreConfigs());
    }

    @Deprecated
    private static boolean isBiomeConfig(@NotNull final Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof BiomeConfig;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public Optional<OreConfig> getOreConfig(@NotNull final String name) {
        Validate.notNull(name, "World name can not be null");

        checkOreConfigs();

        return Optional.ofNullable(this.oreConfigs.get(name));
    }

    @NotNull
    @Override
    public Set<OreConfig> getOreConfigs() {
        checkOreConfigs();

        return new LinkedHashSet<>(oreConfigs.values());
    }

    @Override
    public void addOreConfig(@NotNull final OreConfig oreConfig) {
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(!this.allOreConfigs.contains(oreConfig.getName()), "The OreConfig " + oreConfig.getName() + " is already added to the WorldConfig " + getName());

        this.oreConfigs.put(oreConfig.getName(), oreConfig);
        this.allOreConfigs.add(oreConfig.getName());
    }

    @Override
    public void addOreConfig(@NotNull final OreConfig oreConfig, int position) {
        final Set<String> allOreConfigs = getAllOreConfigs();
        this.allOreConfigs.clear();
        this.oreConfigs.clear();

        if (position < 0)
            position = 0;

        int current = 0;
        String storage = oreConfig.getName();

        allOreConfigs.remove(storage);

        for (final String oreConfigName : allOreConfigs) {
            if (current >= position) {
                this.allOreConfigs.add(storage);
                storage = oreConfigName;
            } else {
                this.allOreConfigs.add(oreConfigName);
            }

            current++;
        }

        this.allOreConfigs.add(storage);
    }

    @Override
    public Set<String> getAllOreConfigs() {
        return new LinkedHashSet<>(this.allOreConfigs);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> serialize = new LinkedHashMap<>();

        serialize.put(NAME_KEY, getName());

        if (!getAllOreConfigs().isEmpty()) {
            serialize.put(ORE_CONFIG_KEY, new LinkedList<>(getAllOreConfigs()));
        }

        return serialize;
    }

    @Override
    public void reload() {
        this.oreConfigs.clear();
    }

    /**
     * converts the old BiomeConfig format to the new OreConfig format
     *
     * @param biomeConfig to format
     * @return a set with the new OreConfig format
     */
    @Deprecated
    private Set<OreConfig> convertBiomeConfig(@NotNull final BiomeConfig biomeConfig) {
        final Set<OreConfig> oreConfigs = new HashSet<>();

        int index = 0;

        while (this.oreConfigs.containsKey("biome-config_" + index)) {
            index++;
        }

        for (final OreConfig source : biomeConfig.getOreConfigs()) {
            final OreConfigYamlImpl oreConfig = new OreConfigYamlImpl("biome-config_" + index, source.getMaterial(), source.getOreGenerator(), source.getBlockSelector());

            OreConfigYamlImpl.copyData((OreConfigYamlImpl) source, oreConfig);

            oreConfig.setGeneratedAll(false);
            oreConfig.addBiome(biomeConfig.getBiome());

            oreConfigs.add(oreConfig);
            index++;
        }

        return oreConfigs;
    }

    /**
     * Checks if some OreConfigs are now present
     */
    private void checkOreConfigs() {
        if (allOreConfigs.size() == oreConfigs.size())
            return;

        final CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);
        final Map<String, OreConfig> map = new LinkedHashMap<>();


        Validate.notNull(service, "CustomOreGeneratorService can not be null");

        allOreConfigs.forEach((name) -> {
            final OreConfig oreConfig = oreConfigs.get(name);

            if (oreConfig != null)
                map.put(name, oreConfig);

            final Optional<OreConfig> optionalOreConfig = service.getOreConfig(name);

            optionalOreConfig.ifPresent(config -> map.put(name, config));
        });

        oreConfigs.clear();
        oreConfigs.putAll(map);

    }

}
