package de.derfrzocker.custom.genrator.ore.impl;

import de.derfrzocker.custom.generator.ore.api.CustomOreGeneratorService;
import de.derfrzocker.custom.generator.ore.api.OreGenerator;
import de.derfrzocker.custom.generator.ore.api.WorldConfig;
import de.derfrzocker.custom.generator.ore.api.dao.WorldConfigDao;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class CustomOreGeneratorServiceImpl implements CustomOreGeneratorService {

    @NonNull
    private final WorldConfigDao dao;

    private final Map<String, OreGenerator> oreGenerator = new HashMap<>();

    @Getter
    private OreGenerator defaultOreGenerator;

    @Override
    public Optional<OreGenerator> getOreGenerator(String name) {
        return Optional.ofNullable(oreGenerator.get(name));
    }

    @Override
    public void registerOreGenerator(OreGenerator oreGenerator) {
        this.oreGenerator.put(oreGenerator.getName(), oreGenerator);
    }

    @Override
    public Optional<WorldConfig> getWorldConfig(String world) {
        return dao.get(world);
    }

    @Override
    public WorldConfig createWorldConfig(World world) {
        WorldConfig worldConfig = new WorldConfigYamlImpl(world.getName());

        saveWorldConfig(worldConfig);

        return worldConfig;
    }

    @Override
    public void saveWorldConfig(WorldConfig config) {
        dao.save(config);
    }

    @Override
    public Set<WorldConfig> getWorldConfigs() {
        return dao.getAll();
    }

    @Override
    public void setDefaultOreGenerator(OreGenerator oreGenerator) {
        registerOreGenerator(oreGenerator);
        defaultOreGenerator = oreGenerator;
    }


}
