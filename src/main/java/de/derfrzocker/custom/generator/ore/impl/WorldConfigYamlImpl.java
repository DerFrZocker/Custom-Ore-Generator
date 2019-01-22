package de.derfrzocker.custom.generator.ore.impl;

import de.derfrzocker.custom.generator.ore.api.BiomeConfig;
import de.derfrzocker.custom.generator.ore.api.OreConfig;
import de.derfrzocker.custom.generator.ore.api.WorldConfig;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@SerializableAs(value = "CustomOreGenerator#WorldConfig")
public class WorldConfigYamlImpl implements WorldConfig, ConfigurationSerializable {

    private final static String WORLD_KEY = "world";

    @NonNull
    @Getter
    private final String world;

    private final Map<Material, OreConfig> oreConfigs = new HashMap<>();

    private final Map<Biome, BiomeConfig> biomeConfigs = new HashMap<>();

    public WorldConfigYamlImpl(String world) {
        this.world = world;
    }

    @Override
    public Optional<OreConfig> getOreConfig(Material material) {
        return Optional.ofNullable(oreConfigs.get(material));
    }

    @Override
    public Set<OreConfig> getOreConfigs() {
        return new HashSet<>(oreConfigs.values());
    }

    @Override
    public void addOreConfig(OreConfig oreConfig) {
        oreConfigs.put(oreConfig.getMaterial(), oreConfig);
    }

    @Override
    public Optional<BiomeConfig> getBiomeConfig(Biome biome) {
        return Optional.ofNullable(biomeConfigs.get(biome));
    }

    @Override
    public Set<BiomeConfig> getBiomeConfigs() {
        return new HashSet<>(biomeConfigs.values());
    }

    @Override
    public void addBiomeConfig(BiomeConfig biomeConfig) {
        biomeConfigs.put(biomeConfig.getBiome(), biomeConfig);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put(WORLD_KEY, world);

        //noinspection Duplicates
        getOreConfigs().forEach(value -> {
            if (value instanceof ConfigurationSerializable) {
                map.put(value.getMaterial().toString(), value);
                return;
            }
            OreConfigYamlImpl oreConfig = new OreConfigYamlImpl(value.getMaterial(), value.getOreGenerator());

            value.getOreSettings().forEach(oreConfig::setValue);

            map.put(value.getMaterial().toString(), value);
        });

        getBiomeConfigs().forEach(value -> {
            if (value instanceof ConfigurationSerializable) {
                map.put(value.getBiome().toString(), value);
                return;
            }
            BiomeConfigYamlImpl biomeConfigYaml = new BiomeConfigYamlImpl(value.getBiome());

            value.getOreConfigs().forEach(biomeConfigYaml::addOreConfig);

            map.put(value.getBiome().toString(), value);
        });

        return map;
    }

    public static WorldConfigYamlImpl deserialize(Map<String, Object> map) {
        WorldConfigYamlImpl oreConfig = new WorldConfigYamlImpl((String) map.get(WORLD_KEY));

        map.entrySet().stream().filter(WorldConfigYamlImpl::isOreConfig).map(entry -> (OreConfig) entry.getValue()).forEach(oreConfig::addOreConfig);
        map.entrySet().stream().filter(WorldConfigYamlImpl::isBiomeConfig).map(entry -> (BiomeConfig) entry.getValue()).forEach(oreConfig::addBiomeConfig);

        return oreConfig;
    }

    private static boolean isOreConfig(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof OreConfig;
    }

    private static boolean isBiomeConfig(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof OreConfig;
    }

}
