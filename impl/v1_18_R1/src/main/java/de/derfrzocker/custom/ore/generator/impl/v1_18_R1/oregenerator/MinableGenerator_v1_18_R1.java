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

package de.derfrzocker.custom.ore.generator.impl.v1_18_R1.oregenerator;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.AbstractMinableGenerator;
import de.derfrzocker.spigot.utils.NumberUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MinableGenerator_v1_18_R1 extends AbstractMinableGenerator {

    private final OreFeature generator = new OreFeature(OreConfiguration.CODEC);

    /**
     * The infoFunction gives the name of the OreGenerator as value.
     * The oreSettingInfo gives the name of the OreGenerator and the OreSetting as values.
     *
     * @param infoFunction   function to get the info object of this OreGenerator
     * @param oreSettingInfo biFunction to get the info object of a given OreSetting
     * @throws IllegalArgumentException if one of the arguments are null
     */
    public MinableGenerator_v1_18_R1(@NotNull Function<String, Info> infoFunction, @NotNull BiFunction<String, OreSetting, Info> oreSettingInfo) {
        super(infoFunction, oreSettingInfo);
    }

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = NumberUtil.getInt(config.getOreGeneratorOreSettings().getValue(VEIN_SIZE).orElse(0d), random);

        if (veinSize == 0)
            return;

        final BlockPos chunkPosition = new BlockPos(x << 4, 0, z << 4);
        final Set<Block> blocks = new HashSet<>();

        config.getReplaceMaterials().forEach(material -> blocks.add(CraftMagicNumbers.getBlock(material)));

        final OreConfiguration oreConfiguration = new OreConfiguration(new RuleTest() {
            @Override
            public boolean test(BlockState blockState, Random random) {
                return blocks.contains(blockState.getBlock());
            }

            @Override
            protected RuleTestType<?> getType() {
                return null;
            }
        }, CraftMagicNumbers.getBlock(config.getMaterial()).defaultBlockState(), veinSize);

        for (final Location location : locations) {
            generator.place(new FeaturePlaceContext<>(null, (WorldGenLevel) chunkAccess, null, random, chunkPosition.offset(location.getBlockX(), location.getBlockY(), location.getBlockZ()), oreConfiguration));
        }
    }
}
