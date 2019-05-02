package de.derfrzocker.custom.generator.ore.v1_14_R1;

import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.api.WorldHandler;
import net.minecraft.server.v1_14_R1.ChunkGenerator;
import net.minecraft.server.v1_14_R1.PlayerChunkMap;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.lang.reflect.Field;

public class WorldHandler_v1_14_R1 implements WorldHandler, Listener {

    public WorldHandler_v1_14_R1() {
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

            // get the playerChunkMap where the ChunkGenerator is store, that we need to override
            final PlayerChunkMap playerChunkMap = world.getHandle().getChunkProvider().playerChunkMap;

            // get the ChunkGenerator from the PlayerChunkMap
            final Field ChunkGeneratorField = PlayerChunkMap.class.getDeclaredField("chunkGenerator");
            ChunkGeneratorField.setAccessible(true);
            final Object chunkGeneratorObject = ChunkGeneratorField.get(playerChunkMap);

            // return, if the chunkGeneratorObject is not an instance of ChunkGenerator
            if (!(chunkGeneratorObject instanceof ChunkGenerator)) {
                CustomOreGenerator.getInstance().getLogger().info("can't hook into world: " + world.getName() + ", because object is not an instance of ChunkTaskScheduler");
                return;
            }

            final ChunkGenerator<?> chunkGenerator = (ChunkGenerator<?>) chunkGeneratorObject;

            // create a new ChunkOverrider
            final ChunkOverrider<?> overrider = new ChunkOverrider<>(chunkGenerator);

            // set the ChunkOverrider to the PlayerChunkMap
            ChunkGeneratorField.set(playerChunkMap, overrider);

        } catch (Exception e) {
            CustomOreGenerator.getInstance().getLogger().warning("Unexpected error while hook into world: " + world.getName() + ", send the stacktrace below to the developer");
            e.printStackTrace();
        }
    }

}
