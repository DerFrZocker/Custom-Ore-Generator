package de.derfrzocker.custom.ore.generator.impl.dao;

import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.api.dao.WorldConfigDao;
import de.derfrzocker.spigot.utils.dao.yaml.BasicYamlDao;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

@Deprecated
public class WorldConfigYamlDao_Old extends BasicYamlDao<String, WorldConfig> implements WorldConfigDao {

    public WorldConfigYamlDao_Old(File file) {
        super(file);
    }

    @Override
    public Optional<WorldConfig> get(@NotNull final String key) {
        Validate.notNull(key, "String key can not be null");

        return getFromStringKey(key);
    }

    @Override
    public void remove(@NotNull final WorldConfig config) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void save(@NotNull WorldConfig config) {
        throw new RuntimeException("Not supported");
    }

}
