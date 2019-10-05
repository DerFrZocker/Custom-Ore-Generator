package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.BiomeConfig;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

@SerializableAs(value = "CustomOreGenerator#WorldConfig")
public class WorldConfigYamlImpl implements WorldConfig, ConfigurationSerializable {

    private final static Pattern ORE_CONFIG_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");
    private final static String WORLD_KEY = "world";
    private final static String ORE_CONFIG_KEY = "ore-config";

    @NotNull
    private final String worldName;

    private final Map<String, OreConfig> oreConfigs = new HashMap<>();

    public WorldConfigYamlImpl(@NotNull final String worldName) {
        Validate.notNull(worldName, "World name can not be null");
        this.worldName = worldName;
    }

    public static WorldConfigYamlImpl deserialize(@NotNull final Map<String, Object> map) {
        final CustomOreGeneratorService service = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);
        final WorldConfigYamlImpl worldConfig = new WorldConfigYamlImpl((String) map.get(WORLD_KEY));

        Validate.notNull(service, "CustomOreGeneratorService can not be null");

        // map.entrySet().stream().filter(WorldConfigYamlImpl::isOreConfig).map(entry -> (OreConfig) entry.getValue()).forEach(worldConfig::addOreConfig);
        if (!map.containsKey(ORE_CONFIG_KEY)) {
            // old format version
            service.getLogger().info("Found old WorldConfig format, replacing it with new one");

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                if (!(value instanceof OreConfigYamlImpl))
                    continue;

                if (value instanceof DummyOreConfig) {

                    int index = 0;

                    while (worldConfig.oreConfigs.containsKey("ore-config_" + index)) {
                        index++;
                    }

                    final DummyOreConfig source = (DummyOreConfig) value;

                    final OreConfigYamlImpl oreConfig = new OreConfigYamlImpl("ore-config_" + index, source.getMaterial(), source.getOreGenerator(), source.getBlockSelector());

                    OreConfigYamlImpl.copyData(source, oreConfig);

                    worldConfig.addOreConfig(oreConfig);
                } else if (ORE_CONFIG_NAME_PATTERN.matcher(worldConfig.getWorld()).matches()) {
                    worldConfig.addOreConfig((OreConfig) value);
                } else {
                    // give it a new name, when it not match the name convention
                    int index = 0;

                    while (worldConfig.oreConfigs.containsKey("ore-config_" + index)) {
                        index++;
                    }
                    final OreConfigYamlImpl source = (OreConfigYamlImpl) value;

                    final OreConfigYamlImpl oreConfig = new OreConfigYamlImpl("ore-config_" + index, source.getMaterial(), source.getOreGenerator(), source.getBlockSelector());

                    OreConfigYamlImpl.copyData(source, oreConfig);

                    worldConfig.addOreConfig(oreConfig);
                }
            }

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!isBiomeConfig(entry))
                    continue;

                final Set<OreConfig> oreConfigs = worldConfig.convertBiomeConfig((BiomeConfig) entry.getValue());

                oreConfigs.forEach(worldConfig::addOreConfig);
            }

        } else {
            //new format version
            final List<?> oreConfigs = (List<?>) map.get(ORE_CONFIG_KEY);
            oreConfigs.forEach(oreConfig -> worldConfig.addOreConfig((OreConfig) oreConfig));
        }

        return worldConfig;
    }

    @Deprecated
    private static boolean isBiomeConfig(@NotNull final Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof BiomeConfig;
    }

    @NotNull
    @Override
    public String getWorld() {
        return worldName;
    }

    @NotNull
    @Override
    public Optional<OreConfig> getOreConfig(@NotNull final String name) {
        Validate.notNull(name, "World name can not be null");

        return Optional.ofNullable(this.oreConfigs.get(name));
    }

    @NotNull
    @Override
    public Set<OreConfig> getOreConfigs() {
        return new HashSet<>(oreConfigs.values());
    }

    @Override
    public void addOreConfig(@NotNull final OreConfig oreConfig) {
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(!this.oreConfigs.containsKey(oreConfig.getName()), "The OreConfig " + oreConfig.getName() + " is already added to the WorldConfig " + getWorld());

        this.oreConfigs.put(oreConfig.getName(), oreConfig);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> serialize = new LinkedHashMap<>();

        serialize.put(WORLD_KEY, getWorld());

        final Set<OreConfig> oreConfigSet = getOreConfigs();

        if (!oreConfigSet.isEmpty()) {
            final List<OreConfig> data = new LinkedList<>();

            oreConfigSet.forEach(oreConfig -> {
                // if the OreConfig is not an instance of ConfigurationSerializable,
                // than we need to create a new OreConfigYamlImpl and copy all data from the original to the new one
                // So that yaml can save it

                if (oreConfig instanceof ConfigurationSerializable) {
                    data.add(oreConfig);
                    return;
                }

                final OreConfigYamlImpl oreConfigYaml = new OreConfigYamlImpl(oreConfig.getName(), oreConfig.getMaterial(), oreConfig.getOreGenerator(), oreConfig.getBlockSelector());
                oreConfigYaml.setActivated(oreConfig.isActivated());
                oreConfigYaml.setGeneratedAll(oreConfig.shouldGeneratedAll());
                oreConfig.getBiomes().forEach(oreConfigYaml::addBiome);
                oreConfig.getCustomData().forEach(oreConfigYaml::setCustomData);
                oreConfig.getOreSettings().forEach(oreConfigYaml::setValue);

                data.add(oreConfigYaml);
            });

            serialize.put(ORE_CONFIG_KEY, data);
        }

        return serialize;
    }

    /**
     * converts the old BiomeConfig format to the new OreConfig format
     *
     * @param biomeConfig to format
     * @return a set with the new OreConfig format
     */
    @Deprecated
    private Set<OreConfig> convertBiomeConfig(@NotNull final BiomeConfig biomeConfig) {
        final Set<OreConfig> oreConfigs = new HashSet<>();

        int index = 0;

        while (this.oreConfigs.containsKey("biome-config_" + index)) {
            index++;
        }

        for (final OreConfig source : biomeConfig.getOreConfigs()) {
            final OreConfigYamlImpl oreConfig = new OreConfigYamlImpl("biome-config_" + index, source.getMaterial(), source.getOreGenerator(), source.getBlockSelector());

            OreConfigYamlImpl.copyData((OreConfigYamlImpl) source, oreConfig);

            oreConfig.setGeneratedAll(false);
            oreConfig.addBiome(biomeConfig.getBiome());

            oreConfigs.add(oreConfig);
            index++;
        }

        return oreConfigs;
    }

}
