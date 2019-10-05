package de.derfrzocker.custom.ore.generator.impl.v1_13_R1;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import net.minecraft.server.v1_13_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class GeneratorAccessOverrider implements GeneratorAccess {

    @NotNull
    private final GeneratorAccess parent;
    @NotNull
    private final OreConfig oreConfig;
    private final int x;
    private final int z;

    public GeneratorAccessOverrider(@NotNull final GeneratorAccess parent, @NotNull final OreConfig oreConfig, final int x, final int z) {
        Validate.notNull(parent, "Parent GeneratorAccess can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");

        this.parent = parent;
        this.oreConfig = oreConfig;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean setTypeAndData(final BlockPosition blockPosition, final IBlockData iBlockData, final int i) {
        final boolean success = parent.setTypeAndData(blockPosition, iBlockData, i);

        if (!success)
            return false;

        oreConfig.getCustomData().forEach((customData, object) -> customData.getCustomDataApplier().apply(oreConfig, blockPosition, parent));

        return true;
    }

    @Override
    public boolean addEntity(Entity entity) {
        return parent.addEntity(entity);
    }

    @Override
    public boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
        return parent.addEntity(entity, spawnReason);
    }

    @Override
    public boolean setAir(BlockPosition blockPosition) {
        return parent.setAir(blockPosition);
    }

    @Override
    public void a(EnumSkyBlock enumSkyBlock, BlockPosition blockPosition, int i) {
        parent.a(enumSkyBlock, blockPosition, i);
    }

    @Override
    public boolean setAir(BlockPosition blockPosition, boolean b) {
        return parent.setAir(blockPosition, b);
    }

    @Override
    public long getSeed() {
        return parent.getSeed();
    }

    @Override
    public float af() {
        return parent.af();
    }

    @Override
    public float k(float v) {
        return parent.k(v);
    }

    @Override
    public TickList<Block> I() {
        return parent.I();
    }

    @Override
    public TickList<FluidType> H() {
        return parent.H();
    }

    @Override
    public IChunkAccess y(BlockPosition blockPosition) {
        return parent.y(blockPosition);
    }

    @Override
    public IChunkAccess c(int i, int i1) {
        return parent.c(i, i1);
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

    @Nullable
    @Override
    public <T extends PersistentBase> T a(Function<String, T> function, String s) {
        return parent.a(function, s);
    }

    @Nullable
    @Override
    public PersistentCollection s_() {
        return parent.s_();
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return parent.getDifficulty();
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return parent.getChunkProvider();
    }

    @Override
    public IDataManager getDataManager() {
        return parent.getDataManager();
    }

    @Override
    public Random m() {
        return parent.m();
    }

    @Override
    public void update(BlockPosition blockPosition, Block block) {
        parent.update(blockPosition, block);
    }

    @Override
    public BlockPosition getSpawn() {
        return parent.getSpawn();
    }

    @Override
    public void a(@Nullable EntityHuman entityHuman, BlockPosition blockPosition, SoundEffect soundEffect, SoundCategory soundCategory, float v, float v1) {
        parent.a(entityHuman, blockPosition, soundEffect, soundCategory, v, v1);
    }

    @Override
    public void addParticle(ParticleParam particleParam, double v, double v1, double v2, double v3, double v4, double v5) {
        parent.addParticle(particleParam, v, v1, v2, v3, v4, v5);
    }

    @Override
    public boolean isEmpty(BlockPosition blockPosition) {
        return parent.isEmpty(blockPosition);
    }

    @Override
    public BiomeBase getBiome(BlockPosition blockPosition) {
        return parent.getBiome(blockPosition);
    }

    @Override
    public int getBrightness(EnumSkyBlock enumSkyBlock, BlockPosition blockPosition) {
        return parent.getBrightness(enumSkyBlock, blockPosition);
    }

    @Override
    public boolean z(BlockPosition blockPosition) {
        return parent.z(blockPosition);
    }

    @Override
    public int getLightLevel(BlockPosition blockPosition, int i) {
        return parent.getLightLevel(blockPosition, i);
    }

    @Override
    public boolean isChunkLoaded(int i, int i1, boolean b) {
        return parent.isChunkLoaded(i, i1, b);
    }

    @Override
    public boolean e(BlockPosition blockPosition) {
        return parent.e(blockPosition);
    }

    @Override
    public BlockPosition getHighestBlockYAt(HeightMap.Type type, BlockPosition blockPosition) {
        return parent.getHighestBlockYAt(type, blockPosition);
    }

    @Override
    public int a(HeightMap.Type type, int i, int i1) {
        return parent.a(type, i, i1);
    }

    @Override
    public float A(BlockPosition blockPosition) {
        return parent.A(blockPosition);
    }

    @Nullable
    @Override
    public EntityHuman findNearbyPlayer(Entity entity, double v) {
        return parent.findNearbyPlayer(entity, v);
    }

    @Nullable
    @Override
    public EntityHuman b(Entity entity, double v) {
        return parent.b(entity, v);
    }

    @Nullable
    @Override
    public EntityHuman a(double v, double v1, double v2, double v3, boolean b) {
        return parent.a(v, v1, v2, v3, b);
    }

    @Nullable
    @Override
    public EntityHuman a(double v, double v1, double v2, double v3, Predicate<Entity> predicate) {
        return parent.a(v, v1, v2, v3, predicate);
    }

    @Override
    public int c() {
        return parent.c();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return parent.getWorldBorder();
    }

    @Override
    public boolean a(@Nullable Entity entity, VoxelShape voxelShape) {
        return parent.a(entity, voxelShape);
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisAlignedBB) {
        return parent.getEntities(entity, axisAlignedBB);
    }

    @Override
    public int a(BlockPosition blockPosition, EnumDirection enumDirection) {
        return parent.a(blockPosition, enumDirection);
    }

    @Override
    public boolean e() {
        return parent.e();
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Override
    public boolean a(IBlockData iBlockData, BlockPosition blockPosition) {
        return parent.a(iBlockData, blockPosition);
    }

    @Override
    public boolean b(@Nullable Entity entity, AxisAlignedBB axisAlignedBB) {
        return parent.b(entity, axisAlignedBB);
    }

    @Override
    public VoxelShape a(VoxelShape voxelShape, VoxelShape voxelShape1, boolean b, boolean b1) {
        return parent.a(voxelShape, voxelShape1, b, b1);
    }

    @Override
    public VoxelShape a(@Nullable Entity entity, AxisAlignedBB axisAlignedBB, double v, double v1, double v2) {
        return parent.a(entity, axisAlignedBB, v, v1, v2);
    }

    @Override
    public VoxelShape a(@Nullable Entity entity, AxisAlignedBB axisAlignedBB, Set<Entity> set, double v, double v1, double v2) {
        return parent.a(entity, axisAlignedBB, set, v, v1, v2);
    }

    @Override
    public VoxelShape c(@Nullable Entity entity, AxisAlignedBB axisAlignedBB) {
        return parent.c(entity, axisAlignedBB);
    }

    @Override
    public VoxelShape a(@Nullable Entity entity, VoxelShape voxelShape, VoxelShape voxelShape1, Set<Entity> set, boolean b) {
        return parent.a(entity, voxelShape, voxelShape1, set, b);
    }

    @Override
    public VoxelShape a(@Nullable Entity entity, VoxelShape voxelShape, boolean b, Set<Entity> set) {
        return parent.a(entity, voxelShape, b, set);
    }

    @Override
    public boolean i(Entity entity) {
        return parent.i(entity);
    }

    @Override
    public boolean a(@Nullable Entity entity, AxisAlignedBB axisAlignedBB, Set<Entity> set) {
        return parent.a(entity, axisAlignedBB, set);
    }

    @Override
    public boolean getCubes(@Nullable Entity entity, AxisAlignedBB axisAlignedBB) {
        return parent.getCubes(entity, axisAlignedBB);
    }

    @Override
    public boolean B(BlockPosition blockPosition) {
        return parent.B(blockPosition);
    }

    @Override
    public boolean containsLiquid(AxisAlignedBB axisAlignedBB) {
        return parent.containsLiquid(axisAlignedBB);
    }

    @Override
    public int getLightLevel(BlockPosition blockPosition) {
        return parent.getLightLevel(blockPosition);
    }

    @Override
    public int d(BlockPosition blockPosition, int i) {
        return parent.d(blockPosition, i);
    }

    @Override
    public boolean isLoaded(BlockPosition blockPosition) {
        return parent.isLoaded(blockPosition);
    }

    @Override
    public boolean b(BlockPosition blockPosition, boolean b) {
        return parent.b(blockPosition, b);
    }

    @Override
    public boolean areChunksLoaded(BlockPosition blockPosition, int i) {
        return parent.areChunksLoaded(blockPosition, i);
    }

    @Override
    public boolean areChunksLoaded(BlockPosition blockPosition, int i, boolean b) {
        return parent.areChunksLoaded(blockPosition, i, b);
    }

    @Override
    public boolean areChunksLoadedBetween(BlockPosition blockPosition, BlockPosition blockPosition1) {
        return parent.areChunksLoadedBetween(blockPosition, blockPosition1);
    }

    @Override
    public boolean areChunksLoadedBetween(BlockPosition blockPosition, BlockPosition blockPosition1, boolean b) {
        return parent.areChunksLoadedBetween(blockPosition, blockPosition1, b);
    }

    @Override
    public boolean a(StructureBoundingBox structureBoundingBox) {
        return parent.a(structureBoundingBox);
    }

    @Override
    public boolean a(StructureBoundingBox structureBoundingBox, boolean b) {
        return parent.a(structureBoundingBox, b);
    }

    @Override
    public boolean isAreaLoaded(int i, int i1, int i2, int i3, int i4, int i5, boolean b) {
        return parent.isAreaLoaded(i, i1, i2, i3, i4, i5, b);
    }

    @Override
    public WorldProvider o() {
        return parent.o();
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
    public Fluid b(BlockPosition blockPosition) {
        return parent.b(blockPosition);
    }

    @Override
    public int J() {
        return parent.J();
    }

}
