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

package de.derfrzocker.custom.ore.generator.impl.v1_8_R1.oregenerator;

import com.google.common.base.Predicate;
import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractMinableGenerator;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.ChunkAccessImpl;
import de.derfrzocker.spigot.utils.NumberUtil;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MinableGenerator_v1_8_R1 extends AbstractMinableGenerator {

    /**
     * The infoFunction gives the name of the OreGenerator as value.
     * The oreSettingInfo gives the name of the OreGenerator and the OreSetting as values.
     *
     * @param infoFunction   function to get the info object of this OreGenerator
     * @param oreSettingInfo biFunction to get the info object of a given OreSetting
     * @throws IllegalArgumentException if one of the arguments are null
     */
    public MinableGenerator_v1_8_R1(@NotNull Function<String, Info> infoFunction, @NotNull BiFunction<String, OreSetting, Info> oreSettingInfo) {
        super(infoFunction, oreSettingInfo);
    }

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = NumberUtil.getInt(config.getOreGeneratorOreSettings().getValue(VEIN_SIZE).orElse(0d), random);

        if (veinSize == 0)
            return;

        final WorldServer worldServer = ((ChunkAccessImpl) chunkAccess).getWorldServer();

        final IBlockData blockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();
        final Set<Block> blocks = new HashSet<>();

        replaceMaterials.forEach(material -> blocks.add(CraftMagicNumbers.getBlock(material)));
        final WorldGenMinable generator = new WorldGenMinable(blockData, veinSize, getPredicate(blocks));
        final BlockPosition chunkPosition = new BlockPosition(x << 4, 0, z << 4);


        for (final Location location : locations) {
            generator.generate(worldServer, random, chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }

    }

    private Predicate<IBlockData> getPredicate(@NotNull final Set<Block> blocks) {
        return (value) -> {
            if (value == null) {
                return false;
            } else {
                final Block block = value.getBlock();
                return blocks.contains(block);
            }
        };
    }

}
