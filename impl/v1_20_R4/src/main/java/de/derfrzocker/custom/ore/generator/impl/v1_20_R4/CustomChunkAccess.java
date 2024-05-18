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

package de.derfrzocker.custom.ore.generator.impl.v1_20_R4;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEventListenerRegistry;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickContainerAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CustomChunkAccess extends ChunkAccess {

    private final ChunkAccess delegate;
    private final Consumer<BlockPos> blockSet;
    private final Registry<Biome> biomes;

    public CustomChunkAccess(ChunkAccess delegate, Consumer<BlockPos> blockSet, Registry<Biome> biomes) {
        super(delegate.getPos(), delegate.getUpgradeData(), delegate.getHeightAccessorForGeneration(), delegate.biomeRegistry, delegate.getInhabitedTime(), null, null);
        this.delegate = delegate;
        this.blockSet = blockSet;
        this.biomes = biomes;
    }

    @Override
    public int getLightEmission(BlockPos blockposition) {
        return delegate.getLightEmission(blockposition);
    }

    @Override
    public int getMaxLightLevel() {
        return delegate.getMaxLightLevel();
    }

    @Override
    public BlockHitResult isBlockInLine(ClipBlockStateContext clipblockstatecontext) {
        return delegate.isBlockInLine(clipblockstatecontext);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape voxelshape, Supplier<VoxelShape> supplier) {
        return delegate.getBlockFloorHeight(voxelshape, supplier);
    }

    @Override
    public double getBlockFloorHeight(BlockPos blockposition) {
        return delegate.getBlockFloorHeight(blockposition);
    }

    public static <T, C> T traverseBlocks(Vec3 vec3d, Vec3 vec3d1, C c0, BiFunction<C, BlockPos, T> bifunction, Function<C, T> function) {
        return BlockGetter.traverseBlocks(vec3d, vec3d1, c0, bifunction, function);
    }

    @Override
    public int getMaxBuildHeight() {
        return delegate.getMaxBuildHeight();
    }

    @Override
    public int getSectionsCount() {
        return delegate.getSectionsCount();
    }

    @Override
    public int getMinSection() {
        return delegate.getMinSection();
    }

    @Override
    public int getMaxSection() {
        return delegate.getMaxSection();
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos var0) {
        return delegate.isOutsideBuildHeight(var0);
    }

    @Override
    public boolean isOutsideBuildHeight(int var0) {
        return delegate.isOutsideBuildHeight(var0);
    }

    @Override
    public int getSectionIndex(int var0) {
        return delegate.getSectionIndex(var0);
    }

    @Override
    public int getSectionIndexFromSectionY(int var0) {
        return delegate.getSectionIndexFromSectionY(var0);
    }

    public static LevelHeightAccessor create(int var0, int var1) {
        return LevelHeightAccessor.create(var0, var1);
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
    public void initializeLightSources() {
        delegate.initializeLightSources();
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
    public LevelChunkSection getSection(int i) {
        return new LevelChunkSectionOverride(delegate.getSection(i), delegate, blockSet, biomes, i);
    }

    @Override
    public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
        return delegate.getHeightmaps();
    }

    @Override
    public void setHeightmap(Heightmap.Types heightmap_type, long[] along) {
        delegate.setHeightmap(heightmap_type, along);
    }

    @Override
    @Deprecated
    public int getHighestSectionPosition() {
        return delegate.getHighestSectionPosition();
    }

    @Override
    public ChunkSkyLightSources getSkyLightSources() {
        return delegate.getSkyLightSources();
    }

    @Override
    public Stream<BlockState> getBlockStates(AABB axisalignedbb) {
        return delegate.getBlockStates(axisalignedbb);
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos blockposition, BlockEntityType<T> tileentitytypes) {
        return delegate.getBlockEntity(blockposition, tileentitytypes);
    }

    @Override
    public int getSectionYFromSectionIndex(int var0) {
        return delegate.getSectionYFromSectionIndex(var0);
    }

    @Override
    public ChunkStatus getHighestGeneratedStatus() {
        return delegate.getHighestGeneratedStatus();
    }

    @Override
    public GameEventListenerRegistry getListenerRegistry(int i) {
        return delegate.getListenerRegistry(i);
    }

    @Override
    public int getHighestFilledSectionIndex() {
        return delegate.getHighestFilledSectionIndex();
    }

    @Override
    public void findBlocks(Predicate<BlockState> predicate, BiConsumer<BlockPos, BlockState> biconsumer) {
        delegate.findBlocks(predicate, biconsumer);
    }

    @Override
    public void setBlockEntityNbt(CompoundTag nbttagcompound) {
        delegate.setBlockEntityNbt(nbttagcompound);
    }

    @Override
    public BlockHitResult clip(ClipContext raytrace) {
        return delegate.clip(raytrace);
    }

    @Override
    public BlockHitResult clip(ClipContext raytrace1, BlockPos blockposition) {
        return delegate.clip(raytrace1, blockposition);
    }

    @Override
    public @javax.annotation.Nullable BlockHitResult clipWithInteractionOverride(Vec3 vec3d, Vec3 vec3d1, BlockPos blockposition, VoxelShape voxelshape, BlockState iblockdata) {
        return delegate.clipWithInteractionOverride(vec3d, vec3d1, blockposition, voxelshape, iblockdata);
    }



    @Override
    public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types types) {
        return delegate.getOrCreateHeightmapUnprimed(types);
    }

    @Override
    public boolean hasPrimedHeightmap(Heightmap.Types heightmap_type) {
        return delegate.hasPrimedHeightmap(heightmap_type);
    }

    @Override
    public int getHeight(Heightmap.Types types, int i, int i1) {
        return delegate.getHeight(types, i, i1);
    }

    @Override
    public ChunkPos getPos() {
        return delegate.getPos();
    }

    @Override
    public @javax.annotation.Nullable StructureStart getStartForStructure(Structure structure) {
        return delegate.getStartForStructure(structure);
    }

    @Override
    public void setStartForStructure(Structure structure, StructureStart structurestart) {
        super.setStartForStructure(structure, structurestart);
    }

    @Override
    public Map<Structure, StructureStart> getAllStarts() {
        return delegate.getAllStarts();
    }

    @Override
    public void setAllStarts(Map<Structure, StructureStart> map) {
        delegate.setAllStarts(map);
    }

    @Override
    public LongSet getReferencesForStructure(Structure structure) {
        return delegate.getReferencesForStructure(structure);
    }

    @Override
    public void addReferenceForStructure(Structure structureFeature, long i) {
        delegate.addReferenceForStructure(structureFeature, i);
    }

    @Override
    public Map<Structure, LongSet> getAllReferences() {
        return delegate.getAllReferences();
    }

    @Override
    public void setAllReferences(Map<Structure, LongSet> map) {
        delegate.setAllReferences(map);
    }

    @Override
    public boolean isYSpaceEmpty(int i, int j) {
        return delegate.isYSpaceEmpty(i, j);
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
    public void markPosForPostprocessing(BlockPos blockposition) {
        delegate.markPosForPostprocessing(blockposition);
    }

    @Override
    public ShortList[] getPostProcessing() {
        return delegate.getPostProcessing();
    }

    @Override
    public void addPackedPostProcess(short short0, int i) {
        delegate.addPackedPostProcess(short0, i);
    }

    @Nullable
    @Override
    public CompoundTag getBlockEntityNbt(BlockPos blockPos) {
        return delegate.getBlockEntityNbt(blockPos);
    }

    @Nullable
    @Override
    public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos, HolderLookup.Provider provider) {
        return delegate.getBlockEntityNbtForSaving(blockPos, provider);
    }

    @Override
    public TickContainerAccess<Block> getBlockTicks() {
        return delegate.getBlockTicks();
    }

    @Override
    public TickContainerAccess<Fluid> getFluidTicks() {
        return delegate.getFluidTicks();
    }

    @Override
    public TicksToSave getTicksForSerialization() {
        return delegate.getTicksForSerialization();
    }

    @Override
    public UpgradeData getUpgradeData() {
        return delegate.getUpgradeData();
    }

    @Override
    public boolean isOldNoiseGeneration() {
        return delegate.isOldNoiseGeneration();
    }

    @Override
    public @javax.annotation.Nullable
    BlendingData getBlendingData() {
        return delegate.getBlendingData();
    }

    @Override
    public void setBlendingData(BlendingData blendingdata) {
        delegate.setBlendingData(blendingdata);
    }

    @Override
    public long getInhabitedTime() {
        return delegate.getInhabitedTime();
    }

    @Override
    public void incrementInhabitedTime(long i) {
        delegate.incrementInhabitedTime(i);
    }

    @Override
    public void setInhabitedTime(long l) {
        delegate.setInhabitedTime(l);
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
    public int getMinBuildHeight() {
        return delegate.getMinBuildHeight();
    }

    @Override
    public int getHeight() {
        return delegate.getHeight();
    }

    @Override
    public NoiseChunk getOrCreateNoiseChunk(Function<ChunkAccess, NoiseChunk> function) {
        return delegate.getOrCreateNoiseChunk(function);
    }

    @Override
    public BiomeGenerationSettings carverBiome(Supplier<BiomeGenerationSettings> supplier) {
        return delegate.carverBiome(supplier);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int i, int j, int k) {
        return delegate.getNoiseBiome(i, j, k);
    }

    @Override
    public void setBiome(int i, int j, int k, Holder<Biome> biome) {
        delegate.setBiome(i, j, k, biome);
    }

    @Override
    public void fillBiomesFromNoise(BiomeResolver biomeresolver, Climate.Sampler climate_sampler) {
        delegate.fillBiomesFromNoise(biomeresolver, climate_sampler);
    }

    @Override
    public boolean hasAnyStructureReferences() {
        return delegate.hasAnyStructureReferences();
    }

    @Override
    public @javax.annotation.Nullable
    BelowZeroRetrogen getBelowZeroRetrogen() {
        return delegate.getBelowZeroRetrogen();
    }

    @Override
    public boolean isUpgrading() {
        return delegate.isUpgrading();
    }

    @Override
    public LevelHeightAccessor getHeightAccessorForGeneration() {
        return delegate.getHeightAccessorForGeneration();
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
}
