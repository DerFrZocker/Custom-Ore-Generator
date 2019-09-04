package de.derfrzocker.custom.ore.generator.impl.v1_14_R1;

import net.minecraft.server.v1_14_R1.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class DummyGeneratorAccess implements GeneratorAccess {

    public static final GeneratorAccess INSTANCE = new DummyGeneratorAccess();

    private DummyGeneratorAccess() {
    }

    public long getSeed() {
        return 0L;
    }

    public TickList<Block> getBlockTickList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TickList<FluidType> getFluidTickList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public World getMinecraftWorld() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public WorldData getWorldData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DifficultyDamageScaler getDamageScaler(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IChunkProvider getChunkProvider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Random getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(BlockPosition bp, Block block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playSound(@Nullable EntityHuman entityHuman, BlockPosition blockPosition, SoundEffect soundEffect, SoundCategory soundCategory, float v, float v1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void a(EntityHuman eh, BlockPosition bp, SoundEffect se, SoundCategory sc, float f, float f1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addParticle(ParticleParam pp, double d, double d1, double d2, double d3, double d4, double d5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void a(EntityHuman eh, int i, BlockPosition bp, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Entity> getEntities(Entity entity, AxisAlignedBB aabb, Predicate<? super Entity> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T extends Entity> List<T> a(Class<? extends T> type, AxisAlignedBB aabb, Predicate<? super T> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<? extends EntityHuman> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getLightLevel(BlockPosition bp, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IChunkAccess getChunkAt(int i, int i1, ChunkStatus cs, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BlockPosition getHighestBlockYAt(HeightMap.Type type, BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int a(HeightMap.Type type, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int c() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int a(BlockPosition bp, EnumDirection ed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean e() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public WorldProvider getWorldProvider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BiomeBase getBiome(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getBrightness(EnumSkyBlock esb, BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TileEntity getTileEntity(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IBlockData getType(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Fluid getFluid(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean a(BlockPosition bp, Predicate<IBlockData> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setTypeAndData(BlockPosition blockposition, IBlockData iblockdata, int i) {
        return false;
    }

    public boolean a(BlockPosition blockposition, boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean b(BlockPosition blockposition, boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
