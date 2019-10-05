package de.derfrzocker.custom.ore.generator.impl;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Deprecated //TODO remove in newer versions
public class DummyOreConfig extends OreConfigYamlImpl {

    DummyOreConfig(Material material, String oreGenerator, String blockSelector) {
        super("dummy_ore_config", material, oreGenerator, blockSelector);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException("Not supported");
    }

}
