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

package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.*;
import de.derfrzocker.spigot.utils.NumberUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RootGenerator extends AbstractOreGenerator {

    private final static OreSetting ROOT_LENGTH = OreSetting.createOreSetting("ROOT_LENGTH");
    private final static OreSetting CONTINUE_CHANCE = OreSetting.createOreSetting("CONTINUE_CHANCE");
    private final static OreSetting CONTINUE_TRIES = OreSetting.createOreSetting("CONTINUE_TRIES");
    private final static OreSetting MINIMUM_LENGTH_SUBTRACTION = OreSetting.createOreSetting("MINIMUM_LENGTH_SUBTRACTION");
    private final static OreSetting LENGTH_SUBTRACTION_RANGE = OreSetting.createOreSetting("LENGTH_SUBTRACTION_RANGE");
    private final static OreSetting MINIMUM_CHANCE_SUBTRACTION = OreSetting.createOreSetting("MINIMUM_CHANCE_SUBTRACTION");
    private final static OreSetting CHANCE_SUBTRACTION_RANGE = OreSetting.createOreSetting("CHANCE_SUBTRACTION_RANGE");
    private final static OreSetting MINIMUM_TRIES_SUBTRACTION = OreSetting.createOreSetting("MINIMUM_TRIES_SUBTRACTION");
    private final static OreSetting TRIES_SUBTRACTION_RANGE = OreSetting.createOreSetting("TRIES_SUBTRACTION_RANGE");
    private final static OreSetting DOWN_CHANCE = OreSetting.createOreSetting("DOWN_CHANCE");
    private final static Set<OreSetting> NEEDED_ORE_SETTINGS = Collections.unmodifiableSet(Sets.newHashSet(ROOT_LENGTH, CONTINUE_CHANCE, CONTINUE_TRIES, MINIMUM_LENGTH_SUBTRACTION, LENGTH_SUBTRACTION_RANGE, MINIMUM_CHANCE_SUBTRACTION, CHANCE_SUBTRACTION_RANGE, MINIMUM_TRIES_SUBTRACTION, TRIES_SUBTRACTION_RANGE, DOWN_CHANCE));
    private final static Map<Integer, BlockFace> DIRECTIONS;

    static {
        final Map<Integer, BlockFace> blockFaces = new HashMap<>();

        blockFaces.put(0, BlockFace.NORTH);
        blockFaces.put(1, BlockFace.EAST);
        blockFaces.put(2, BlockFace.WEST);
        blockFaces.put(3, BlockFace.SOUTH);
        blockFaces.put(4, BlockFace.NORTH_EAST);
        blockFaces.put(5, BlockFace.NORTH_WEST);
        blockFaces.put(6, BlockFace.SOUTH_WEST);
        blockFaces.put(7, BlockFace.SOUTH_EAST);

        DIRECTIONS = Collections.unmodifiableMap(blockFaces);
    }

    /**
     * The infoFunction gives the name of the OreGenerator as value.
     * The oreSettingInfo gives the name of the OreGenerator and the OreSetting as values.
     *
     * @param infoFunction   function to get the info object of this OreGenerator
     * @param oreSettingInfo biFunction to get the info object of a given OreSetting
     * @throws IllegalArgumentException if one of the arguments are null
     */
    public RootGenerator(@NotNull final Function<String, Info> infoFunction, @NotNull final BiFunction<String, OreSetting, Info> oreSettingInfo) {
        super("ROOT_GENERATOR", NEEDED_ORE_SETTINGS, infoFunction, oreSettingInfo);
    }

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final Location chunkLocation = new Location(null, x << 4, 0, z << 4);
        final Material material = config.getMaterial();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();
        final OreSettingContainer oreSettingContainer = config.getOreGeneratorOreSettings();

        final int rootLength = NumberUtil.getInt(oreSettingContainer.getValue(ROOT_LENGTH).orElse(0d), random);
        final int continueChance = NumberUtil.getInt(oreSettingContainer.getValue(CONTINUE_CHANCE).orElse(0d), random);
        final int continueTries = NumberUtil.getInt(oreSettingContainer.getValue(CONTINUE_TRIES).orElse(0d), random);
        final int minimumLengthSubtraction = NumberUtil.getInt(oreSettingContainer.getValue(MINIMUM_LENGTH_SUBTRACTION).orElse(0d), random);
        final int lengthSubtractionRange = NumberUtil.getInt(oreSettingContainer.getValue(LENGTH_SUBTRACTION_RANGE).orElse(0d), random);
        final int minimumChanceSubtraction = NumberUtil.getInt(oreSettingContainer.getValue(MINIMUM_CHANCE_SUBTRACTION).orElse(0d), random);
        final int chanceSubtractionRange = NumberUtil.getInt(oreSettingContainer.getValue(CHANCE_SUBTRACTION_RANGE).orElse(0d), random);
        final int minimumTriesSubtraction = NumberUtil.getInt(oreSettingContainer.getValue(MINIMUM_TRIES_SUBTRACTION).orElse(0d), random);
        final int triesSubtractionRange = NumberUtil.getInt(oreSettingContainer.getValue(TRIES_SUBTRACTION_RANGE).orElse(0d), random);
        final int downChance = NumberUtil.getInt(oreSettingContainer.getValue(DOWN_CHANCE).orElse(0d), random);

        for (final Location location : locations) {
            final int xPosition = chunkLocation.getBlockX() + location.getBlockX();
            final int yPosition = chunkLocation.getBlockY() + location.getBlockY();
            final int zPosition = chunkLocation.getBlockZ() + location.getBlockZ();

            if (replaceMaterials.contains(chunkAccess.getMaterial(xPosition, yPosition, zPosition))) {
                chunkAccess.setMaterial(material, xPosition, yPosition, zPosition);
            }

            final Location startLocation = new Location(null, xPosition, yPosition, zPosition);

            generateRoot(material, replaceMaterials, chunkAccess, startLocation, random, continueTries, continueChance, rootLength, minimumTriesSubtraction, triesSubtractionRange, minimumChanceSubtraction, chanceSubtractionRange, minimumLengthSubtraction, lengthSubtractionRange, downChance);
        }

    }

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, double value, @NotNull OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(NEEDED_ORE_SETTINGS.contains(oreSetting), "The OreGenerator '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        if (oreSetting == ROOT_LENGTH) {
            return value >= 0;
        }

        if (oreSetting == CONTINUE_CHANCE) {
            return value >= 0 && value <= 100;
        }

        if (oreSetting == DOWN_CHANCE) {
            return value >= 0 && value <= 100;
        }

        if (oreSetting == CONTINUE_TRIES) {
            return value >= 0;
        }

        if (oreSetting == LENGTH_SUBTRACTION_RANGE) {
            return value >= 1;
        }

        if (oreSetting == CHANCE_SUBTRACTION_RANGE) {
            return value >= 1;
        }


        if (oreSetting == TRIES_SUBTRACTION_RANGE) {
            return value >= 1;
        }

        return true;
    }


    private void generateRoot(final Material setMaterial, final Set<Material> replaceMaterials, final ChunkAccess chunkAccess, final Location startLocation, final Random random, final int tries, final int chance, final int length, final int triesDeMin, final int triesDeRange, final int chanceDeMin, final int chanceDeRange, final int lengthDeMin, final int lengthDeRange, final int downchance) {
        for (int i = 0; i < tries; i++) {
            final int con = random.nextInt(100);
            if (con < chance) {
                Location newLocation = startLocation.clone();
                int newTries = tries;
                int newchance = chance;
                int newLength = length;
                length:
                for (int i2 = 0; i2 < length; i2++) {
                    final BlockFace blockFace = DIRECTIONS.get(random.nextInt(DIRECTIONS.size()));
                    final Location tempNewLocation = newLocation.clone().add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());

                    if (random.nextInt(100) < downchance) {
                        tempNewLocation.add(0, -1, 0);
                    }

                    if (replaceMaterials.contains(chunkAccess.getMaterial(tempNewLocation.getBlockX(), tempNewLocation.getBlockY(), tempNewLocation.getBlockZ()))) {
                        newLocation = tempNewLocation;
                        chunkAccess.setMaterial(setMaterial, newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ());
                        final int tempNewTries = newTries - triesDeMin - (triesDeRange <= 0 ? 0 : random.nextInt(triesDeRange));
                        final int tempNewchance = newchance - chanceDeMin - (chanceDeRange <= 0 ? 0 : random.nextInt(chanceDeRange));
                        final int tempNewLength = newLength - lengthDeMin - (lengthDeRange <= 0 ? 0 : random.nextInt(lengthDeRange));

                        if (tempNewTries <= 0) {
                            continue length;
                        }

                        if (tempNewchance <= 0) {
                            continue length;
                        }

                        if (tempNewLength <= 0) {
                            continue length;
                        }

                        newTries = tempNewTries;
                        newchance = tempNewchance;
                        newLength = tempNewLength;

                        generateRoot(setMaterial, replaceMaterials, chunkAccess, newLocation, random, newTries, newchance, newLength, triesDeMin, triesDeRange, chanceDeMin, chanceDeRange, lengthDeMin, lengthDeRange, downchance);
                    }

                }
            }

        }
    }

}
