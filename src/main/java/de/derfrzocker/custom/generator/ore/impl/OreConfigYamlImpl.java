package de.derfrzocker.custom.generator.ore.impl;

import de.derfrzocker.custom.generator.ore.api.OreConfig;
import de.derfrzocker.custom.generator.ore.api.OreSetting;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@SerializableAs(value = "CustomOreGenerator#OreConfig")
public class OreConfigYamlImpl implements OreConfig, ConfigurationSerializable {

    private final static String MATERIAL_KEY = "material";
    private final static String ORE_GENERATOR_KEY = "ore_generator";

    @Getter
    @NonNull
    private final Material material;

    @NonNull
    private final String oreGenerator;

    @Getter
    private final Map<OreSetting, Integer> oreSettings = new HashMap<>();

    public OreConfigYamlImpl(Material material, String oreGenerator) {
        if (!material.isBlock())
            throw new IllegalArgumentException("material " + material + " must be a block!");

        this.material = material;
        this.oreGenerator = oreGenerator;
    }

    @Override
    public String getOreGenerator() {
        return oreGenerator;
    }

    @Override
    public Optional<Integer> getValue(OreSetting setting) {
        return Optional.ofNullable(oreSettings.get(setting));
    }

    @Override
    public void setValue(OreSetting setting, int value) {
        oreSettings.put(setting, value);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put(MATERIAL_KEY, material.toString());
        map.put(ORE_GENERATOR_KEY, oreGenerator);

        getOreSettings().forEach((key, value) -> map.put(key.toString(), value));

        return map;
    }

    public static OreConfigYamlImpl deserialize(Map<String, Object> map) {
        OreConfigYamlImpl oreConfig = new OreConfigYamlImpl(Material.valueOf((String) map.get(MATERIAL_KEY)), (String) map.get(ORE_GENERATOR_KEY));

        map.entrySet().stream().filter(OreConfigYamlImpl::isOreSetting).forEach(entry -> oreConfig.setValue(OreSetting.valueOf(entry.getKey()), (Integer) entry.getValue()));

        return oreConfig;
    }

    private static boolean isOreSetting(Map.Entry<String, Object> entry) {
        try {
            OreSetting.valueOf(entry.getKey());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
