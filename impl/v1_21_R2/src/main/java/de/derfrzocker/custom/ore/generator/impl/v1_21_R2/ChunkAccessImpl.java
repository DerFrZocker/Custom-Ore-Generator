package de.derfrzocker.custom.ore.generator.impl.v1_21_R2;

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
import org.bukkit.craftbukkit.v1_21_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R2.util.BlockStateListPopulator;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftMagicNumbers;
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
