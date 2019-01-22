package de.derfrzocker.custom.generator.ore.generators.v1_13_R2;

import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import net.minecraft.server.v1_13_R2.ChunkProviderServer;
import net.minecraft.server.v1_13_R2.ChunkTaskScheduler;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.generator.InternalChunkGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public class WorldHandler implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event){

        CustomOreGenerator.getInstance().getLogger().info("try to hook in to world "+ event.getWorld().getName());

        if(!(event.getWorld() instanceof CraftWorld)) {
            CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + event.getWorld().getName() + ", because World is not an instance of CraftWorld");
            return;
        }
        CraftWorld world = (CraftWorld) event.getWorld();

        if(world.getHandle().generator instanceof InternalChunkGenerator<?>) {
            CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + world.getName() + ", because ChunkGenerator is not an instance of InternalChunkGenerator");
            return;
        }

        ChunkOverrieder<?> overrieder = new ChunkOverrieder<>(((ChunkProviderServer )world.getHandle().getChunkProvider()).chunkGenerator);

        try {
            {
                Stream.of(ChunkProviderServer.class.getFields()).forEach(value -> CustomOreGenerator.getInstance().getLogger().info("normal -> " + value));
                Stream.of(ChunkProviderServer.class.getDeclaredFields()).forEach(value -> CustomOreGenerator.getInstance().getLogger().info("declared -> " + value));

                Field field = ChunkProviderServer.class.getDeclaredField("chunkGenerator");
                field.setAccessible(true);
                field.set((ChunkProviderServer) world.getHandle().getChunkProvider(), overrieder);
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
