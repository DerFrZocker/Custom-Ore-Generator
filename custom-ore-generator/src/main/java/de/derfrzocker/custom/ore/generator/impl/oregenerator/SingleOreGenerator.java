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

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public class SingleOreGenerator extends AbstractSingleOreGenerator {

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final Location chunkLocation = new Location(null, x << 4, 0, z << 4);
        final Material material = config.getMaterial();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();

        for (final Location location : locations) {
            final int xPosition = chunkLocation.getBlockX() + location.getBlockX();
            final int yPosition = chunkLocation.getBlockY() + location.getBlockY();
            final int zPosition = chunkLocation.getBlockZ() + location.getBlockZ();

            final Material blockMaterial = chunkAccess.getMaterial(xPosition, yPosition, zPosition);
            if (replaceMaterials.contains(blockMaterial))
                chunkAccess.setMaterial(material, xPosition, yPosition, zPosition);
        }
    }

}
