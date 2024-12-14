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

package de.derfrzocker.custom.ore.generator.impl.v1_21_R3;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R3.util.BlockStateListPopulator;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

public class ChunkAccessImpl extends BlockStateListPopulator implements ChunkAccess {

    private final Set<BlockPos> blockPosSet = new HashSet<>();
    private final Consumer<BlockPos> blockSet = blockPosSet::add;

    public ChunkAccessImpl(LevelAccessor world) {
        super(world);
    }

    @Override
    public void setMaterial(@NotNull Material material, int x, int y, int z) {
        setBlock(new BlockPos(x, y, z), ((CraftBlockData) material.createBlockData()).getState(), 3);
    }

    @NotNull
    @Override
    public Material getMaterial(int x, int y, int z) {
        return CraftMagicNumbers.getMaterial(getBlockState(new BlockPos(x, y, z)).getBlock());
    }

    @Override
    public int getHeight(Heightmap.Types type, int i, int i1) {
        return getWorld().getHeight(type, i, i1);
    }

    public net.minecraft.world.level.chunk.ChunkAccess getChunk(int i, int j) {
        return this.getChunk(i, j, ChunkStatus.EMPTY);
    }

    @Override
    public Set<BlockPos> getBlocks() {
        Set<BlockPos> blockPos = new HashSet<>(super.getBlocks());
        blockPos.addAll(blockPosSet);
        return blockPos;
    }

    @Override
    public net.minecraft.world.level.chunk.ChunkAccess getChunk(int i, int i1, ChunkStatus cs, boolean bln) {
        net.minecraft.world.level.chunk.ChunkAccess chunkAccess = getWorld().getChunk(i, i1, cs, bln);
        return new CustomChunkAccess(chunkAccess, blockSet, getWorld().getServer().registryAccess().lookupOrThrow(Registries.BIOME));
    }
}
