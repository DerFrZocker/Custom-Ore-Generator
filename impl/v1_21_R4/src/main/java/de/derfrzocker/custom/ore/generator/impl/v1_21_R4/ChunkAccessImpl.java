package de.derfrzocker.custom.ore.generator.impl.v1_21_R4;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R4.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R4.util.BlockStateListPopulator;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

public class ChunkAccessImpl extends BlockStateListPopulator implements ChunkAccess {

    private static final Method PLACE_BLOCKS;
    private static final boolean PAPER;

    static {
        Method tmpMethod;
        try {
            tmpMethod = BlockStateListPopulator.class.getDeclaredMethod("placeBlocks");
        } catch (NoSuchMethodException e) {
            tmpMethod = null;
        }

        PLACE_BLOCKS = tmpMethod;
        PAPER = PLACE_BLOCKS != null;
    }

    private final LevelAccessor world;
    private final Set<BlockPos> blockPosSet = new HashSet<>();
    private final Consumer<BlockPos> blockSet = blockPosSet::add;

    public ChunkAccessImpl(LevelAccessor world) {
        super(world);
        this.world = world;
    }

    // In Paper 1.21.11, getWorld() was removed from BlockStateListPopulator
    public LevelAccessor getWorld() {
        return world;
    }

    @Override
    public void setMaterial(@NotNull Material material, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        setBlock(pos, ((CraftBlockData) material.createBlockData()).getState(), 3);
        blockPosSet.add(pos);
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
        // In Paper 1.21.11, getBlocks() was removed from BlockStateListPopulator
        // We track blocks ourselves in blockPosSet (populated in setMaterial)
        return new HashSet<>(blockPosSet);
    }

    @Override
    public net.minecraft.world.level.chunk.ChunkAccess getChunk(int i, int i1, ChunkStatus cs, boolean bln) {
        net.minecraft.world.level.chunk.ChunkAccess chunkAccess = getWorld().getChunk(i, i1, cs, bln);
        return new CustomChunkAccess(chunkAccess, blockSet, getWorld().getServer().registryAccess().lookupOrThrow(Registries.BIOME));
    }

    public void submit() {
        // refreshTiles() and updateList() methods were removed in Paper 1.21.11
        // Tile entities are now handled automatically by the new BlockStateListPopulator API
        if (PAPER) {
            try {
                PLACE_BLOCKS.invoke(this);
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error while submitting chunk", e);
            }
        } else {
            refreshTiles();
            updateList();
        }
    }
}
