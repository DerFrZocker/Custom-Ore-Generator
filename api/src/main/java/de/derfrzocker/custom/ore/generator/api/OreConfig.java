package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Material;

import java.util.Map;
import java.util.Optional;

public interface OreConfig {

    Material getMaterial();

    String getOreGenerator();

    Optional<Integer> getValue(OreSetting setting);

    void setValue(OreSetting setting, int value);

    Map<OreSetting, Integer> getOreSettings();

}
