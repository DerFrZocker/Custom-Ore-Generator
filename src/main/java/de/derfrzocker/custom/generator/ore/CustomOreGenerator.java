package de.derfrzocker.custom.generator.ore;

import de.derfrzocker.custom.generator.ore.api.CustomOreGeneratorService;
import de.derfrzocker.custom.generator.ore.command.OreGenCommand;
import de.derfrzocker.custom.generator.ore.command.SetCommand;
import de.derfrzocker.custom.generator.ore.generators.MinableGenerator_v1_12_R1;
import de.derfrzocker.custom.generator.ore.generators.v1_13_R2.MinableGenerator_v1_13_R2;
import de.derfrzocker.custom.generator.ore.generators.v1_13_R2.WorldHandler;
import de.derfrzocker.custom.generator.ore.impl.BiomeConfigYamlImpl;
import de.derfrzocker.custom.generator.ore.impl.CustomOreGeneratorServiceImpl;
import de.derfrzocker.custom.generator.ore.impl.OreConfigYamlImpl;
import de.derfrzocker.custom.generator.ore.impl.WorldConfigYamlImpl;
import de.derfrzocker.custom.generator.ore.impl.dao.WorldConfigYamlDao;
import de.derfrzocker.custom.generator.ore.util.CommandSeparator;
import de.derfrzocker.custom.generator.ore.util.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class CustomOreGenerator extends JavaPlugin implements Listener {

    @Getter
    private static CustomOreGenerator instance;

    private final BlockPopulator populator = new CustomOreBlockPopulator();

    private final CommandSeparator commandSeparator = new OreGenCommand();

    @Override
    public void onLoad() {
        instance = this;

        ConfigurationSerialization.registerClass(BiomeConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(WorldConfigYamlImpl.class);


        Bukkit.getServicesManager().register(CustomOreGeneratorService.class, new CustomOreGeneratorServiceImpl(new WorldConfigYamlDao(new File(getDataFolder(), "data/world_configs.yml"))), this, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        getService().setDefaultOreGenerator(new MinableGenerator_v1_12_R1());

        CustomOreGeneratorMessages.getInstance().setFile(Config.getConfig(this, "messages.yml"));

        getCommand("oregen").setExecutor(commandSeparator);
        commandSeparator.registerExecuter(new SetCommand(), "set");
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        getLogger().log(Level.INFO, "load world " + event.getWorld().getName());

        if (event.getWorld().getPopulators().contains(populator))
            return;

        getLogger().log(Level.INFO, "add populator to world " + event.getWorld().getName());

        event.getWorld().getPopulators().add(populator);
    }

    public static CustomOreGeneratorService getService() {
        CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);

        if (service == null)
            throw new IllegalStateException("The Bukkit Service have no " + CustomOreGeneratorService.class.getName() + " registered", new NullPointerException("service can't be null"));

        return service;
    }

}
