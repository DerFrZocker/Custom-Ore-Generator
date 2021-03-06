/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
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

import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.dao.OreConfigDao;
import de.derfrzocker.custom.ore.generator.api.dao.WorldConfigDao;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CustomOreGeneratorServiceImpl implements CustomOreGeneratorService {

    private final static Pattern BLOCK_SELECTOR_NAME_PATTERN = Pattern.compile("^[A-Z_]*$");
    private final static Pattern ORE_GENERATOR_NAME_PATTERN = BLOCK_SELECTOR_NAME_PATTERN;
    private final static Pattern CUSTOM_DATA_NAME_PATTERN = ORE_GENERATOR_NAME_PATTERN;
    private final static Pattern ORE_CONFIG_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");

    @NotNull
    private final WorldConfigDao worldConfigDao;
    @NotNull
    private final OreConfigDao oreConfigDao;
    @NotNull
    private final Logger logger;
    private final Map<String, BlockSelector> blockSelectors = new HashMap<>();
    private final Map<String, OreGenerator> oreGenerators = new HashMap<>();
    private final Map<String, CustomData> customDatas = new HashMap<>();
    @Nullable
    private BlockSelector defaultBlockSelector;
    @Nullable
    private OreGenerator defaultOreGenerator;

    /**
     * Creates a new CustomOreGeneratorServiceImpl
     *
     * @param worldConfigDao to use
     * @param logger         to use
     */
    public CustomOreGeneratorServiceImpl(@NotNull final WorldConfigDao worldConfigDao, @NotNull final OreConfigDao oreConfigDao, @NotNull final Logger logger) {
        Validate.notNull(worldConfigDao, "WorldConfigDao can not be null");
        Validate.notNull(oreConfigDao, "OreConfigDao can not be null");
        Validate.notNull(logger, "Logger can not be null");

        this.worldConfigDao = worldConfigDao;
        this.oreConfigDao = oreConfigDao;
        this.logger = logger;
    }

    @NotNull
    @Override
    public Optional<BlockSelector> getBlockSelector(@NotNull final String name) {
        Validate.notNull(name, "Name can not be null");

        return Optional.ofNullable(this.blockSelectors.get(name.toUpperCase()));
    }

    @Override
    public void registerBlockSelector(@NotNull final BlockSelector blockSelector) {
        Validate.notNull(blockSelector, "BlockSelector can not be null");
        Validate.notEmpty(blockSelector.getName(), "BlockSelector name can not be empty");
        Validate.isTrue(!getBlockSelector(blockSelector.getName()).isPresent(), "BlockSelector " + blockSelector.getName() + " is already registered");
        Validate.isTrue(BLOCK_SELECTOR_NAME_PATTERN.matcher(blockSelector.getName()).matches(), "BlockSelector " + blockSelector.getName() + " does not match the regex: ^[A-Z_]*$");

        this.blockSelectors.put(blockSelector.getName(), blockSelector);
    }

    @NotNull
    @Override
    public Set<BlockSelector> getBlockSelectors() {
        return new HashSet<>(this.blockSelectors.values());
    }

    @Nullable
    @Override
    public BlockSelector getDefaultBlockSelector() {
        return this.defaultBlockSelector;
    }

    @Override
    public void setDefaultBlockSelector(@Nullable final BlockSelector blockSelector) {
        if (blockSelector == null) {
            this.defaultBlockSelector = null;
            return;
        }

        Validate.isTrue(getBlockSelector(blockSelector.getName()).isPresent(), "BlockSelector " + blockSelector.getName() + " is not registered");

        this.defaultBlockSelector = blockSelector;
    }

    @NotNull
    @Override
    public Optional<OreGenerator> getOreGenerator(@NotNull final String name) {
        Validate.notNull(name, "Name can not be null");

        return Optional.ofNullable(this.oreGenerators.get(name.toUpperCase()));
    }

    @Override
    public void registerOreGenerator(@NotNull final OreGenerator oreGenerator) {
        Validate.notNull(oreGenerator, "OreGenerator can not be null");
        Validate.notEmpty(oreGenerator.getName(), "OreGenerator name can not be empty");
        Validate.isTrue(!getOreGenerator(oreGenerator.getName()).isPresent(), "OreGenerator " + oreGenerator.getName() + " is already registered");
        Validate.isTrue(ORE_GENERATOR_NAME_PATTERN.matcher(oreGenerator.getName()).matches(), "OreGenerator " + oreGenerator.getName() + " does not match the regex: ^[A-Z_]*$");

        this.oreGenerators.put(oreGenerator.getName(), oreGenerator);
    }

    @NotNull
    @Override
    public Set<OreGenerator> getOreGenerators() {
        return new HashSet<>(this.oreGenerators.values());
    }

    @Nullable
    @Override
    public OreGenerator getDefaultOreGenerator() {
        return this.defaultOreGenerator;
    }

    @Override
    public void setDefaultOreGenerator(@Nullable final OreGenerator oreGenerator) {
        if (oreGenerator == null) {
            this.defaultOreGenerator = null;
            return;
        }

        Validate.isTrue(getOreGenerator(oreGenerator.getName()).isPresent(), "OreGenerator " + oreGenerator.getName() + " is not registered");

        this.defaultOreGenerator = oreGenerator;
    }

    @NotNull
    @Override
    public Optional<CustomData> getCustomData(@NotNull final String name) {
        Validate.notNull(name, "Name can not be null");

        return Optional.ofNullable(this.customDatas.get(name.toUpperCase()));
    }

    @Override
    public void registerCustomData(@NotNull final CustomData customData) {
        Validate.notNull(customData, "CustomData can not be null");
        Validate.notEmpty(customData.getName(), "CustomData name can not be empty");
        Validate.isTrue(!getCustomData(customData.getName()).isPresent(), "CustomData " + customData.getName() + " is already registered");
        Validate.isTrue(CUSTOM_DATA_NAME_PATTERN.matcher(customData.getName()).matches(), "CustomData " + customData.getName() + " does not match the regex: ^[A-Z_]*$");

        this.customDatas.put(customData.getName(), customData);
    }

    @NotNull
    @Override
    public Set<CustomData> getCustomData() {
        return new HashSet<>(this.customDatas.values());
    }

    @NotNull
    @Override
    public Optional<WorldConfig> getWorldConfig(@NotNull final String worldName) {
        Validate.notNull(worldName, "World name can not be null");

        return this.worldConfigDao.get(worldName);
    }

    @NotNull
    @Override
    public WorldConfig createWorldConfig(@NotNull final World world) {
        Validate.notNull(world, "World can not be null");

        return getWorldConfig(world.getName()).orElseGet(() -> new WorldConfigYamlImpl(world.getName()));
    }

    @NotNull
    @Override
    public WorldConfig createWorldConfig(@NotNull final String worldName) {
        Validate.notNull(worldName, "World name can not be null");
        Validate.notEmpty(worldName, "World name can not be empty");
        //TODO name regex check

        return getWorldConfig(worldName).orElseGet(() -> new WorldConfigYamlImpl(worldName));
    }

    @NotNull
    @Override
    public OreConfig createOreConfig(@NotNull final String name, @NotNull final Material material, @NotNull final OreGenerator oreGenerator, @NotNull final BlockSelector blockSelector) {
        Validate.notNull(name, "OreConfig name can not be null");
        Validate.notNull(material, "Material can not be null");
        Validate.notNull(oreGenerator, "OreGenerator can not be null");
        Validate.notNull(blockSelector, "BlockSelector can not be null");
        Validate.notEmpty(name, "OreConfig name can not be null");
        Validate.isTrue(material.isBlock(), "Material " + material + " is not a block");
        Validate.isTrue(getOreGenerator(oreGenerator.getName()).isPresent(), "OreGenerator " + oreGenerator.getName() + " is not registered");
        Validate.isTrue(getBlockSelector(blockSelector.getName()).isPresent(), "BlockSelector " + blockSelector.getName() + " is not registered");
        Validate.isTrue(ORE_CONFIG_NAME_PATTERN.matcher(name).matches(), "OreConfig name " + name + " does not match the regex: ^[a-zA-Z0-9_-]*$");

        return new OreConfigYamlImpl(name, material, oreGenerator.getName(), blockSelector.getName());
    }

    @Override
    public void saveOreConfig(@NotNull final OreConfig config) {
        Validate.notNull(config, "OreConfig can not be null");

        oreConfigDao.save(config);
    }

    @NotNull
    @Override
    public Set<OreConfig> getOreConfigs() {
        return oreConfigDao.getAll();
    }

    @Override
    public void saveWorldConfig(@NotNull final WorldConfig config) {
        Validate.notNull(config, "WorldConfig can not be null");

        this.worldConfigDao.save(config);
    }

    @NotNull
    @Override
    public Set<WorldConfig> getWorldConfigs() {
        return this.worldConfigDao.getAll();
    }

    @NotNull
    @Override
    public Optional<OreConfig> getOreConfig(@NotNull final String name) {
        Validate.notNull(name, "Name can not be null");
        Validate.notEmpty(name, "Name can not be empty");

        return oreConfigDao.get(name);
    }

    @NotNull
    @Override
    public Random createRandom(final long seed, final int x, final int z) {
        final Random random = new Random(seed);

        long long1 = random.nextLong();
        long long2 = random.nextLong();
        long newSeed = (long) x * long1 ^ (long) z * long2 ^ seed;
        random.setSeed(newSeed);

        return random;
    }

    @NotNull
    @Override
    public Logger getLogger() {
        return this.logger;
    }

}
