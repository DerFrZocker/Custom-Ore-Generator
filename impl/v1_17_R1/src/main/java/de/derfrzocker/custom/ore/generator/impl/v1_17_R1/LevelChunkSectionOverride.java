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

package de.derfrzocker.custom.ore.generator.impl.v1_17_R1;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class LevelChunkSectionOverride extends LevelChunkSection {

    private final ProtoChunk protoChunk;
    private final LevelChunkSection delegate;
    private final Consumer<BlockPos> blockSet;

    public LevelChunkSectionOverride(LevelChunkSection delegate, ProtoChunk protoChunk, Consumer<BlockPos> blockSet) {
        super(0);
        this.delegate = delegate;
        this.protoChunk = protoChunk;
        this.blockSet = blockSet;
    }

    @Override
    public BlockState getBlockState(int var0, int var1, int var2) {
        return delegate.getBlockState(var0, var1, var2);
    }

    @Override
    public FluidState getFluidState(int var0, int var1, int var2) {
        return delegate.getFluidState(var0, var1, var2);
    }

    @Override
    public void acquire() {
        delegate.acquire();
    }

    @Override
    public void release() {
        delegate.release();
    }

    @Override
    public BlockState setBlockState(int var0, int var1, int var2, BlockState var3) {
        return setBlockState(var0, var1, var2, var3, true);
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState blockState, boolean var4) {
        BlockPos blockPos = new BlockPos(protoChunk.getPos().getBlockX(x), y + bottomBlockY(), protoChunk.getPos().getBlockZ(z));
        if (blockState.getLightEmission() > 0) {
            protoChunk.addLight(blockPos);
        }

        BlockState old = delegate.setBlockState(x, y, z, blockState, var4);
        if (blockState.hasBlockEntity()) {
            if (protoChunk.getStatus().getChunkType() == ChunkStatus.ChunkType.LEVELCHUNK) {
                BlockEntity tileentity = ((EntityBlock) blockState.getBlock()).newBlockEntity(blockPos, blockState);
                if (tileentity != null) {
                    protoChunk.setBlockEntity(tileentity);
                } else {
                    protoChunk.removeBlockEntity(blockPos);
                }
            } else {
                CompoundTag nbttagcompound = new CompoundTag();
                nbttagcompound.putInt("x", blockPos.getX());
                nbttagcompound.putInt("y", blockPos.getY());
                nbttagcompound.putInt("z", blockPos.getZ());
                nbttagcompound.putString("id", "DUMMY");
                protoChunk.setBlockEntityNbt(nbttagcompound);
            }
        } else if (old != null && old.hasBlockEntity()) {
            protoChunk.removeBlockEntity(blockPos);
        }

        blockSet.accept(blockPos);

        return old;
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean isRandomlyTicking() {
        return delegate.isRandomlyTicking();
    }

    @Override
    public boolean isRandomlyTickingBlocks() {
        return delegate.isRandomlyTickingBlocks();
    }

    @Override
    public boolean isRandomlyTickingFluids() {
        return delegate.isRandomlyTickingFluids();
    }

    @Override
    public int bottomBlockY() {
        return delegate.bottomBlockY();
    }

    @Override
    public void recalcBlockCounts() {
        delegate.recalcBlockCounts();
    }

    @Override
    public PalettedContainer<BlockState> getStates() {
        return delegate.getStates();
    }

    @Override
    public void read(FriendlyByteBuf var0) {
        delegate.read(var0);
    }

    @Override
    public void write(FriendlyByteBuf var0) {
        delegate.write(var0);
    }

    @Override
    public int getSerializedSize() {
        return delegate.getSerializedSize();
    }

    @Override
    public boolean maybeHas(Predicate<BlockState> var0) {
        return delegate.maybeHas(var0);
    }
}
