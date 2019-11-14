package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SerializableAs(value = "CustomOreGenerator#OreConfig")
public class OreConfigYamlImpl implements OreConfig, ConfigurationSerializable {

    private final static String NAME_KEY = "name";
    private final static String MATERIAL_KEY = "material";

    @Deprecated
    private final static String ORE_GENERATOR_KEY_OLD = "ore_generator";

    private final static String ORE_GENERATOR_KEY = "ore-generator";
    private final static String BLOCK_SELECTOR_KEY = "block-selector";
    private final static String ACTIVATED_KEY = "activated";
    private final static String GENERATED_ALL_KEY = "generated-all";
    private final static String BIOMES_KEY = "biomes";
    private final static String REPLACE_MATERIALS_KEY = "replace-materials";
    private final static String SELECT_MATERIALS_KEY = "select-materials";
    private final static String CUSTOM_DATA_KEY = "custom-data";
    private final static String ORE_SETTINGS_KEY = "ore-settings";


    @NotNull
    private final String name;
    @NotNull
    private final Material material;
    private final Set<Biome> biomes = new HashSet<>();
    private final Set<Material> replaceMaterials = new HashSet<>();
    private final Set<Material> selectMaterials = new HashSet<>();
    private final Map<String, Integer> lazyOreSettings = new HashMap<>();
    private final Map<OreSetting, Integer> oreSettings = new HashMap<>();
    private final Map<String, Object> lazyCustomData = new HashMap<>();
    private final Map<CustomData, Object> customData = new HashMap<>();
    @NotNull
    private String oreGenerator;
    @NotNull
    private String blockSelector;
    private boolean activated = true;
    private boolean generatedAll = true;

    /**
     * Creates a new OreConfigYamlImpl with the given values
     *
     * @param name          of the OreConfig
     * @param material      of the OreConfig
     * @param oreGenerator  of the OreConfig
     * @param blockSelector of the OreConfig
     */
    public OreConfigYamlImpl(@NotNull final String name, @NotNull final Material material, @NotNull final String oreGenerator, @NotNull final String blockSelector) {
        Validate.notNull(name, "Name can not be null");
        Validate.notEmpty(name, "Name can not be empty");
        Validate.notNull(material, "Material can not be empty");
        Validate.notNull(oreGenerator, "OreGenerator can not be null");
        Validate.notEmpty(oreGenerator, "OreGenerator can not be empty");
        Validate.notNull(blockSelector, "BlockSelector can not be null");
        Validate.notEmpty(blockSelector, "BlockSelector can not be empty");

        this.name = name;
        this.material = material;
        this.oreGenerator = oreGenerator;
        this.blockSelector = blockSelector;
    }

    public static OreConfigYamlImpl deserialize(@NotNull final Map<String, Object> map) {
        Validate.notNull(map, "Map can not be null");

        final CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);
        final OreConfigYamlImpl oreConfig;

        Validate.notNull(service, "CustomOreGeneratorService can not be null");

        // check if it is the old or new format
        if (!map.containsKey(NAME_KEY)) {
            // older format version
            service.getLogger().info("Found old OreConfig format, replacing it with new one");

            final BlockSelector blockSelector = service.getDefaultBlockSelector();

            Validate.notNull(blockSelector, "Default BlockSelector is null");

            oreConfig = new DummyOreConfig(Material.valueOf((String) map.get(MATERIAL_KEY)), ((String) map.get(ORE_GENERATOR_KEY_OLD)).toUpperCase(), blockSelector.getName());

            map.entrySet().stream().filter(entry -> OreSetting.getOreSetting(entry.getKey()) != null).forEach(entry -> oreConfig.lazyOreSettings.put(entry.getKey(), NumberConversions.toInt(entry.getValue())));

            // add Default Materials
            oreConfig.addReplaceMaterial(Material.STONE);
        } else {
            // new format version
            final String blockSelectorName;

            if (!map.containsKey(BLOCK_SELECTOR_KEY)) {
                service.getLogger().info("BlockSelector not found try to use default one");
                final BlockSelector blockSelector = service.getDefaultBlockSelector();

                Validate.notNull(blockSelector, "Default BlockSelector is null");

                blockSelectorName = blockSelector.getName();
            } else {
                blockSelectorName = (String) map.get(BLOCK_SELECTOR_KEY);
            }

            oreConfig = new OreConfigYamlImpl((String) map.get(NAME_KEY), Material.valueOf((String) map.get(MATERIAL_KEY)), ((String) map.get(ORE_GENERATOR_KEY)).toUpperCase(), blockSelectorName);
            oreConfig.setActivated((boolean) map.get(ACTIVATED_KEY));
            oreConfig.setGeneratedAll((boolean) map.get(GENERATED_ALL_KEY));

            if (map.containsKey(BIOMES_KEY)) {
                ((List<String>) map.get(BIOMES_KEY)).forEach(biome -> oreConfig.addBiome(Biome.valueOf(biome)));
            }

            if (map.containsKey(REPLACE_MATERIALS_KEY)) {
                ((List<String>) map.get(REPLACE_MATERIALS_KEY)).forEach(material -> oreConfig.addReplaceMaterial(Material.valueOf(material)));
            }

            if (map.containsKey(SELECT_MATERIALS_KEY)) {
                ((List<String>) map.get(SELECT_MATERIALS_KEY)).forEach(material -> oreConfig.addSelectMaterial(Material.valueOf(material)));
            }

            if (map.containsKey(CUSTOM_DATA_KEY)) {
                oreConfig.lazyCustomData.putAll((Map<String, Object>) map.get(CUSTOM_DATA_KEY));
            }

            if (map.containsKey(ORE_SETTINGS_KEY)) {
                oreConfig.lazyOreSettings.putAll((Map<String, Integer>) map.get(ORE_SETTINGS_KEY));
            }
        }

