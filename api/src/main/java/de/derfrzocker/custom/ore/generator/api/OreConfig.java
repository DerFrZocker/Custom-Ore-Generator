package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface OreConfig {

    String getName();

    boolean isActivated();

    boolean shouldGeneratedAll(); //TODO maybe better name

    void setActivated(boolean activated);

    void setGeneratedAll(boolean generatedAll); //TODO maybe better name

    Set<Biome> getBiomes();

    Material getMaterial();

    String getOreGenerator();

    Optional<Integer> getValue(OreSetting setting);

    void setValue(OreSetting setting, int value);

    Map<OreSetting, Integer> getOreSettings();

}
