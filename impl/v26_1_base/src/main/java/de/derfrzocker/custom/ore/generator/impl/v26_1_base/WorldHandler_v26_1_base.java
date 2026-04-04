package de.derfrzocker.custom.ore.generator.impl.v26_1_base;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.WorldHandler;
import java.util.function.Supplier;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class WorldHandler_v26_1_base implements WorldHandler, Listener {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public WorldHandler_v26_1_base(
            @NotNull final JavaPlugin javaPlugin,
            @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        this.serviceSupplier = serviceSupplier;

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldInit(final WorldInitEvent event) {
        event.getWorld().getPopulators().add(new CustomOrePopulator(event.getWorld(), serviceSupplier, this));
    }
}
