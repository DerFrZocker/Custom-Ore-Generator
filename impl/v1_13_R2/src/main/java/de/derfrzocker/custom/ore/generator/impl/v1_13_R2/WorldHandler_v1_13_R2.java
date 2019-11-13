package de.derfrzocker.custom.ore.generator.impl.v1_13_R2;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.WorldHandler;
import net.minecraft.server.v1_13_R2.ChunkGenerator;
import net.minecraft.server.v1_13_R2.ChunkProviderServer;
import net.minecraft.server.v1_13_R2.ChunkTaskScheduler;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class WorldHandler_v1_13_R2 implements WorldHandler, Listener {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public WorldHandler_v1_13_R2(@NotNull final JavaPlugin javaPlugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        this.serviceSupplier = serviceSupplier;

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void onWorldLoad(@NotNull final WorldInitEvent event) {
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
            final ChunkOverrieder<?> overrider = new ChunkOverrieder<>(serviceSupplier, chunkGenerator);

            // set the ChunkOverrider tho the ChunkTaskScheduler
            ChunkGeneratorField.set(chunkSchedulerObject, overrider);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
