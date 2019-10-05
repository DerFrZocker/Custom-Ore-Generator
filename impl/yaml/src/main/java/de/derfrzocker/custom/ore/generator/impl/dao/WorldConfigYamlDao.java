package de.derfrzocker.custom.ore.generator.impl.dao;

import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.api.dao.WorldConfigDao;
import de.derfrzocker.custom.ore.generator.impl.WorldConfigYamlImpl;
import de.derfrzocker.spigot.utils.dao.yaml.BasicYamlDao;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

public class WorldConfigYamlDao extends BasicYamlDao<String, WorldConfig> implements WorldConfigDao {

    public WorldConfigYamlDao(File file) {
        super(file);
    }

    @Override
    public Optional<WorldConfig> get(@NotNull final String key) {
        Validate.notNull(key, "String key can not be null");

        return getFromStringKey(key);
    }

    @Override
    public void remove(@NotNull final WorldConfig config) {
        Validate.notNull(config, "WorldConfig can not be null");

        saveFromStringKey(config.getWorld(), null);
    }

    @Override
    public void save(@NotNull WorldConfig config) {
        Validate.notNull(config, "WorldConfig can not be null");

        if (!(config instanceof ConfigurationSerializable)) {
            final WorldConfig config2 = new WorldConfigYamlImpl(config.getWorld());
            config.getOreConfigs().forEach(config2::addOreConfig);
            config = config2;
        }

        saveFromStringKey(config.getWorld(), config);
    }

}
