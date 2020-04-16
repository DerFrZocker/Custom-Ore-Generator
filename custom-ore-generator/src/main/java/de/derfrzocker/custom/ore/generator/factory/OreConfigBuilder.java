package de.derfrzocker.custom.ore.generator.factory;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class OreConfigBuilder {

    @Nullable
    private String name;
    @Nullable
    private Material material;
    @Nullable
    private OreGenerator oreGenerator;
    @Nullable
    private BlockSelector blockSelector;
    @NotNull
    private Set<Material> replaceMaterial = new LinkedHashSet<>();
    @NotNull
    private Set<Material> selectMaterial = new LinkedHashSet<>();
    @NotNull
    private Map<OreSetting, Double> oreSettings = new LinkedHashMap<>();
    @NotNull
    private Map<CustomData, Object> customDatas = new LinkedHashMap<>();
    @NotNull
    private Set<Biome> biomes = new LinkedHashSet<>();
    @NotNull
    private Set<World> worlds = new LinkedHashSet<>();
    @NotNull
    private Map<CustomData, Object> foundCustomData = new LinkedHashMap<>();


    public static OreConfigBuilder newBuilder() {
        return new OreConfigBuilder();
    }

    @NotNull
    public OreConfigBuilder name(@Nullable final String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public String name() {
        return this.name;
    }

    @NotNull
    public OreConfigBuilder material(@Nullable final Material material) {
        this.material = material;
        return this;
    }

    @Nullable
    public Material material() {
        return this.material;
    }

    @NotNull
    public OreConfigBuilder addReplaceMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        this.replaceMaterial.add(material);
        return this;
    }

    @NotNull
    public OreConfigBuilder removeReplaceMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        this.replaceMaterial.remove(material);
        return this;
    }

    public boolean containsReplaceMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        return this.replaceMaterial.contains(material);
    }

    @NotNull
    public Set<Material> replaceMaterial() {
        return this.replaceMaterial;
    }

    @NotNull
    public OreConfigBuilder addSelectMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        this.selectMaterial.add(material);
        return this;
    }

    @NotNull
    public OreConfigBuilder removeSelectMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        this.selectMaterial.remove(material);
        return this;
    }

    public boolean containsSelectMaterial(@NotNull final Material material) {
        Validate.notNull(material, "Material can not be null");

        return this.selectMaterial.contains(material);
    }

    @NotNull
    public Set<Material> selectMaterial() {
        return this.selectMaterial;
    }

    @Nullable
    public OreGenerator oreGenerator() {
        return this.oreGenerator;
    }

    @NotNull
    public OreConfigBuilder oreGenerator(@Nullable final OreGenerator oreGenerator) {
        this.oreGenerator = oreGenerator;

        return this;
    }

    @Nullable
    public BlockSelector blockSelector() {
        return this.blockSelector;
    }

    @NotNull
    public OreConfigBuilder blockSelector(@Nullable final BlockSelector blockSelector) {
        this.blockSelector = blockSelector;

        return this;
    }

    @NotNull
    public OreConfigBuilder setOreSetting(@NotNull final OreSetting oreSetting, double value) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        this.oreSettings.put(oreSetting, value);

        return this;
    }

    @Nullable
    public Double getOreSetting(@NotNull final OreSetting oreSetting) {
        Validate.notNull(oreSetting, "OreSetting can not be null");

        return this.oreSettings.get(oreSetting);
    }

    @NotNull
    public Map<OreSetting, Double> oreSettings() {
        return this.oreSettings;
    }

    @NotNull
    public OreConfigBuilder setCustomData(@NotNull final CustomData customData, @NotNull final Object value) {
        Validate.notNull(customData, "CustomData can not be null");
        Validate.notNull(value, "CustomData value can not be null");

        this.customDatas.put(customData, value);

        return this;
    }

    @Nullable
    public Object getCustomData(@NotNull final CustomData customData) {
        Validate.notNull(customData, "CustomData can not be null");

        return this.customDatas.get(customData);
    }

    @Nullable
    public OreConfigBuilder removeCustomData(@NotNull final CustomData customData) {
        Validate.notNull(customData, "CustomData can not be null");

        this.customDatas.remove(customData);
        return this;
    }

    @NotNull
    public Map<CustomData, Object> customDatas() {
        return this.customDatas;
    }

    @NotNull
    public OreConfigBuilder addBiome(@NotNull final Biome biome) {
        Validate.notNull(biome, "Biome can not be null");

        this.biomes.add(biome);
        return this;
    }

    @NotNull
    public OreConfigBuilder removeBiome(@NotNull final Biome biome) {
        Validate.notNull(biome, "Biome can not be null");

        this.biomes.remove(biome);
        return this;
    }

    public boolean containsBiome(@NotNull final Biome biome) {
        Validate.notNull(biome, "Biome can not be null");

        return this.biomes.contains(biome);
    }

    @NotNull
    public Set<Biome> biomes() {
        return this.biomes;
    }

    @NotNull
    public OreConfigBuilder addWorld(@NotNull final World world) {
        Validate.notNull(world, "World can not be null");

        this.worlds.add(world);
        return this;
    }

    @NotNull
    public OreConfigBuilder removeWorld(@NotNull final World world) {
        Validate.notNull(world, "World can not be null");

        this.worlds.remove(world);
        return this;
    }

    public boolean containsWorld(@NotNull final World world) {
        Validate.notNull(world, "World can not be null");

        return this.worlds.contains(world);
    }

    @NotNull
    public Set<World> worlds() {
        return this.worlds;
    }

    @NotNull
    public OreConfigBuilder setFoundCustomData(@NotNull final CustomData customData, @NotNull final Object value) {
        Validate.notNull(customData, "CustomData can not be null");
        Validate.notNull(value, "CustomData value can not be null");

        this.foundCustomData.put(customData, value);

        return this;
    }

    @Nullable
    public Object getFoundCustomData(@NotNull final CustomData customData) {
        Validate.notNull(customData, "CustomData can not be null");

        return this.foundCustomData.get(customData);
    }

    @NotNull
    public Map<CustomData, Object> foundCustomDatas() {
        return this.foundCustomData;
    }

}
