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

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class GlowStoneGenerator extends AbstractOreGenerator {

    private final static OreSetting POSITIVE_VERTICAL_SCOPE = OreSetting.createOreSetting("POSITIVE_VERTICAL_SCOPE");
    private final static OreSetting NEGATIVE_VERTICAL_SCOPE = OreSetting.createOreSetting("NEGATIVE_VERTICAL_SCOPE");
    private final static OreSetting HORIZONTAL_SCOPE = OreSetting.createOreSetting("HORIZONTAL_SCOPE");
    private final static OreSetting POSITIVE_TRIES = OreSetting.createOreSetting("POSITIVE_TRIES");
    private final static OreSetting NEGATIVE_TRIES = OreSetting.createOreSetting("NEGATIVE_TRIES");
    private final static OreSetting CONNECTIONS = OreSetting.createOreSetting("CONNECTIONS");
    private final static Set<OreSetting> NEEDED_ORE_SETTINGS = Collections.unmodifiableSet(Sets.newHashSet(POSITIVE_VERTICAL_SCOPE, NEGATIVE_VERTICAL_SCOPE, HORIZONTAL_SCOPE, POSITIVE_TRIES, NEGATIVE_TRIES, CONNECTIONS));
    private final static BlockFace[] BLOCK_FACES = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH};

    public GlowStoneGenerator(@NotNull final Function<String, Info> infoFunction) {
        super("GLOW_STONE_GENERATOR", NEEDED_ORE_SETTINGS, infoFunction);
    }

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final Location chunkLocation = new Location(null, x << 4, 0, z << 4);
        final Material material = config.getMaterial();
        final Set<Material> replaceMaterial = config.getReplaceMaterials();

        final int positiveVerticalScope = NumberUtil.getInt(config.getValue(POSITIVE_VERTICAL_SCOPE).orElse(0d), random);
        final int negativeVerticalScope = NumberUtil.getInt(config.getValue(NEGATIVE_VERTICAL_SCOPE).orElse(12d), random);
        final int horizontalScope = NumberUtil.getInt(config.getValue(HORIZONTAL_SCOPE).orElse(8d), random);
        final int positiveTries = NumberUtil.getInt(config.getValue(POSITIVE_TRIES).orElse(0d), random);
        final int negativeTries = NumberUtil.getInt(config.getValue(NEGATIVE_TRIES).orElse(1500d), random);
        final int connections = NumberUtil.getInt(config.getValue(CONNECTIONS).orElse(1d), random);

        for (final Location location : locations) {
            final int xPosition = chunkLocation.getBlockX() + location.getBlockX();
            final int yPosition = chunkLocation.getBlockY() + location.getBlockY();
            final int zPosition = chunkLocation.getBlockZ() + location.getBlockZ();

            chunkAccess.setMaterial(material, xPosition, yPosition, zPosition);

            for (int trie = 0; trie < positiveTries; ++trie) {
                final int yLocation = yPosition + (positiveVerticalScope == 0 ? 0 : random.nextInt(positiveVerticalScope));
                generate(chunkAccess, random, material, replaceMaterial, horizontalScope, connections, xPosition, zPosition, yLocation);
            }

            for (int trie = 0; trie < negativeTries; ++trie) {
                final int yLocation = yPosition - (negativeVerticalScope == 0 ? 0 : random.nextInt(negativeVerticalScope));
                generate(chunkAccess, random, material, replaceMaterial, horizontalScope, connections, xPosition, zPosition, yLocation);
            }

        }

    }

    private void generate(@NotNull final ChunkAccess chunkAccess, @NotNull final Random random, final Material material, final Set<Material> replaceMaterial, final int horizontalScope, final int connections, final int xPosition, final int zPosition, final int yLocation) {
        final int xLocation = xPosition + random.nextInt(horizontalScope) - random.nextInt(horizontalScope);
        final int zLocation = zPosition + random.nextInt(horizontalScope) - random.nextInt(horizontalScope);

        if (!replaceMaterial.contains(chunkAccess.getMaterial(xLocation, yLocation, zLocation)))
            return;

        int counter = 0;
        for (final BlockFace blockFace : BLOCK_FACES) {
            if (chunkAccess.getMaterial(xLocation + blockFace.getModX(), yLocation + blockFace.getModY(), zLocation + blockFace.getModZ()) == material)
                counter++;

            if (counter > connections)
                break;
        }

        if (connections == 0 && counter == 0) {
            chunkAccess.setMaterial(material, xLocation, yLocation, zLocation);
        } else if (counter <= connections && counter != 0) {
            chunkAccess.setMaterial(material, xLocation, yLocation, zLocation);
        }
    }

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, final double value, @NotNull final OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(NEEDED_ORE_SETTINGS.contains(oreSetting), "The OreGenerator '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        if (oreSetting == POSITIVE_VERTICAL_SCOPE) {
            return value >= 1;
        }

        if (oreSetting == NEGATIVE_VERTICAL_SCOPE) {
            return value >= 0;
        }

        if (oreSetting == HORIZONTAL_SCOPE) {
            return value >= 0;
        }

        if (oreSetting == POSITIVE_TRIES) {
            return value >= 0;
        }

        if (oreSetting == NEGATIVE_TRIES) {
            return value >= 0;
        }


        if (oreSetting == CONNECTIONS) {
            return value >= 1;
        }

        throw new RuntimeException("Wtf?");
    }

}
