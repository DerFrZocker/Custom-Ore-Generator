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
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public interface CustomOreGeneratorService {

    /**
     * If this CustomOreGeneratorService have the BlockSelector registered
     * it returns an Optional that contains the BlockSelector,
     * Otherwise it return an empty Optional
     *
     * @param name of the blockSelector
     * @return an Optional that hold the value of the given name,
     * or an empty Optional if the CustomOreGeneratorService not have the BlockSelector registered
     * @throws IllegalArgumentException if name is null
     */
    @NotNull
    Optional<BlockSelector> getBlockSelector(@NotNull String name);

    /**
     * Register the given BlockSelector to this CustomOreGeneratorService service
     * The name of the BlockSelector must match the following Regex: ^[A-Z_]*$
     * The name of the BlockSelector can not be empty
     * If a BlockSelector with the same name is already registered, it throws an Exception
     *
     * @param blockSelector which get registered
     * @throws IllegalArgumentException if blockSelector is null
     * @throws IllegalArgumentException if the name of the BlockSelector doesn't match the Regex: ^[A-Z_]*$
     * @throws IllegalArgumentException if the name of the blockSelector is empty
     * @throws IllegalArgumentException if a BlockSelector with the same name is already registered
     */
    void registerBlockSelector(@NotNull BlockSelector blockSelector);

    /**
     * @return a new set with all registered BlockSelector's
     */
    @NotNull
    Set<BlockSelector> getBlockSelectors();

    /**
     * Set's the default BlockSelector if a default BlockSelector is
     * already set, it get's overridden by the new one.
     *
     * @param blockSelector the new default BlockSelector
     * @throws IllegalArgumentException if the blockSelector is not null and not registered
     */
    void setDefaultBlockSelector(@Nullable BlockSelector blockSelector);

    /**
     * @return the default BlockSelector or null if not set
     */
    @Nullable
    BlockSelector getDefaultBlockSelector();

    /**
     * If this CustomOreGeneratorService have the OreGenerator registered
     * it returns an Optional that contains the OreGenerator,
     * Otherwise it return an empty Optional
     *
     * @param name of the generator
     * @return an Optional that hold the value of the given name,
     * or an empty Optional if the CustomOreGeneratorService not have the OreGenerator registered
     * @throws IllegalArgumentException if name is null
     */
    @NotNull
    Optional<OreGenerator> getOreGenerator(@NotNull String name);

    /**
     * Register the given OreGenerator to this CustomOreGeneratorService service
     * The name of the OreGenerator must match the following Regex: ^[A-Z_]*$
     * The name of the OreGenerator can not be empty
     * If a OreGenerator with the same name is already registered, it throws an Exception
     *
     * @param oreGenerator which get registered
     * @throws IllegalArgumentException if oreGenerator is null
     * @throws IllegalArgumentException if the name of the OreGenerator doesn't match the Regex: ^[A-Z_]*$
     * @throws IllegalArgumentException if the name of the oreGenerator is empty
     * @throws IllegalArgumentException if a OreGenerator with the same name is already registered
     */
    void registerOreGenerator(@NotNull OreGenerator oreGenerator);

    /**
     * @return a new set with all registered OreGenerator's
     */
    @NotNull
    Set<OreGenerator> getOreGenerators();

    /**
     * Set's the default OreGenerator if a default OreGenerator is
     * already set, it get's overridden by the new one.
     *
     * @param oreGenerator the new default OreGenerator
     * @throws IllegalArgumentException if the oreGenerator is not null and not registered
     */
    void setDefaultOreGenerator(@Nullable OreGenerator oreGenerator);

    /**
     * @return the default OreGenerator or null if not set
     */
    @Nullable
    OreGenerator getDefaultOreGenerator();

    /**
     * If this CustomOreGeneratorService have the CustomData registered
     * it returns an Optional that contains the CustomData,
     * Otherwise it return an empty Optional
     *
     * @param name of the customData
     * @return an Optional that hold the value of the given name,
     * or an empty Optional if the CustomOreGeneratorService not have the CustomData registered
     * @throws IllegalArgumentException if name is null
     */
    @NotNull
    Optional<CustomData> getCustomData(@NotNull String name);

    /**
     * Register the given CustomData to this CustomOreGeneratorService service
     * The name of the CustomData must match the following Regex: ^[A-Z_]*$
     * The name of the CustomData can not be empty
     * If a CustomData with the same name is already registered, it throws an Exception
     *
     * @param customData which get registered
     * @throws IllegalArgumentException if customData is null
     * @throws IllegalArgumentException if the name of the customData doesn't match the Regex: ^[A-Z_]*$
     * @throws IllegalArgumentException if the name of the customData is empty
     * @throws IllegalArgumentException if a CustomData with the same name is already registered
     */
    void registerCustomData(@NotNull CustomData customData);

    /**
     * @return a new set with all registered CustomData's
     */
    @NotNull
    Set<CustomData> getCustomData();

    /**
     * If the WorldConfig with the given name exist
     * it returns an Optional that contains the WorldConfig,
     * Otherwise it return an empty Optional
     *
     * @param name the name of the world
     * @return an Optional that hold the value of the given name,
     * or an empty Optional if the WorldConfig doesn't exist
     * @throws IllegalArgumentException if name is null
     */
    @NotNull
    Optional<WorldConfig> getWorldConfig(@NotNull String name);

    /**
     * Creates a new WorldConfig for the given world.
     * If for the given world already a WorldConfig exist, then it returns the
     * existing one.
     *
     * @param world for which the WorldConfig is
     * @return a new WorldConfig or if one already exists, the existing one.
     * @throws IllegalArgumentException if world is null
     */
    @NotNull
    WorldConfig createWorldConfig(@NotNull World world);

    /**
     * Creates a new WorldConfig for the given worldName.
     * If for the given name already a WorldConfig exist, then it returns the
     * existing one.
     * The name must match the following Regex: //TODO search name check regex
     * The name can not be empty
     *
     * @param name for which the WorldConfig is
     * @return a new WorldConfig or if one already exists, the existing one.
     * @throws IllegalArgumentException if name is null
     * @throws IllegalArgumentException if the name doesn't match the Regex: //TODO search name check regex
     * @throws IllegalArgumentException if the name is empty
     */
    @NotNull
    WorldConfig createWorldConfig(@NotNull String name);

    /**
     * Saves the given WorldOreConfig to disk
     *
     * @param config to save
     */
    void saveWorldConfig(@NotNull WorldConfig config);

    /**
     * @return a new set with all WorldConfig's
     */
    @NotNull
    Set<WorldConfig> getWorldConfigs();

    /**
     * If the OreConfig with the given name exist
     * it returns an Optional that contains the OreConfig,
     * Otherwise it return an empty Optional
     *
     * @param name the name of the oreConfig
     * @return an Optional that hold the value of the given name,
     * or an empty Optional if the WorldConfig doesn't exist
     * @throws IllegalArgumentException if name is null
     */
    @NotNull
    Optional<OreConfig> getOreConfig(@NotNull String name);

    /**
     * Creates a new OreConfig with the given parameters.
     * The name must match the following Regex: ^[a-zA-Z0-9_-]*$
     * The name can not be empty.
     * The returning OreConfig is not saved to disk, you must do it manuel with save saveOreConfig(OreConfig)
     *
     * @param name          of the OreConfig
     * @param material      of the OreConfig
     * @param oreGenerator  of the OreConfig
     * @param blockSelector of the OreConfig
     * @return a new OreConfig with the given values
     * @throws IllegalArgumentException if name, material, oreGenerator or blockSelector are null
     * @throws IllegalArgumentException if the name doesn't match the Regex: ^[a-zA-Z0-9_-]*$
     * @throws IllegalArgumentException if the name is empty
     * @throws IllegalArgumentException if material is not a block
     * @throws IllegalArgumentException if oreGenerator is not registered
     * @throws IllegalArgumentException if blockSelector is not registered
     */
    @NotNull
    OreConfig createOreConfig(@NotNull String name, @NotNull Material material, @NotNull OreGenerator oreGenerator, @NotNull BlockSelector blockSelector);

    /**
     * Saves the given OreConfig to disk
     *
     * @param config to save
     */
    void saveOreConfig(@NotNull OreConfig config);

    /**
     * @return a new set with all OreConfigs's
     */
    @NotNull
    Set<OreConfig> getOreConfigs();

    /**
     * Returns a seeded random, which is based on a seed
     * and the position of the chunk.
     *
     * @param seed to use
     * @param x    position of the chunk
     * @param z    position of the chunk
     * @return
     */
    @NotNull
    Random createRandom(long seed, int x, int z);

    /**
     * @return the logger of the CustomOreGeneratorService
     */
    @NotNull
    Logger getLogger();

}
