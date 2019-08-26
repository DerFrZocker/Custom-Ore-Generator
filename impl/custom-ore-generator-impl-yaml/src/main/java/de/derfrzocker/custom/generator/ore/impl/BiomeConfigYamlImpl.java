package de.derfrzocker.custom.generator.ore.impl;

import de.derfrzocker.custom.generator.ore.api.BiomeConfig;
import de.derfrzocker.custom.generator.ore.api.OreConfig;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@SerializableAs(value = "CustomOreGenerator#BiomeConfig")
public class BiomeConfigYamlImpl implements BiomeConfig, ConfigurationSerializable {

    private final static String BIOME_KEY = "biome";

    @Getter
    @NonNull
    private final Biome biome;

    private final Map<Material, OreConfig> oreConfigs = new HashMap<>();

    public BiomeConfigYamlImpl(Biome biome) {
        this.biome = biome;
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

    @SuppressWarnings("Duplicates")
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put(BIOME_KEY, biome.toString());

        getOreConfigs().forEach(value -> {
            if (value instanceof ConfigurationSerializable) {
                map.put(value.getMaterial().toString(), value);
                return;
            }
            OreConfigYamlImpl oreConfig = new OreConfigYamlImpl(value.getMaterial(), value.getOreGenerator());

            value.getOreSettings().forEach(oreConfig::setValue);

            map.put(value.getMaterial().toString(), value);
        });

        return map;
    }

    public static BiomeConfigYamlImpl deserialize(Map<String, Object> map) {
        BiomeConfigYamlImpl biomeConfig = new BiomeConfigYamlImpl(Biome.valueOf((String) map.get(BIOME_KEY)));

        map.entrySet().stream().filter(BiomeConfigYamlImpl::isOreConfig).map(entry -> (OreConfig) entry.getValue()).forEach(biomeConfig::addOreConfig);

        return biomeConfig;
    }

    private static boolean isOreConfig(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof OreConfig;
    }

}
