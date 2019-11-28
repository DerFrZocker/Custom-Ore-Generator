package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.custom.ore.generator.impl.BiomeConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.CustomOreGeneratorServiceImpl;
import de.derfrzocker.custom.ore.generator.impl.OreConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.WorldConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.blockselector.CountRangeBlockSelector;
import de.derfrzocker.custom.ore.generator.impl.blockselector.HighestBlockBlockSelector;
import de.derfrzocker.custom.ore.generator.impl.customdata.*;
import de.derfrzocker.custom.ore.generator.impl.dao.OreConfigYamlDao;
import de.derfrzocker.custom.ore.generator.impl.dao.WorldConfigYamlDao;
import de.derfrzocker.custom.ore.generator.impl.dao.WorldConfigYamlDao_Old;
import de.derfrzocker.custom.ore.generator.utils.VersionPicker;
import de.derfrzocker.spigot.utils.Version;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Supplier;

public class CustomOreGenerator extends JavaPlugin implements Listener {

    static {
        ConfigurationSerialization.registerClass(BiomeConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(WorldConfigYamlImpl.class);
    }

    private CustomOreGeneratorMessages messages;
    private Permissions permissions;

    @Override
    public void onLoad() {
        messages = new CustomOreGeneratorMessages(this);
        permissions = new Permissions(this);

        final WorldConfigYamlDao worldConfigYamlDao = new WorldConfigYamlDao(new File(getDataFolder(), "data/world-config"));
        final OreConfigYamlDao oreConfigYamlDao = new OreConfigYamlDao(new File(getDataFolder(), "data/ore-config"));

        final CustomOreGeneratorService service = new CustomOreGeneratorServiceImpl(worldConfigYamlDao, oreConfigYamlDao, getLogger());

        Bukkit.getServicesManager().register(CustomOreGeneratorService.class, service, this, ServicePriority.Normal);

        //register BlockSelector
        final BlockSelector blockSelector = new CountRangeBlockSelector();
        service.registerBlockSelector(blockSelector);
        service.setDefaultBlockSelector(blockSelector);
        service.registerBlockSelector(new HighestBlockBlockSelector());

        // register CustomData
        service.registerCustomData(SkullTextureCustomData.INSTANCE);
        service.registerCustomData(FacingCustomData.INSTANCE);
        service.registerCustomData(CommandCustomData.INSTANCE);

        if (Version.v1_9_R1.isNewerOrSameVersion(Version.getCurrent()))
            service.registerCustomData(AutoCustomData.INSTANCE);

        if (Version.v1_13_R1.isNewerOrSameVersion(Version.getCurrent()))
            service.registerCustomData(TickBlockCustomData.INSTANCE);

        service.registerCustomData(DirectionCustomData.DOWN);
        service.registerCustomData(DirectionCustomData.UP);
        service.registerCustomData(DirectionCustomData.NORTH);
        service.registerCustomData(DirectionCustomData.SOUTH);
        service.registerCustomData(DirectionCustomData.EAST);
        service.registerCustomData(DirectionCustomData.WEST);

        oreConfigYamlDao.init();
        worldConfigYamlDao.init();

        checkOldStorageType();
    }

    @Override
    public void onEnable() {
        new VersionPicker(CustomOreGeneratorServiceSupplier.INSTANCE, this, Version.getCurrent()).init();

        getCommand("oregen").setExecutor(new OreGenCommand(CustomOreGeneratorServiceSupplier.INSTANCE, this, messages, permissions));

        new Metrics(this);
    }

    @Deprecated
    private void checkOldStorageType() {
        final File file = new File(getDataFolder(), "data/world_configs.yml");

        if (!file.exists())
            return;

        if (file.isDirectory()) {
            getLogger().info("WTF?? why??");
            return;
        }

        getLogger().info("Found old storage type, convert to new one");

        final WorldConfigYamlDao_Old worldConfigYamlDao = new WorldConfigYamlDao_Old(new File(getDataFolder(), "data/world_configs.yml"));
        worldConfigYamlDao.init();

        if (!file.delete())
            throw new RuntimeException("Can not delete File " + file);

        getLogger().info("Finish converting old storage format to new one");
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
