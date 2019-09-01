package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.BiomeConfig;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@SerializableAs(value = "CustomOreGenerator#WorldConfig")
public class WorldConfigYamlImpl implements WorldConfig, ConfigurationSerializable {

    private final static String WORLD_KEY = "world";

    @NonNull
    @Getter
    private final String world;

    private final Map<String, OreConfig> oreConfigs = new HashMap<>();

    public WorldConfigYamlImpl(String world) {
        this.world = world;
    }

    @Override
    public Optional<OreConfig> getOreConfig(String name) {
        return Optional.ofNullable(oreConfigs.get(name));
    }

    @Override
    public Set<OreConfig> getOreConfigs() {
        return new HashSet<>(oreConfigs.values());
    }

    @Override
    public void addOreConfig(OreConfig oreConfig) {
        oreConfigs.put(oreConfig.getName(), oreConfig);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put(WORLD_KEY, world);

        getOreConfigs().forEach(value -> {
            if (value instanceof ConfigurationSerializable) {
                map.put(value.getName(), value);
                return;
            }
            OreConfigYamlImpl oreConfig = new OreConfigYamlImpl(value.getName(), value.getMaterial(), value.getOreGenerator());

            value.getOreSettings().forEach(oreConfig::setValue);

            oreConfig.setActivated(value.isActivated());
            oreConfig.setGeneratedAll(value.shouldGeneratedAll());
            oreConfig.getBiomes().addAll(value.getBiomes());

            map.put(value.getName(), value);
        });

        return map;
    }

    public static WorldConfigYamlImpl deserialize(Map<String, Object> map) {
        WorldConfigYamlImpl worldConfig = new WorldConfigYamlImpl((String) map.get(WORLD_KEY));

        // map.entrySet().stream().filter(WorldConfigYamlImpl::isOreConfig).map(entry -> (OreConfig) entry.getValue()).forEach(worldConfig::addOreConfig);

        {//TODO switch back to stream in newer version and remove DummyOreConfig
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!isOreConfig(entry))
                    continue;

                if (entry.getValue() instanceof DummyOreConfig) {
                    int index = 0;

                    while (worldConfig.oreConfigs.containsKey("ore-config#" + index)) {
                        index++;
                    }

                    OreConfig oreConfig = new OreConfigYamlImpl("ore-config#" + index, ((DummyOreConfig) entry.getValue()).getMaterial(), ((DummyOreConfig) entry.getValue()).getOreGenerator());

                    oreConfig.getOreSettings().putAll(((DummyOreConfig) entry.getValue()).getOreSettings());

                    worldConfig.addOreConfig(oreConfig);
                } else {
                    worldConfig.addOreConfig((OreConfig) entry.getValue());
                }
            }
        }

        {//TODO remove in newer versions
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!isBiomeConfig(entry))
                    continue;

                Set<OreConfig> oreConfigs = worldConfig.convertBiomeConfig((BiomeConfig) entry.getValue());

                oreConfigs.forEach(worldConfig::addOreConfig);
            }
        }

        return worldConfig;
    }

    private static boolean isOreConfig(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof OreConfig;
    }

    @Deprecated
    private static boolean isBiomeConfig(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof BiomeConfig;
    }

    @Deprecated
    private Set<OreConfig> convertBiomeConfig(BiomeConfig biomeConfig) {
        final Set<OreConfig> oreConfigs = new HashSet<>();

        int index = 0;

        while (this.oreConfigs.containsKey("biome-config#" + index)) {
            index++;
        }

        for (final OreConfig oreConfig : biomeConfig.getOreConfigs()) {
            final OreConfig newOreConfig = new OreConfigYamlImpl("biome-config#" + index, oreConfig.getMaterial(), oreConfig.getOreGenerator());

            newOreConfig.getOreSettings().putAll(oreConfig.getOreSettings());
            newOreConfig.setGeneratedAll(false);
            newOreConfig.getBiomes().add(biomeConfig.getBiome());

            oreConfigs.add(newOreConfig);
            index++;
        }

        return oreConfigs;
    }

}
