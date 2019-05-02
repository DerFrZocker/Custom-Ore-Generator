package de.derfrzocker.custom.generator.ore.v1_13_R2;

import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.api.WorldHandler;
import net.minecraft.server.v1_13_R2.ChunkGenerator;
import net.minecraft.server.v1_13_R2.ChunkProviderServer;
import net.minecraft.server.v1_13_R2.ChunkTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.lang.reflect.Field;

public class WorldHandler_v1_13_R2 implements WorldHandler, Listener {

    public WorldHandler_v1_13_R2() {
        Bukkit.getPluginManager().registerEvents(this, CustomOreGenerator.getInstance());
    }

    @EventHandler
    public void onWorldLoad(final WorldLoadEvent event) {
        CustomOreGenerator.getInstance().getLogger().info("try to hook in to world " + event.getWorld().getName());

        // checking if the Bukkit world is an instance of CraftWorld, if not return
        if (!(event.getWorld() instanceof CraftWorld)) {
            CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + event.getWorld().getName() + ", because World is not an instance of CraftWorld");
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
                CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + world.getName() + ", because object is not an instance of ChunkTaskScheduler");
                return;
            }

            // get the ChunkGenerator from the ChunkTaskScheduler
            final Field ChunkGeneratorField = ChunkTaskScheduler.class.getDeclaredField("d");
            ChunkGeneratorField.setAccessible(true);
            final Object chunkGeneratorObject = ChunkGeneratorField.get(chunkSchedulerObject);

            // return, if the chunkGeneratorObject is not an instance of ChunkGenerator
            if (!(chunkGeneratorObject instanceof ChunkGenerator)) {
                CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + world.getName() + ", because object is not an instance of ChunkTaskScheduler");
                return;
            }

            final ChunkGenerator<?> chunkGenerator = (ChunkGenerator<?>) chunkGeneratorObject;

            // create a new ChunkOverrider
            final ChunkOverrieder<?> overrider = new ChunkOverrieder<>(chunkGenerator);

            // set the ChunkOverrider tho the ChunkTaskScheduler
            ChunkGeneratorField.set(chunkSchedulerObject, overrider);

        } catch (Exception e) {
            CustomOreGenerator.getInstance().getLogger().warning("Unexpected error while hook into world: " + world.getName() + ", send the stacktrace below to the developer");
            e.printStackTrace();
        }
    }

}
