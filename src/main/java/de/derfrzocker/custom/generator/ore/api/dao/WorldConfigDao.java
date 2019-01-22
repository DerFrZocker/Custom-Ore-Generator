package de.derfrzocker.custom.generator.ore.api.dao;

import de.derfrzocker.custom.generator.ore.api.WorldConfig;

import java.util.Optional;
import java.util.Set;

public interface WorldConfigDao {

    Optional<WorldConfig> get(String world);

    void remove(WorldConfig config);

    void save(WorldConfig config);

    Set<WorldConfig> getAll();


}
