package de.derfrzocker.custom.generator.ore.impl.dao;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.generator.ore.api.WorldConfig;
import de.derfrzocker.custom.generator.ore.api.dao.WorldConfigDao;
import de.derfrzocker.custom.generator.ore.impl.WorldConfigYamlImpl;
import de.derfrzocker.custom.generator.ore.util.Config;
import de.derfrzocker.custom.generator.ore.util.YamlReloadAble;
import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class WorldConfigYamlDao implements WorldConfigDao, YamlReloadAble {

    @NonNull
    private File file;

    @NonNull
    private YamlConfiguration yaml; //TODO Check Thread safety

    public WorldConfigYamlDao(File file) {
        this.file = file;
        yaml = new Config(file);
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<WorldConfig> get(@NonNull String world) {
        Object object = yaml.get(world);

        if (object == null)
            return Optional.empty();

        return Optional.of((WorldConfig) object);
    }

    @Override
    public void remove(@NonNull WorldConfig config) {
        yaml.set(config.getWorld(), null);

        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(@NonNull WorldConfig config) {
        if (!(config instanceof ConfigurationSerializable)) {
            WorldConfig config2 = new WorldConfigYamlImpl(config.getWorld());
            config.getBiomeConfigs().forEach(config2::addBiomeConfig);
            config.getOreConfigs().forEach(config2::addOreConfig);
            config = config2;
        }

        yaml.set(config.getWorld(), config);

        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<WorldConfig> getAll() {
        return Sets.newHashSet(yaml.getKeys(false).stream().map(yaml::get).filter(Objects::nonNull).filter(value -> value instanceof WorldConfig).map(value -> (WorldConfig) value).toArray(WorldConfig[]::new));
    }

    @Override
    public void reload(File file) {
        this.file = file;
        reload();
    }

    @Override
    public void reload() {
        yaml = new Config(file);
    }
}
