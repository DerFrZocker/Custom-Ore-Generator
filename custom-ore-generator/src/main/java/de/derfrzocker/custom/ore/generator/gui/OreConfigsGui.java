package de.derfrzocker.custom.ore.generator.gui;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.gui.settings.OreConfigsGuiSettings;
import de.derfrzocker.spigot.utils.gui.PageGui;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OreConfigsGui extends PageGui<OreConfig> {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;

    public OreConfigsGui(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigsGuiSettings settings) {
        super(plugin);
        Validate.notNull(serviceSupplier, "Supplier can not be null");

        this.serviceSupplier = serviceSupplier;

        addDecorations();
        addItem(settings.getBackSlot(), settings.getBackItemStack(), event -> {

        });
        init(serviceSupplier.get().getOreConfigs().toArray(new OreConfig[0]), OreConfig[]::new, settings, oreConfig -> settings.getOreConfigItemStack(), (oreConfig, event) -> {



        });



    }

}
