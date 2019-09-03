package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@SerializableAs(value = "CustomOreGenerator#OreConfig")
public class OreConfigYamlImpl implements OreConfig, ConfigurationSerializable {

    private final static String NAME_KEY = "name";
    private final static String MATERIAL_KEY = "material";

    @Deprecated
    private final static String ORE_GENERATOR_KEY_OLD = "ore_generator";

    private final static String ORE_GENERATOR_KEY = "ore-generator";
    private final static String ACTIVATED_KEY = "activated";
    private final static String GENERATED_ALL_KEY = "generated-all";
    private final static String BIOMES_KEY = "biomes";
    private final static String CUSTOM_DATA_KEY = "custom-data";
    private final static String ORE_SETTINGS_KEY = "ore-settings";

    @Getter
    @NonNull
    private final String name;

    @Getter
    @NonNull
    private final Material material;

    @NonNull
    @Getter
    private final String oreGenerator;

    @Getter
    @Setter
    private boolean activated = true;

    @Setter
    private boolean generatedAll = true;

    @Getter
    private final Map<OreSetting, Integer> oreSettings = new HashMap<>();

    @Getter
    private final Set<Biome> biomes = new HashSet<>();

    private final Map<String, Object> lazyCustomData = new HashMap<>();

    private final Map<CustomData, Object> customData = new HashMap<>();

    public OreConfigYamlImpl(String name, Material material, String oreGenerator) {
        if (!material.isBlock())
            throw new IllegalArgumentException("material " + material + " must be a block!");

        this.name = name;
        this.material = material;
        this.oreGenerator = oreGenerator;
    }


    @Override
    public boolean shouldGeneratedAll() {
        return generatedAll;
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
    public Map<CustomData, Object> getCustomData() {
        final CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);

        final Set<String> toRemove = new HashSet<>();

        lazyCustomData.forEach((name, object) -> service.getCustomData(name).ifPresent(customData -> {
            toRemove.add(name);
            this.customData.put(customData, object);
        }));

        toRemove.forEach(this.lazyCustomData::remove);

        return this.customData;
    }

    @Override
    public Optional<Object> getCustomData(CustomData customData) {
        return Optional.ofNullable(getCustomData().get(customData));
    }

    @Override
    public void setCustomData(CustomData customData, Object data) {
        this.customData.put(customData, data);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put(NAME_KEY, getName());
        map.put(MATERIAL_KEY, getMaterial().toString());
        map.put(ORE_GENERATOR_KEY, getOreGenerator());
        map.put(GENERATED_ALL_KEY, shouldGeneratedAll());
        map.put(ACTIVATED_KEY, isActivated());

        if (!getBiomes().isEmpty()) {
            List<String> biomes = new ArrayList<>();

            getBiomes().forEach(biome -> biomes.add(biome.toString()));

            map.put(BIOMES_KEY, biomes);
        }

        if (!getCustomData().isEmpty() || !lazyCustomData.isEmpty()) {
            final Map<String, Object> data = new LinkedHashMap<>(lazyCustomData);

            getCustomData().forEach((customData, object) -> data.put(customData.getName(), data));

            map.put(CUSTOM_DATA_KEY, customData);
        }

        if (!getOreSettings().isEmpty()) {
            final Map<String, Integer> data = new LinkedHashMap<>();

            getOreSettings().forEach((key, value) -> data.put(key.toString(), value));

            map.put(ORE_SETTINGS_KEY, data);
        }

        return map;
    }

    public static OreConfigYamlImpl deserialize(Map<String, Object> map) {
        final OreConfigYamlImpl oreConfig;

        if (!map.containsKey(NAME_KEY)) {
            oreConfig = new DummyOreConfig(Material.valueOf((String) map.get(MATERIAL_KEY)), (String) map.get(ORE_GENERATOR_KEY_OLD));
            map.entrySet().stream().filter(OreConfigYamlImpl::isOreSetting).forEach(entry -> oreConfig.setValue(OreSetting.valueOf(entry.getKey()), (Integer) entry.getValue()));
        } else {
            oreConfig = new OreConfigYamlImpl((String) map.get(NAME_KEY), Material.valueOf((String) map.get(MATERIAL_KEY)), (String) map.get(ORE_GENERATOR_KEY));
            oreConfig.setActivated((boolean) map.get(ACTIVATED_KEY));
            oreConfig.setGeneratedAll((boolean) map.get(GENERATED_ALL_KEY));

            if (map.containsKey(BIOMES_KEY)) {
                ((List<String>) map.get(BIOMES_KEY)).forEach(biome -> oreConfig.getBiomes().add(Biome.valueOf(biome)));
            }

            if (map.containsKey(CUSTOM_DATA_KEY)) {
                oreConfig.lazyCustomData.putAll((Map<String, Object>) map.get(CUSTOM_DATA_KEY));
            }

            if (map.containsKey(ORE_SETTINGS_KEY)) {
                ((Map<String, Integer>) map.get(ORE_SETTINGS_KEY)).forEach((setting, value) -> oreConfig.setValue(OreSetting.valueOf(setting.toUpperCase()), value));
            }
        }

        return oreConfig;
    }

    @Deprecated
    private static boolean isOreSetting(Map.Entry<String, Object> entry) {
        try {
            OreSetting.valueOf(entry.getKey());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
