package de.derfrzocker.custom.ore.generator.impl.v1_14_R1;

import de.derfrzocker.custom.ore.generator.api.WorldHandler;
import lombok.NonNull;
import net.minecraft.server.v1_14_R1.ChunkGenerator;
import net.minecraft.server.v1_14_R1.PlayerChunkMap;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class WorldHandler_v1_14_R1 implements WorldHandler, Listener {

    public WorldHandler_v1_14_R1(final @NonNull JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void onWorldLoad(final WorldLoadEvent event) {
        // checking if the Bukkit world is an instance of CraftWorld, if not return
        if (!(event.getWorld() instanceof CraftWorld)) {
            return;
        }

        final CraftWorld world = (CraftWorld) event.getWorld();

        try {

            // get the playerChunkMap where the ChunkGenerator is store, that we need to override
            final PlayerChunkMap playerChunkMap = world.getHandle().getChunkProvider().playerChunkMap;

            // get the ChunkGenerator from the PlayerChunkMap
            final Field ChunkGeneratorField = PlayerChunkMap.class.getDeclaredField("chunkGenerator");
            ChunkGeneratorField.setAccessible(true);
            final Object chunkGeneratorObject = ChunkGeneratorField.get(playerChunkMap);

            // return, if the chunkGeneratorObject is not an instance of ChunkGenerator
            if (!(chunkGeneratorObject instanceof ChunkGenerator)) {
                return;
            }

            final ChunkGenerator<?> chunkGenerator = (ChunkGenerator<?>) chunkGeneratorObject;

            // create a new ChunkOverrider
            final ChunkOverrider<?> overrider = new ChunkOverrider<>(chunkGenerator);

            // set the ChunkOverrider to the PlayerChunkMap
            ChunkGeneratorField.set(playerChunkMap, overrider);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
