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

package de.derfrzocker.custom.ore.generator.impl.v1_8_R3;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
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
