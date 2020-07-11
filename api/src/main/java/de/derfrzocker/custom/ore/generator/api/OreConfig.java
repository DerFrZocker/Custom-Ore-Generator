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

package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A OreConfig holds the data, to generate veins of Ores. Each different vein type,
 * has it's own OreConfig.
 */
public interface OreConfig {

    /**
     * The name is use to identify the OreConfig.
     * No OreConfig with the same name can be Present in a WorldOreConfig
     * The name is also use to seed the Random, that is use to generate the Ore of this
     * OreConfig.
     *
     * @return the name of this OreConfig
     */
    @NotNull
    String getName();

    /**
     * Return if this OreConfig should get generated or not.
     *
     * @return true if it should generate other wise false
     */
    boolean isActivated();

    /**
     * Set if this OreConfig should get generated or not.
     *
     * @param activated true for generate, false for not generate
     */
    void setActivated(boolean activated);

    /**
     * Return if this OreConfig should be generated on all biomes,
     * or only on the biomes that are in the Set which getBiomes() returns.
     *
     * @return true if this should generate on all biomes other wise false
     */
    boolean shouldGeneratedAll(); //TODO maybe better name

    /**
     * Set if this OreConfig should get generated on all biomes.
     *
     * @param generatedAll true for all, false for only specific biomes
     */
    void setGeneratedAll(boolean generatedAll); //TODO maybe better name

    /**
     * Returns a copy of all biomes which should get generate.
     * The OreConfig gets only generated on the the specific biomes,
     * when shouldGeneratedAll() false is.
     * <p>
     * The Set that get's returned maybe immutable.
     *
     * @return a set with biomes
     */
    @NotNull
    Set<Biome> getBiomes();

    /**
     * Add a biome to this OreConfig
     *
     * @param biome which should be added
     * @throws IllegalArgumentException if biome is null
     */
    void addBiome(@NotNull Biome biome);

    /**
     * Remove a biome from this OreConfig
     *
     * @param biome which should be removed
     * @throws IllegalArgumentException if biome is null
     */
    void removeBiome(@NotNull Biome biome);

    /**
     * @return the main Material of this OreConfig
     */
    @NotNull
    Material getMaterial();

    /**
     * @return the name of the OreGenerator
     */
    @NotNull
    String getOreGenerator();

    /**
     * Set the OreGenerator of this OreConfig
     *
     * @param oreGenerator to set
     * @throws IllegalArgumentException if oreGenerator is null
     */
    void setOreGenerator(@NotNull OreGenerator oreGenerator);

    /**
     * @return the name of the BlockSelector
     */
    @NotNull
    String getBlockSelector();

    /**
     * Set the BlockSelector of this OreConfig
     *
     * @param blockSelector to set
     * @throws IllegalArgumentException if blockSelector is null
     */
    void setBlockSelector(@NotNull BlockSelector blockSelector);

    /**
     * @return the OreSettingContainer for the OreGenerator
     */
    @NotNull
    OreSettingContainer getOreGeneratorOreSettings();

    /**
     * @return the OreSettingContainer for the BlockSelector
     */
    @NotNull
    OreSettingContainer getBlockSelectorOreSettings();

    /**
     * Returns a copy of all loaded CustomData and it's values, which this OreConfig has.
     * <p>
     * The map that get's returned is maybe immutable.
     *
     * @return a map with all CustomData and values
     */
    @NotNull
    Map<CustomData, Object> getCustomData();

    /**
     * Returns a copy of all lazy CustomData and it's values, which this OreConfig has.
     * Lazy CustomData are CustomData, which can be loaded at the moment. Because their are not
     * registered jet.
     *
     * @return a map with all lazy CustomData and values
     */
    @NotNull
    Map<String, Object> getLazyCustomData();

    /**
     * If this OreConfig contains the value of the given CustomData,
     * it returns an Optional that contains the value,
     * otherwise it return an empty Optional
     *
     * @param customData which must be non-null
     * @return an Optional that hold the value of the given CustomData,
     * or an empty Optional if the OreConfig not contain the given customData.
     * @throws IllegalArgumentException if customData is null
     */
    @NotNull
    Optional<Object> getCustomData(@NotNull CustomData customData);

    /**
     * This sets the given CustomData with the given value, to this
     * OreConfig. If this OreConfig already contains the given CustomData,
     * than the old value get's replaced.
     * <p>
     * If data is null, the CustomData get's removed from this OreConfig.
     *
     * @param customData which must be non-null
     * @param data       for the given customData
     * @throws IllegalArgumentException if customData is null
     */
    void setCustomData(@NotNull CustomData customData, @Nullable Object data);

    /**
     * Return a new Set which contains all Materials which should get replaced
     * by this OreConfig
     *
     * @return a set with Materials
     */
    @NotNull
    Set<Material> getReplaceMaterials();

    /**
     * Add a replace material to this OreConfig
     *
     * @param material which should be added
     * @throws IllegalArgumentException if material is null
     */
    void addReplaceMaterial(@NotNull Material material);

    /**
     * Remove a replace material from this OreConfig
     *
     * @param material which should be removed
     * @throws IllegalArgumentException if material is null
     */
    void removeReplaceMaterial(@NotNull Material material);

    /**
     * Return a new Set which contains all Materials on which a vein should begin to generate.
     * If the returned set is empty, it will use the materials which getReplaceMaterials return as select
     * materials.
     *
     * @return a set with Materials
     */
    @NotNull
    Set<Material> getSelectMaterials();

    /**
     * Add a select material to this OreConfig
     *
     * @param material which should be added
     * @throws IllegalArgumentException if material is null
     */
    void addSelectMaterial(@NotNull Material material);

    /**
     * Remove a select material from this OreConfig
     *
     * @param material which should be removed
     * @throws IllegalArgumentException if material is null
     */
    void removeSelectMaterial(@NotNull Material material);

}
