package de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper;

import de.derfrzocker.custom.ore.generator.api.WorldHandler;
import lombok.NonNull;
import net.minecraft.server.v1_13_R2.ChunkGenerator;
import net.minecraft.server.v1_13_R2.ChunkProviderServer;
import net.minecraft.server.v1_13_R2.ChunkTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class WorldHandler_v1_13_R2_paper implements WorldHandler, Listener {

    public WorldHandler_v1_13_R2_paper(final @NonNull JavaPlugin javaPlugin) {
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
            // get the ChunkScheduler from the ChunkProviderServer
            final Field chunkSchedulerField = ChunkProviderServer.class.getDeclaredField("chunkScheduler");
            chunkSchedulerField.setAccessible(true);
            final Object chunkSchedulerObject = chunkSchedulerField.get(world.getHandle().getChunkProvider());

            // if the given chunkScheduler is not an instance of ChunkTaskScheduler return
            if (!(chunkSchedulerObject instanceof ChunkTaskScheduler)) {
                return;
            }

            // get the ChunkGenerator from the ChunkTaskScheduler
            final Field ChunkGeneratorField = ChunkTaskScheduler.class.getDeclaredField("d");
            ChunkGeneratorField.setAccessible(true);
            final Object chunkGeneratorObject = ChunkGeneratorField.get(chunkSchedulerObject);

            // return, if the chunkGeneratorObject is not an instance of ChunkGenerator
            if (!(chunkGeneratorObject instanceof ChunkGenerator)) {
                return;
            }

            final ChunkGenerator<?> chunkGenerator = (ChunkGenerator<?>) chunkGeneratorObject;

            // create a new ChunkOverrider
            final ChunkOverrieder<?> overrider = new ChunkOverrieder<>(chunkGenerator);

            // set the ChunkOverrider tho the ChunkTaskScheduler
            ChunkGeneratorField.set(chunkSchedulerObject, overrider);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
