package de.derfrzocker.custom.ore.generator.impl.v1_13_R1;

import de.derfrzocker.custom.ore.generator.api.WorldHandler;
import lombok.NonNull;
import net.minecraft.server.v1_13_R1.ChunkProviderServer;
import net.minecraft.server.v1_13_R1.ChunkTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.generator.InternalChunkGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class WorldHandler_v1_13_R1 implements WorldHandler, Listener {

    public WorldHandler_v1_13_R1(final @NonNull JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!(event.getWorld() instanceof CraftWorld)) {
            return;
        }

        CraftWorld world = (CraftWorld) event.getWorld();

        if (world.getHandle().generator instanceof InternalChunkGenerator<?>) {
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
                Field field = ChunkProviderServer.class.getDeclaredField("f");
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
