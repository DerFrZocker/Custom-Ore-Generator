package de.derfrzocker.custom.ore.generator.impl.v1_15_R1;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.WorldHandler;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WorldHandler_v1_15_R1 implements WorldHandler, Listener {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    private final Map<Thread, Set<Block>> threadSetMap = Collections.synchronizedMap(new HashMap<>());


    public WorldHandler_v1_15_R1(@NotNull final JavaPlugin javaPlugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        this.serviceSupplier = serviceSupplier;

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
        replaceTarget();
    }

    @EventHandler
    public void onWorldLoad(final WorldInitEvent event) {
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
            final ChunkOverrider<?> overrider = new ChunkOverrider<>(serviceSupplier, chunkGenerator, this);

            // set the ChunkOverrider to the PlayerChunkMap
            ChunkGeneratorField.set(playerChunkMap, overrider);

        } catch (final Exception e) {
            throw new RuntimeException("Unexpected error while hook into world " + world.getName(), e);
        }
    }

    void remove() {
        threadSetMap.remove(Thread.currentThread());
    }

    void add(@Nullable final Set<Block> blocks) {
        threadSetMap.put(Thread.currentThread(), blocks);
    }

    private void replaceTarget() {
        try {
            final Field predicateField = WorldGenFeatureOreConfiguration.Target.class.getDeclaredField("e");
            predicateField.setAccessible(true);
            predicateField.set(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, getPredicate());

        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unexpected error while hook into WorldGenFeatureOreConfiguration Target", e);
        }
    }

    private Predicate<IBlockData> getPredicate() {
        return (value) -> {
            if (value == null) {
                return false;
            } else {
                final Set<Block> blocks = threadSetMap.get(Thread.currentThread());
                final Block block = value.getBlock();

                if (blocks == null)
                    return block == Blocks.STONE || block == Blocks.GRANITE || block == Blocks.DIORITE || block == Blocks.ANDESITE;

                return blocks.contains(block);
            }
        };
    }

}
