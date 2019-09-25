package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.custom.ore.generator.impl.*;
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

        Bukkit.getServicesManager().register(CustomOreGeneratorService.class, new CustomOreGeneratorServiceImpl(worldConfigYamlDao), this, ServicePriority.Normal);

        worldConfigYamlDao.init();
    }

    @Override
    public void onEnable() {
        new VersionPicker(Version.getCurrent(), getService(), this).init();

        getCommand("oregen").setExecutor(new OreGenCommand(this));

        getService().registerCustomData(SkullTextureCustomData.INSTANCE);
        getService().registerCustomData(DirectionCustomData.INSTANCE);

        new Metrics(this);
    }

    public static CustomOreGeneratorService getService() {
        CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);

        if (service == null)
            throw new IllegalStateException("The Bukkit Service have no " + CustomOreGeneratorService.class.getName() + " registered", new NullPointerException("service can't be null"));

        return service;
    }

}
