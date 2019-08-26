package de.derfrzocker.custom.ore.generator.impl.dao;

import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.api.dao.WorldConfigDao;
import de.derfrzocker.custom.ore.generator.impl.WorldConfigYamlImpl;
import de.derfrzocker.spigot.utils.dao.yaml.BasicYamlDao;
import lombok.NonNull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.util.Optional;

public class WorldConfigYamlDao extends BasicYamlDao<String, WorldConfig> implements WorldConfigDao {


    public WorldConfigYamlDao(File file) {
        super(file);
    }

    @Override
    public Optional<WorldConfig> get(final @NonNull String key) {
        return getFromStringKey(key);
    }

    @Override
    public void remove(final @NonNull WorldConfig value) {
        saveFromStringKey(value.getWorld(), null);
    }

    @Override
    public void save(@NonNull WorldConfig config) {
        if (!(config instanceof ConfigurationSerializable)) {
            WorldConfig config2 = new WorldConfigYamlImpl(config.getWorld());
            config.getBiomeConfigs().forEach(config2::addBiomeConfig);
            config.getOreConfigs().forEach(config2::addOreConfig);
            config = config2;
        }

        saveFromStringKey(config.getWorld(), config);
    }

}
