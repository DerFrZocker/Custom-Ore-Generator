package de.derfrzocker.custom.ore.generator.impl.v1_13_R1;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.WorldHandler;
import net.minecraft.server.v1_13_R1.ChunkProviderServer;
import net.minecraft.server.v1_13_R1.ChunkTaskScheduler;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.generator.InternalChunkGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class WorldHandler_v1_13_R1 implements WorldHandler, Listener {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public WorldHandler_v1_13_R1(@NotNull final JavaPlugin javaPlugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        this.serviceSupplier = serviceSupplier;

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void onWorldLoad(@NotNull final WorldLoadEvent event) {
        if (!(event.getWorld() instanceof CraftWorld)) {
            return;
        }

        final CraftWorld world = (CraftWorld) event.getWorld();

        if (world.getHandle().generator instanceof InternalChunkGenerator<?>) {
            return;
        }

        final ChunkOverrieder<?> overrider = new ChunkOverrieder<>(serviceSupplier, ((ChunkProviderServer) world.getHandle().getChunkProvider()).chunkGenerator);

        try {
            {
                final Field field = ChunkProviderServer.class.getDeclaredField("chunkGenerator");
                field.setAccessible(true);
                field.set(world.getHandle().getChunkProvider(), overrider);
            }

            {
                final Field field = ChunkProviderServer.class.getDeclaredField("f");
                field.setAccessible(true);
                final Object object = field.get(world.getHandle().getChunkProvider());

                final Field field2 = ChunkTaskScheduler.class.getDeclaredField("d");
                field2.setAccessible(true);
                field2.set(object, overrider);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
