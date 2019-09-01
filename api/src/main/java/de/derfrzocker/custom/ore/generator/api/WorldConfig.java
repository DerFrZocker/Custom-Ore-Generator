package de.derfrzocker.custom.ore.generator.api;

import java.util.Optional;
import java.util.Set;

public interface WorldConfig {

    String getWorld();

    Optional<OreConfig> getOreConfig(String name);

    Set<OreConfig> getOreConfigs();

    void addOreConfig(OreConfig oreConfig);

}
