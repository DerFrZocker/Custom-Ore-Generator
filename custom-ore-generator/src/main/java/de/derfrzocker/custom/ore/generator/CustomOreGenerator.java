package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.custom.ore.generator.impl.*;
import de.derfrzocker.custom.ore.generator.impl.blockselector.CountRangeBlockSelector;
import de.derfrzocker.custom.ore.generator.impl.dao.WorldConfigYamlDao;
import de.derfrzocker.custom.ore.generator.utils.VersionPicker;
import de.derfrzocker.spigot.utils.Version;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Supplier;

public class CustomOreGenerator extends JavaPlugin implements Listener {

    @Getter
    private static CustomOreGenerator instance;

    static {
        ConfigurationSerialization.registerClass(BiomeConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(WorldConfigYamlImpl.class);
    }

    @Override
    public void onLoad() {
        instance = this;

        final WorldConfigYamlDao worldConfigYamlDao = new WorldConfigYamlDao(new File(getDataFolder(), "data/world_configs.yml"));

        final CustomOreGeneratorService service = new CustomOreGeneratorServiceImpl(worldConfigYamlDao, getLogger());

        Bukkit.getServicesManager().register(CustomOreGeneratorService.class, service, this, ServicePriority.Normal);

        //register BlockSelector
        final BlockSelector blockSelector = new CountRangeBlockSelector();
        service.registerBlockSelector(blockSelector);
        service.setDefaultBlockSelector(blockSelector);

        // register CustomData
        service.registerCustomData(SkullTextureCustomData.INSTANCE);
        service.registerCustomData(FacingCustomData.INSTANCE);
        service.registerCustomData(DirectionCustomData.DOWN);
        service.registerCustomData(DirectionCustomData.UP);
        service.registerCustomData(DirectionCustomData.NORTH);
        service.registerCustomData(DirectionCustomData.SOUTH);
        service.registerCustomData(DirectionCustomData.EAST);
        service.registerCustomData(DirectionCustomData.WEST);

        worldConfigYamlDao.init();
    }

    @Override
    public void onEnable() {
        new VersionPicker(CustomOreGeneratorServiceSupplier.INSTANCE, this, Version.getCurrent()).init();

        getCommand("oregen").setExecutor(new OreGenCommand(CustomOreGeneratorServiceSupplier.INSTANCE, this));


        new Metrics(this);
    }

    private static final class CustomOreGeneratorServiceSupplier implements Supplier<CustomOreGeneratorService> {

        private static final CustomOreGeneratorServiceSupplier INSTANCE = new CustomOreGeneratorServiceSupplier();

        private CustomOreGeneratorService service;

        @Override
        public CustomOreGeneratorService get() {
            final CustomOreGeneratorService tempService = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);

            if (service == null && tempService == null)
                throw new NullPointerException("The Bukkit Service has no CustomOreGeneratorService and no CustomOreGeneratorService is cached!");

            if (tempService != null && service != tempService)
                service = tempService;

            return service;
        }

    }

}
