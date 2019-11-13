package de.derfrzocker.custom.ore.generator.impl.v1_9_R1;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.WorldServer;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

public class ChunkAccessImpl implements ChunkAccess {

    @NotNull
    private final WorldServer worldServer;

    public ChunkAccessImpl(@NotNull final WorldServer worldServer) {
        Validate.notNull(worldServer, "WorldServer can not be null");

        this.worldServer = worldServer;
    }

    @Override
    public void setMaterial(@NotNull Material material, final int x, final int y, final int z) {
        worldServer.setTypeAndData(new BlockPosition(x, y, z), CraftMagicNumbers.getBlock(material).getBlockData(), 2);
    }

    @NotNull
    @Override
    public Material getMaterial(final int x, final int y, final int z) {
        return CraftMagicNumbers.getMaterial(worldServer.getType(new BlockPosition(x, y, z)).getBlock());
    }

    @NotNull
    public WorldServer getWorldServer() {
        return worldServer;
    }
}
