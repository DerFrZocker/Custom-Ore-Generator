package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.api.dao.WorldConfigDao;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

import java.util.*;

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

    @Override
    public Random createRandom(final long seed, final int x, final int z) {
        final Random random = new Random(seed);

        long long1 = random.nextLong();
        long long2 = random.nextLong();
        long newseed = (long) x * long1 ^ (long) z * long2 ^ seed;
        random.setSeed(newseed);

        return random;
    }


}
