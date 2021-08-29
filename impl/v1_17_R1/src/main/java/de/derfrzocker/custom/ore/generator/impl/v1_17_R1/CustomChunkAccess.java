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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongSet;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.shorts.ShortList;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CustomChunkAccess implements ChunkAccess {

    private final ChunkAccess delegate;
    private final Consumer<BlockPos> blockSet;

    public CustomChunkAccess(ChunkAccess delegate, Consumer<BlockPos> blockSet) {
        this.delegate = delegate;
        this.blockSet = blockSet;
    }

    @Override
    public LevelChunkSection getOrCreateSection(int index) {
        LevelChunkSection[] sections = this.getSections();
        if (sections[index] == LevelChunk.EMPTY_SECTION) {
            sections[index] = new LevelChunkSection(this.getSectionYFromSectionIndex(index));
        }

        return new LevelChunkSectionOverride(sections[index], (ProtoChunk) delegate, blockSet);
    }

    @Nullable
    @Override
    public BlockState setBlockState(BlockPos blockPos, BlockState blockState, boolean b) {
        return delegate.setBlockState(blockPos, blockState, b);
    }

    @Override
    public void setBlockEntity(BlockEntity blockEntity) {
        delegate.setBlockEntity(blockEntity);
    }

    @Override
    public void addEntity(Entity entity) {
        delegate.addEntity(entity);
    }

    @Override
    public Set<BlockPos> getBlockEntitiesPos() {
        return delegate.getBlockEntitiesPos();
    }

    @Override
    public LevelChunkSection[] getSections() {
        return delegate.getSections();
    }

    @Override
    public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
        return delegate.getHeightmaps();
    }

    @Override
    public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types types) {
        return delegate.getOrCreateHeightmapUnprimed(types);
    }

    @Override
    public int getHeight(Heightmap.Types types, int i, int i1) {
        return delegate.getHeight(types, i, i1);
    }

    @Override
    public BlockPos getHeighestPosition(Heightmap.Types types) {
        return delegate.getHeighestPosition(types);
    }

    @Override
    public ChunkPos getPos() {
        return delegate.getPos();
    }

    @Override
    public Map<StructureFeature<?>, StructureStart<?>> getAllStarts() {
        return delegate.getAllStarts();
    }

    @Override
    public void setAllStarts(Map<StructureFeature<?>, StructureStart<?>> map) {
        delegate.setAllStarts(map);
    }

    @Nullable
    @Override
    public ChunkBiomeContainer getBiomes() {
        return delegate.getBiomes();
    }

    @Override
    public void setUnsaved(boolean b) {
        delegate.setUnsaved(b);
    }

    @Override
    public boolean isUnsaved() {
        return delegate.isUnsaved();
    }

    @Override
    public ChunkStatus getStatus() {
        return delegate.getStatus();
    }

    @Override
    public void removeBlockEntity(BlockPos blockPos) {
        delegate.removeBlockEntity(blockPos);
    }

    @Override
    public ShortList[] k() {
        return delegate.k();
    }

    @Nullable
    @Override
    public CompoundTag getBlockEntityNbt(BlockPos blockPos) {
        return delegate.getBlockEntityNbt(blockPos);
    }

    @Nullable
    @Override
    public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos) {
        return delegate.getBlockEntityNbtForSaving(blockPos);
    }

    @Override
    public Stream<BlockPos> getLights() {
        return delegate.getLights();
    }

    @Override
    public TickList<Block> getBlockTicks() {
        return delegate.getBlockTicks();
    }

    @Override
    public TickList<Fluid> getLiquidTicks() {
        return delegate.getLiquidTicks();
    }

    @Override
    public UpgradeData getUpgradeData() {
        return delegate.getUpgradeData();
    }

    @Override
    public void setInhabitedTime(long l) {
        delegate.setInhabitedTime(l);
    }

    @Override
    public long getInhabitedTime() {
        return delegate.getInhabitedTime();
    }

    @Override
    public boolean isLightCorrect() {
        return delegate.isLightCorrect();
    }

    @Override
    public void setLightCorrect(boolean b) {
        delegate.setLightCorrect(b);
    }

    @Override
    public @javax.annotation.Nullable
    BlockEntity getBlockEntity(BlockPos blockPos) {
        return delegate.getBlockEntity(blockPos);
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos) {
        return delegate.getBlockState(blockPos);
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos) {
        return delegate.getFluidState(blockPos);
    }

    @Override
    public int getHeight() {
        return delegate.getHeight();
    }

    @Override
    public int getMinBuildHeight() {
        return delegate.getMinBuildHeight();
    }

    @Nullable
    @Override
    public StructureStart<?> getStartForFeature(StructureFeature<?> structureFeature) {
        return delegate.getStartForFeature(structureFeature);
    }

    @Override
    public void setStartForFeature(StructureFeature<?> structureFeature, StructureStart<?> structureStart) {
        delegate.setStartForFeature(structureFeature, structureStart);
    }

    @Override
    public LongSet b(StructureFeature<?> structureFeature) {
        return delegate.b(structureFeature);
    }

    @Override
    public void addReferenceForFeature(StructureFeature<?> structureFeature, long l) {
        delegate.addReferenceForFeature(structureFeature, l);
    }

    @Override
    public Map<StructureFeature<?>, LongSet> getAllReferences() {
        return delegate.getAllReferences();
    }

    @Override
    public void setAllReferences(Map<StructureFeature<?>, LongSet> map) {
        delegate.setAllReferences(map);
    }
}
