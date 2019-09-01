package de.derfrzocker.custom.ore.generator.impl;

import org.bukkit.Material;

import java.util.Map;

@Deprecated //TODO remove in newer versions
public class DummyOreConfig extends OreConfigYamlImpl {

    public DummyOreConfig(Material material, String oreGenerator) {
        super("dummy_ore_config", material, oreGenerator);
    }

    @Override
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException("Not supported");
    }
}
