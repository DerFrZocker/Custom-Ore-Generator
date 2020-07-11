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

package de.derfrzocker.custom.ore.generator.impl.v1_16_R1;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import net.minecraft.server.v1_16_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GeneratorAccessOverrider implements GeneratorAccessSeed, ChunkAccess {

    @NotNull
    private final GeneratorAccessSeed parent;
    @NotNull
    private final OreConfig oreConfig;

    public GeneratorAccessOverrider(@NotNull final GeneratorAccessSeed parent, @NotNull final OreConfig oreConfig) {
        Validate.notNull(parent, "Parent GeneratorAccess can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");

        this.parent = parent;
        this.oreConfig = oreConfig;
    }

    @Override
    public void setMaterial(@NotNull org.bukkit.Material material, final int x, final int y, final int z) {
        setTypeAndData(new BlockPosition(x, y, z), CraftMagicNumbers.getBlock(material).getBlockData(), 2);
    }

    @NotNull
    @Override
    public org.bukkit.Material getMaterial(final int x, final int y, final int z) {
        return CraftMagicNumbers.getMaterial(parent.getType(new BlockPosition(x, y, z)).getBlock());
    }

    @Override
    public boolean setTypeAndData(final BlockPosition blockPosition, final IBlockData iBlockData, final int i) {
        if (z(blockPosition) instanceof ProtoChunkExtension)
            return false;

        final boolean success = parent.setTypeAndData(blockPosition, iBlockData, i);

        if (!success)
            return false;

        oreConfig.getCustomData().forEach((customData, object) -> customData.getCustomDataApplier().apply(oreConfig, blockPosition, parent));

        return true;
    }

    @Override
    public float f(float var0) {
        return 0;
    } //TODO check method


    @Override
    public boolean A(BlockPosition var0) {
        return parent.A(var0);
    }

    @Override
    public boolean a(BlockPosition blockPosition, boolean b, @Nullable Entity entity, int i) {
        return parent.a(blockPosition, b, entity, i);
    }

    @Override
    public boolean a(BlockPosition blockPosition, IBlockData iBlockData, int i, int i1) {
        return parent.a(blockPosition, iBlockData, i, i1);
    }

    @Override
    public boolean b(AxisAlignedBB var0) {
        return parent.b(var0);
    }

    @Override
    public boolean s_() {
        return parent.s_();
    }

    @Override
    public boolean x(BlockPosition var0) {
        return parent.x(var0);
    }

    @Override
    public DimensionManager getDimensionManager() {
        return parent.getDimensionManager();
    }

    @Override
    public float aa() {
        return parent.aa();
    }

    @Override
    public float y(BlockPosition var0) {
        return parent.y(var0);
    }

    @Override
    public IChunkAccess z(BlockPosition var0) {
        return parent.z(var0);
    }

    @Override
    public int getHeight() {
        return parent.getHeight();
    }

    @Override
    public Stream<IBlockData> a(AxisAlignedBB axisalignedbb) {
        return parent.a(axisalignedbb);
    }

    @Override
    public Stream<IBlockData> c(AxisAlignedBB var0) {
        return parent.c(var0);
    }

    @Override
    public Stream<VoxelShape> a(@Nullable Entity var0, AxisAlignedBB var1, BiPredicate<IBlockData, BlockPosition> var2) {
        return parent.a(var0, var1, var2);
    }

    @Override
    public Stream<VoxelShape> c(@Nullable Entity var0, AxisAlignedBB var1, Predicate<Entity> var2) {
        return parent.c(var0, var1, var2);
    }

    @Override
    public Stream<VoxelShape> d(@Nullable Entity var0, AxisAlignedBB var1, Predicate<Entity> var2) {
        return parent.d(var0, var1, var2);
    }

    @Override
    public boolean b(@Nullable Entity var0, AxisAlignedBB var1, Predicate<Entity> var2) {
        return parent.b(var0, var1, var2);
    }

    @Override
    public TickList<Block> getBlockTickList() {
        return parent.getBlockTickList();
    }

    @Override
    public TickList<FluidType> getFluidTickList() {
        return parent.getFluidTickList();
    }

    @Override
    public World getMinecraftWorld() {
        return parent.getMinecraftWorld();
    }

    @Override
    public WorldData getWorldData() {
        return parent.getWorldData();
    }

    @Override
    public DifficultyDamageScaler getDamageScaler(BlockPosition blockPosition) {
        return parent.getDamageScaler(blockPosition);
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return parent.getChunkProvider();
    }

    @Override
    public Random getRandom() {
        return parent.getRandom();
    }

    @Override
    public void update(BlockPosition blockPosition, Block block) {
        parent.update(blockPosition, block);
    }

    @Override
    public void playSound(@Nullable EntityHuman entityHuman, BlockPosition blockPosition, SoundEffect soundEffect, SoundCategory soundCategory, float v, float v1) {
        parent.playSound(entityHuman, blockPosition, soundEffect, soundCategory, v, v1);
    }

    @Override
    public void addParticle(ParticleParam particleParam, double v, double v1, double v2, double v3, double v4, double v5) {
        parent.addParticle(particleParam, v, v1, v2, v3, v4, v5);
    }

    @Override
    public void a(@Nullable EntityHuman entityHuman, int i, BlockPosition blockPosition, int i1) {
        parent.a(entityHuman, i, blockPosition, i1);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPosition blockPosition) {
        return parent.getTileEntity(blockPosition);
    }

    @Override
    public IBlockData getType(BlockPosition blockPosition) {
        return parent.getType(blockPosition);
    }

    @Override
    public Fluid getFluid(BlockPosition blockPosition) {
        return parent.getFluid(blockPosition);
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisAlignedBB, @Nullable Predicate<? super Entity> predicate) {
        return parent.getEntities(entity, axisAlignedBB, predicate);
    }

    @Override
    public <T extends Entity> List<T> a(Class<? extends T> aClass, AxisAlignedBB axisAlignedBB, @Nullable Predicate<? super T> predicate) {
        return parent.a(aClass, axisAlignedBB, predicate);
    }

    @Override
    public List<? extends EntityHuman> getPlayers() {
        return parent.getPlayers();
    }

    @Override
    public int getLightLevel(BlockPosition blockPosition, int i) {
        return parent.getLightLevel(blockPosition, i);
    }

    @Nullable
    @Override
    public IChunkAccess getChunkAt(int i, int i1, ChunkStatus chunkStatus, boolean b) {
        return parent.getChunkAt(i, i1, chunkStatus, b);
    }

    @Override
    public boolean a(BlockPosition blockPosition, Predicate<IBlockData> predicate) {
        return parent.a(blockPosition, predicate);
    }

    @Override
    public BlockPosition getHighestBlockYAt(HeightMap.Type type, BlockPosition blockPosition) {
        return parent.getHighestBlockYAt(type, blockPosition);
    }

    @Override
    public int a(HeightMap.Type type, int i, int i1) {
        return 255;
    }

    @Override
    public int c() {
        return parent.c();
    }

    @Override
    public BiomeManager d() {
        return parent.d();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return parent.getWorldBorder();
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Override
    public BiomeBase getBiome(BlockPosition blockPosition) {
        return parent.getBiome(blockPosition);
    }

    @Override
    public BiomeBase getBiome(int var0, int var1, int var2) {
        return parent.getBiome(var0, var1, var2);
    }

    @Override
    public BiomeBase a(int i, int i1, int i2) {
        return parent.a(i, i1, i2);
    }

    @Override
    public LightEngine e() {
        return parent.e();
    }

    @Override
    public int getBrightness(EnumSkyBlock enumSkyBlock, BlockPosition blockPosition) {
        return parent.getBrightness(enumSkyBlock, blockPosition);
    }

    @Override
    public boolean a(BlockPosition blockPosition, boolean b) {
        return parent.a(blockPosition, b);
    }

    @Override
    public boolean b(BlockPosition blockPosition, boolean b) {
        return parent.b(blockPosition, b);
    }

    @Override
    public boolean a(BlockPosition blockPosition, boolean b, @Nullable Entity entity) {
        return parent.a(blockPosition, b, entity);
    }

    @Override
    public int h(BlockPosition var0) {
        return parent.h(var0);
    }

    @Override
    public int getBuildHeight() {
        return parent.getBuildHeight();
    }

    @Override
    public MovingObjectPositionBlock rayTraceBlock(RayTrace raytrace1, BlockPosition blockposition) {
        return parent.rayTraceBlock(raytrace1, blockposition);
    }

    @Override
    public int H() {
        return parent.H();
    }

    @Override
    public MovingObjectPositionBlock rayTrace(RayTrace var0) {
        return parent.rayTrace(var0);
    }

    @Nullable
    @Override
    public MovingObjectPositionBlock rayTrace(Vec3D var0, Vec3D var1, BlockPosition var2, VoxelShape var3, IBlockData var4) {
        return parent.rayTrace(var0, var1, var2, var3, var4);
    }

    @Override
    public boolean a(@Nullable Entity var0, VoxelShape var1) {
        return parent.a(var0, var1);
    }

    @Override
    public boolean a(IBlockData var0, BlockPosition var1, VoxelShapeCollision var2) {
        return parent.a(var0, var1, var2);
    }

    @Override
    public boolean addEntity(Entity entity) {
        return parent.addEntity(entity);
    }

    @Override
    public boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return parent.addEntity(entity, reason);
    }

    @Override
    public boolean areChunksLoadedBetween(BlockPosition var0, BlockPosition var1) {
        return parent.areChunksLoadedBetween(var0, var1);
    }

    @Override
    public boolean containsLiquid(AxisAlignedBB var0) {
        return parent.containsLiquid(var0);
    }

    @Override
    public boolean f(BlockPosition var0) {
        return parent.f(var0);
    }

    @Override
    public boolean getCubes(Entity var0) {
        return parent.getCubes(var0);
    }

    @Override
    public boolean getCubes(Entity var0, AxisAlignedBB var1) {
        return parent.getCubes(var0, var1);
    }

    @Override
    public boolean i(Entity var0) {
        return parent.i(var0);
    }

    @Override
    public boolean isAreaLoaded(int var0, int var1, int var2, int var3, int var4, int var5) {
        return parent.isAreaLoaded(var0, var1, var2, var3, var4, var5);
    }

    @Override
    public boolean isChunkLoaded(int var0, int var1) {
        return parent.isChunkLoaded(var0, var1);
    }

    @Override
    public boolean isEmpty(BlockPosition var0) {
        return parent.isEmpty(var0);
    }

    @Override
    public boolean isLoaded(BlockPosition var0) {
        return parent.isLoaded(var0);
    }

    @Override
    public boolean isPlayerNearby(double var0, double var2, double var4, double var6) {
        return parent.isPlayerNearby(var0, var2, var4, var6);
    }

    @Nullable
    @Override
    public EntityHuman a(PathfinderTargetCondition var0, EntityLiving var1) {
        return parent.a(var0, var1);
    }

    @Nullable
    @Override
    public EntityHuman a(double var0, double var2, double var4, double var6, boolean var8) {
        return parent.a(var0, var2, var4, var6, var8);
    }

    @Nullable
    @Override
    public EntityHuman a(double var0, double var2, double var4, double var6, @Nullable Predicate<Entity> var8) {
        return parent.a(var0, var2, var4, var6, var8);
    }

    @Nullable
    @Override
    public EntityHuman a(PathfinderTargetCondition var0, double var1, double var3, double var5) {
        return parent.a(var0, var1, var3, var5);
    }

    @Nullable
    @Override
    public EntityHuman a(PathfinderTargetCondition var0, EntityLiving var1, double var2, double var4, double var6) {
        return parent.a(var0, var1, var2, var4, var6);
    }

    @Nullable
    @Override
    public EntityHuman b(UUID var0) {
        return parent.b(var0);
    }

    @Nullable
    @Override
    public EntityHuman findNearbyPlayer(Entity var0, double var1) {
        return parent.findNearbyPlayer(var0, var1);
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return parent.getDifficulty();
    }

    @Override
    public IChunkAccess getChunkAt(int var0, int var1) {
        return parent.getChunkAt(var0, var1);
    }

    @Override
    public IChunkAccess getChunkAt(int var0, int var1, ChunkStatus var2) {
        return parent.getChunkAt(var0, var1, var2);
    }

    @Nullable
    @Override
    public IBlockAccess c(int var0, int var1) {
        return parent.c(var0, var1);
    }

    @Override
    public int c(BlockPosition var0, EnumDirection var1) {
        return parent.c(var0, var1);
    }

    @Override
    public int getLightLevel(BlockPosition var0) {
        return parent.getLightLevel(var0);
    }

    @Override
    public int c(BlockPosition var0, int var1) {
        return parent.c(var0, var1);
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity var0, AxisAlignedBB var1) {
        return parent.getEntities(var0, var1);
    }

    @Override
    public List<EntityHuman> a(PathfinderTargetCondition var0, EntityLiving var1, AxisAlignedBB var2) {
        return parent.a(var0, var1, var2);
    }

    @Override
    public <T extends Entity> List<T> a(Class<? extends T> var0, AxisAlignedBB var1) {
        return parent.a(var0, var1);
    }

    @Override
    public <T extends EntityLiving> List<T> a(Class<? extends T> var0, PathfinderTargetCondition var1, EntityLiving var2, AxisAlignedBB var3) {
        return parent.a(var0, var1, var2, var3);
    }

    @Override
    public <T extends Entity> List<T> b(Class<? extends T> var0, AxisAlignedBB var1) {
        return parent.b(var0, var1);
    }

    @Override
    public <T extends Entity> List<T> b(Class<? extends T> var0, AxisAlignedBB var1, @Nullable Predicate<? super T> var2) {
        return parent.b(var0, var1, var2);
    }

    @Override
    public Stream<VoxelShape> b(@Nullable Entity var0, AxisAlignedBB var1) {
        return parent.b(var0, var1);
    }

    @Nullable
    @Override
    public <T extends EntityLiving> T a(List<? extends T> var0, PathfinderTargetCondition var1, @Nullable EntityLiving var2, double var3, double var5, double var7) {
        return parent.a(var0, var1, var2, var3, var5, var7);
    }

    @Nullable
    @Override
    public <T extends EntityLiving> T a(Class<? extends T> var0, PathfinderTargetCondition var1, @Nullable EntityLiving var2, double var3, double var5, double var7, AxisAlignedBB var9) {
        return parent.a(var0, var1, var2, var3, var5, var7, var9);
    }

    @Nullable
    @Override
    public <T extends EntityLiving> T b(Class<? extends T> var0, PathfinderTargetCondition var1, @Nullable EntityLiving var2, double var3, double var5, double var7, AxisAlignedBB var9) {
        return parent.b(var0, var1, var2, var3, var5, var7, var9);
    }

    @Override
    public void triggerEffect(int var0, BlockPosition var1, int var2) {
        parent.triggerEffect(var0, var1, var2);
    }

    @Override
    public long getSeed() {
        return parent.getSeed();
    }

}
