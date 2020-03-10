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
import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.spigot.utils.NumberUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class RootGenerator extends AbstractOreGenerator {

    private final static OreSetting ROOT_LENGTH = OreSetting.createOreSetting("ROOT_LENGTH");
    private final static OreSetting CONTINUE_CHANGE = OreSetting.createOreSetting("CONTINUE_CHANGE");
    private final static OreSetting CONTINUE_TRIES = OreSetting.createOreSetting("CONTINUE_TRIES");
    private final static OreSetting MINIMUM_LENGTH_SUBTRACTION = OreSetting.createOreSetting("MINIMUM_LENGTH_SUBTRACTION");
    private final static OreSetting LENGTH_SUBTRACTION_RANGE = OreSetting.createOreSetting("LENGTH_SUBTRACTION_RANGE");
    private final static OreSetting MINIMUM_CHANGE_SUBTRACTION = OreSetting.createOreSetting("MINIMUM_CHANGE_SUBTRACTION");
    private final static OreSetting CHANGE_SUBTRACTION_RANGE = OreSetting.createOreSetting("CHANGE_SUBTRACTION_RANGE");
    private final static OreSetting MINIMUM_TRIES_SUBTRACTION = OreSetting.createOreSetting("MINIMUM_TRIES_SUBTRACTION");
    private final static OreSetting TRIES_SUBTRACTION_RANGE = OreSetting.createOreSetting("TRIES_SUBTRACTION_RANGE");
    private final static OreSetting DOWN_CHANGE = OreSetting.createOreSetting("DOWN_CHANGE");
    private final static Set<OreSetting> NEEDED_ORE_SETTINGS = Collections.unmodifiableSet(Sets.newHashSet(ROOT_LENGTH, CONTINUE_CHANGE, CONTINUE_TRIES, MINIMUM_LENGTH_SUBTRACTION, LENGTH_SUBTRACTION_RANGE, MINIMUM_CHANGE_SUBTRACTION, CHANGE_SUBTRACTION_RANGE, MINIMUM_TRIES_SUBTRACTION, TRIES_SUBTRACTION_RANGE, DOWN_CHANGE));
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

    public RootGenerator(@NotNull final Info info) {
        super("ROOT_GENERATOR", NEEDED_ORE_SETTINGS, info);
    }

    public RootGenerator(@NotNull final Function<String, Info> infoFunction) {
        super("ROOT_GENERATOR", NEEDED_ORE_SETTINGS, infoFunction);
    }

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final Location chunkLocation = new Location(null, x << 4, 0, z << 4);
        final Material material = config.getMaterial();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();

        final int rootLength = NumberUtil.getInt(config.getValue(ROOT_LENGTH).orElse(0d), random);
        final int continueChange = NumberUtil.getInt(config.getValue(CONTINUE_CHANGE).orElse(0d), random);
        final int continueTries = NumberUtil.getInt(config.getValue(CONTINUE_TRIES).orElse(0d), random);
        final int minimumLengthSubtraction = NumberUtil.getInt(config.getValue(MINIMUM_LENGTH_SUBTRACTION).orElse(0d), random);
        final int lengthSubtractionRange = NumberUtil.getInt(config.getValue(LENGTH_SUBTRACTION_RANGE).orElse(0d), random);
        final int minimumChangeSubtraction = NumberUtil.getInt(config.getValue(MINIMUM_CHANGE_SUBTRACTION).orElse(0d), random);
        final int changeSubtractionRange = NumberUtil.getInt(config.getValue(CHANGE_SUBTRACTION_RANGE).orElse(0d), random);
        final int minimumTriesSubtraction = NumberUtil.getInt(config.getValue(MINIMUM_TRIES_SUBTRACTION).orElse(0d), random);
        final int triesSubtractionRange = NumberUtil.getInt(config.getValue(TRIES_SUBTRACTION_RANGE).orElse(0d), random);
        final int downChange = NumberUtil.getInt(config.getValue(DOWN_CHANGE).orElse(0d), random);

        for (final Location location : locations) {
            final int xPosition = chunkLocation.getBlockX() + location.getBlockX();
            final int yPosition = chunkLocation.getBlockY() + location.getBlockY();
            final int zPosition = chunkLocation.getBlockZ() + location.getBlockZ();

            if (replaceMaterials.contains(chunkAccess.getMaterial(xPosition, yPosition, zPosition))) {
                chunkAccess.setMaterial(material, xPosition, yPosition, zPosition);
            }

            final Location startLocation = new Location(null, xPosition, yPosition, zPosition);

            generateRoot(material, replaceMaterials, chunkAccess, startLocation, random, continueTries, continueChange, rootLength, minimumTriesSubtraction, triesSubtractionRange, minimumChangeSubtraction, changeSubtractionRange, minimumLengthSubtraction, lengthSubtractionRange, downChange);
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

        if (oreSetting == CONTINUE_CHANGE) {
            return value >= 0 && value <= 100;
        }

        if (oreSetting == DOWN_CHANGE) {
            return value >= 0 && value <= 100;
        }

        if (oreSetting == CONTINUE_TRIES) {
            return value >= 0;
        }

        if (oreSetting == LENGTH_SUBTRACTION_RANGE) {
            return value >= 1;
        }

        if (oreSetting == CHANGE_SUBTRACTION_RANGE) {
            return value >= 1;
        }


        if (oreSetting == TRIES_SUBTRACTION_RANGE) {
            return value >= 1;
        }

        return true;
    }


    private void generateRoot(final Material setMaterial, final Set<Material> replaceMaterials, final ChunkAccess chunkAccess, final Location startLocation, final Random random, final int tries, final int change, final int length, final int triesDeMin, final int triesDeRange, final int changeDeMin, final int changeDeRange, final int lengthDeMin, final int lengthDeRange, final int downChange) {
        for (int i = 0; i < tries; i++) {
            final int con = random.nextInt(100);
            if (con < change) {
                Location newLocation = startLocation.clone();
                int newTries = tries;
                int newChange = change;
                int newLength = length;
                length:
                for (int i2 = 0; i2 < length; i2++) {
                    final BlockFace blockFace = DIRECTIONS.get(random.nextInt(DIRECTIONS.size()));
                    final Location tempNewLocation = newLocation.clone().add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());

                    if (random.nextInt(100) < downChange) {
                        tempNewLocation.add(0, -1, 0);
                    }

                    if (replaceMaterials.contains(chunkAccess.getMaterial(tempNewLocation.getBlockX(), tempNewLocation.getBlockY(), tempNewLocation.getBlockZ()))) {
                        newLocation = tempNewLocation;
                        chunkAccess.setMaterial(setMaterial, newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ());
                        final int tempNewTries = newTries - triesDeMin - (triesDeRange <= 0 ? 0 : random.nextInt(triesDeRange));
                        final int tempNewChange = newChange - changeDeMin - (changeDeRange <= 0 ? 0 : random.nextInt(changeDeRange));
                        final int tempNewLength = newLength - lengthDeMin - (lengthDeRange <= 0 ? 0 : random.nextInt(lengthDeRange));

                        if (tempNewTries <= 0) {
                            continue length;
                        }

                        if (tempNewChange <= 0) {
                            continue length;
                        }

                        if (tempNewLength <= 0) {
                            continue length;
                        }

                        newTries = tempNewTries;
                        newChange = tempNewChange;
                        newLength = tempNewLength;

                        generateRoot(setMaterial, replaceMaterials, chunkAccess, newLocation, random, newTries, newChange, newLength, triesDeMin, triesDeRange, changeDeMin, changeDeRange, lengthDeMin, lengthDeRange, downChange);
                    }

                }
            }

        }
    }

}
