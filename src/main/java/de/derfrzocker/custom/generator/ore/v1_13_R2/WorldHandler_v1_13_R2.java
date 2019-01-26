package de.derfrzocker.custom.generator.ore.v1_13_R2;

import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.api.WorldHandler;
import net.minecraft.server.v1_13_R2.ChunkProviderServer;
import net.minecraft.server.v1_13_R2.ChunkTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.generator.InternalChunkGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.lang.reflect.Field;

public class WorldHandler_v1_13_R2 implements WorldHandler, Listener {

    public WorldHandler_v1_13_R2() {
        Bukkit.getPluginManager().registerEvents(this, CustomOreGenerator.getInstance());
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        CustomOreGenerator.getInstance().getLogger().info("try to hook in to world " + event.getWorld().getName());

        if (!(event.getWorld() instanceof CraftWorld)) {
            CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + event.getWorld().getName() + ", because World is not an instance of CraftWorld");
            return;
        }
        CraftWorld world = (CraftWorld) event.getWorld();

        if (world.getHandle().generator instanceof InternalChunkGenerator<?>) {
            CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + world.getName() + ", because ChunkGenerator is not an instance of InternalChunkGenerator");
            return;
        }

        ChunkOverrieder<?> overrieder = new ChunkOverrieder<>(((ChunkProviderServer) world.getHandle().getChunkProvider()).chunkGenerator);

        try {
            {
                Field field = ChunkProviderServer.class.getDeclaredField("chunkGenerator");
                field.setAccessible(true);
                field.set(world.getHandle().getChunkProvider(), overrieder);
            }

            {
                Field field = ChunkProviderServer.class.getDeclaredField("chunkScheduler");
                field.setAccessible(true);
                Object object = field.get(world.getHandle().getChunkProvider());

                Field field2 = ChunkTaskScheduler.class.getDeclaredField("d");
                field2.setAccessible(true);
                field2.set(object, overrieder);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
