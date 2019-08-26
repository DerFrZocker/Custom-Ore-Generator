package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.command.*;
import de.derfrzocker.custom.ore.generator.impl.BiomeConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.CustomOreGeneratorServiceImpl;
import de.derfrzocker.custom.ore.generator.impl.OreConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.WorldConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.dao.WorldConfigYamlDao;
import de.derfrzocker.custom.ore.generator.util.VersionPicker;
import de.derfrzocker.spigot.utils.CommandSeparator;
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

    private final CommandSeparator commandSeparator = new OreGenCommand();

    static {
        ConfigurationSerialization.registerClass(BiomeConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(WorldConfigYamlImpl.class);
    }

    @Override
    public void onLoad() {
        instance = this;

        Bukkit.getServicesManager().register(CustomOreGeneratorService.class, new CustomOreGeneratorServiceImpl(new WorldConfigYamlDao(new File(getDataFolder(), "data/world_configs.yml"))), this, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {

        new VersionPicker(Version.getCurrent(), getService()).init();

        getCommand("oregen").setExecutor(commandSeparator);
        commandSeparator.registerExecutor(new SetCommand(), "set");
        commandSeparator.registerExecutor(new SetBiomeCommand(), "setbiome");
        commandSeparator.registerExecutor(new ReloadCommand(), "reload");
        HelpCommand helpCommand = new HelpCommand();
        commandSeparator.registerExecutor(helpCommand, "");
        commandSeparator.registerExecutor(helpCommand, null);
        commandSeparator.registerExecutor(helpCommand, "help");

        new Metrics(this);
    }

    public static CustomOreGeneratorService getService() {
        CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);

        if (service == null)
            throw new IllegalStateException("The Bukkit Service have no " + CustomOreGeneratorService.class.getName() + " registered", new NullPointerException("service can't be null"));

        return service;
    }

}