        return oreConfig;
    }

    /**
     * Copy's all values from the given OreConfig to the second given OreConfig
     *
     * @param toCopy the source of the data
     * @param target to which the data should be get copy
     * @throws IllegalArgumentException if toCopy or target is null
     */
    public static void copyData(@NotNull final OreConfig toCopy, @NotNull final OreConfigYamlImpl target) {
        Validate.notNull(toCopy, "ToCopy OreConfig can not be null");
        Validate.notNull(target, "Target OreConfig can not be null");

        target.setActivated(toCopy.isActivated());
        target.setGeneratedAll(toCopy.shouldGeneratedAll());
        target.getBiomes().forEach(target::addBiome);
        toCopy.getCustomData().forEach(target::setCustomData);
        toCopy.getOreSettings().forEach(target::setValue);
        toCopy.getReplaceMaterials().forEach(target::addReplaceMaterial);
        toCopy.getSelectMaterials().forEach(target::addSelectMaterial);

        target.lazyCustomData.putAll(toCopy.getLazyCustomData());
        target.lazyOreSettings.putAll(toCopy.getLazyOreSettings());
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public void setActivated(final boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean shouldGeneratedAll() {
        return generatedAll;
    }

    @Override
    public void setGeneratedAll(final boolean generatedAll) {
        this.generatedAll = generatedAll;
    }

    @NotNull
    @Override
    public Set<Biome> getBiomes() {
        return new HashSet<>(biomes);
    }

    @Override
    public void addBiome(@NotNull final Biome biome) {
        Validate.notNull(biome, "Biome can not be null");

        biomes.add(biome);
    }

    @Override
    public void removeBiome(@NotNull final Biome biome) {
        Validate.notNull(biome, "Biome can not be null");

        biomes.remove(biome);
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return material;
    }

    @NotNull
    @Override
    public String getOreGenerator() {
        return oreGenerator;
    }

    @Override
    public void setOreGenerator(@NotNull final OreGenerator oreGenerator) {
        Validate.notNull(oreGenerator, "OreGenerator can not be null");

        this.oreGenerator = oreGenerator.getName();
    }

    @NotNull
    @Override
    public String getBlockSelector() {
        return this.blockSelector;
    }

    @Override
    public void setBlockSelector(@NotNull final BlockSelector blockSelector) {
        Validate.notNull(blockSelector, "BlockSelector can not be null");

        this.blockSelector = blockSelector.getName();
    }

    @NotNull
    @Override
    public Optional<Integer> getValue(@NotNull final OreSetting oreSetting) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        return Optional.ofNullable(getOreSettings().get(oreSetting));
    }

    @Override
    public void setValue(@NotNull final OreSetting oreSetting, final int value) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        checkLazyOreSettings();

        this.oreSettings.put(oreSetting, value);
    }

    @Override
    public boolean removeValue(@NotNull final OreSetting oreSetting) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        checkLazyOreSettings();

        return this.oreSettings.remove(oreSetting) != null;
    }

    @NotNull
    @Override
    public Map<OreSetting, Integer> getOreSettings() {
        checkLazyOreSettings();

        return new HashMap<>(this.oreSettings);
    }

    @NotNull
    @Override
    public Map<String, Integer> getLazyOreSettings() {
        checkLazyOreSettings();

        return new HashMap<>(this.lazyOreSettings);
    }

    @NotNull
    @Override
    public Map<CustomData, Object> getCustomData() {
        checkLazyCustomData();

        return new HashMap<>(this.customData);
    }

    @NotNull
    @Override
    public Map<String, Object> getLazyCustomData() {
        checkLazyCustomData();

        return new HashMap<>(this.lazyCustomData);
    }

    @NotNull
    @Override
    public Optional<Object> getCustomData(@NotNull final CustomData customData) {
        Validate.notNull(customData, "CustomData can not be null");

        return Optional.ofNullable(getCustomData().get(customData));
    }

    @Override
    public void setCustomData(@NotNull final CustomData customData, @Nullable final Object data) {
        Validate.notNull(customData, "CustomData can not be null");

        checkLazyCustomData();

        if (data == null)
            this.customData.remove(customData);
        else
            this.customData.put(customData, data);

    }

    @NotNull
    @Override
    public Set<Material> getReplaceMaterials() {
        return new HashSet<>(replaceMaterials);
    }

    @Override
    public void addReplaceMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        replaceMaterials.add(material);
    }

    @Override
    public void removeReplaceMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        replaceMaterials.remove(material);
    }

    @NotNull
    @Override
    public Set<Material> getSelectMaterials() {
        return new HashSet<>(selectMaterials);
    }

    @Override
    public void addSelectMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        selectMaterials.add(material);
    }

    @Override
    public void removeSelectMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        selectMaterials.remove(material);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> serialize = new LinkedHashMap<>();

        serialize.put(NAME_KEY, getName());
        serialize.put(MATERIAL_KEY, getMaterial().toString());
        serialize.put(ORE_GENERATOR_KEY, getOreGenerator());
        serialize.put(BLOCK_SELECTOR_KEY, getBlockSelector());
        serialize.put(ACTIVATED_KEY, isActivated());
        serialize.put(GENERATED_ALL_KEY, shouldGeneratedAll());

        final Set<Biome> biomesSet = getBiomes();
        if (!biomesSet.isEmpty()) {
            final List<String> data = new ArrayList<>();

            biomesSet.forEach(biome -> data.add(biome.toString()));

            serialize.put(BIOMES_KEY, data);
        }

        final Set<Material> replaceMaterialsSet = getReplaceMaterials();
        if (!replaceMaterialsSet.isEmpty()) {
            final List<String> data = new ArrayList<>();

            replaceMaterialsSet.forEach(material -> data.add(material.toString()));

            serialize.put(REPLACE_MATERIALS_KEY, data);
        }

        final Set<Material> selectMaterialsSet = getSelectMaterials();
        if (!selectMaterialsSet.isEmpty()) {
            final List<String> data = new ArrayList<>();

            selectMaterialsSet.forEach(material -> data.add(material.toString()));

            serialize.put(SELECT_MATERIALS_KEY, data);
        }

        final Map<CustomData, Object> customDataMap = getCustomData();
        if (!customDataMap.isEmpty() || !lazyCustomData.isEmpty()) {
            final Map<String, Object> data = new LinkedHashMap<>(lazyCustomData);

            customDataMap.forEach((customData, object) -> data.put(customData.getName(), object));

            serialize.put(CUSTOM_DATA_KEY, data);
        }

        final Map<OreSetting, Integer> oreSettingsMap = getOreSettings();
        if (!oreSettingsMap.isEmpty() || !lazyOreSettings.isEmpty()) {
            final Map<String, Integer> data = new LinkedHashMap<>(lazyOreSettings);

            oreSettingsMap.forEach((key, value) -> data.put(key.getName(), value));

            serialize.put(ORE_SETTINGS_KEY, data);
        }

        return serialize;
    }

    /**
     * Checks if the lazyOreSettings have any new OreSetting create and put them if so to the oreSettings map
     */
    private void checkLazyOreSettings() {
        if (lazyOreSettings.isEmpty())
            return;

        final Set<String> toRemove = new HashSet<>();

        lazyOreSettings.forEach((name, value) -> {
            final OreSetting oreSetting = OreSetting.getOreSetting(name);
            if (oreSetting == null)
                return;

            toRemove.add(name);
            oreSettings.put(oreSetting, value);
        });

        toRemove.forEach(lazyOreSettings::remove);
    }

    /**
     * Checks if the CustomOreGeneratorService have any new CustomData registered, if so put them to the customData map
     */
    private void checkLazyCustomData() {
        if (lazyCustomData.isEmpty())
            return;

        final CustomOreGeneratorService customOreGeneratorService = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);
        final Set<String> toRemove = new HashSet<>();

        Validate.notNull(customOreGeneratorService, "CustomOreGeneratorService can not be null");

        lazyCustomData.forEach((name, value) -> {
            final Optional<CustomData> customDataOptional = customOreGeneratorService.getCustomData(name);
            if (!customDataOptional.isPresent())
                return;

            toRemove.add(name);
            customData.put(customDataOptional.get(), value);
        });

        toRemove.forEach(lazyCustomData::remove);
    }

}
