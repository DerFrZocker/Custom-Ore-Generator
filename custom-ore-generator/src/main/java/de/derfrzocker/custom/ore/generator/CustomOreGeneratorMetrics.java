package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import org.apache.commons.lang.Validate;
import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomOreGeneratorMetrics {

    public CustomOreGeneratorMetrics(@NotNull final Plugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(serviceSupplier, "Service supplier can not be null");

        final Metrics metrics = new Metrics(plugin);

        metrics.addCustomChart(new Metrics.SingleLineChart("used_in_worlds", () -> serviceSupplier.get().getWorldConfigs().size()));
        metrics.addCustomChart(new Metrics.SingleLineChart("created_ore_configs", () -> serviceSupplier.get().getOreConfigs().size()));
        metrics.addCustomChart(new Metrics.AdvancedPie("used_material", () -> {
            final Map<String, Integer> result = new LinkedHashMap<>();

            for (final OreConfig oreConfig : serviceSupplier.get().getOreConfigs()) {
                final int count = result.getOrDefault(oreConfig.getMaterial().toString(), 0);

                result.put(oreConfig.getMaterial().toString(), count + 1);
            }

            return result;
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("used_replace_material", () -> {
            final Map<String, Integer> result = new LinkedHashMap<>();

            for (final OreConfig oreConfig : serviceSupplier.get().getOreConfigs()) {
                for (final Material material : oreConfig.getReplaceMaterials()) {
                    final int count = result.getOrDefault(material.toString(), 0);

                    result.put(material.toString(), count + 1);
                }
            }

            return result;
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("used_select_material", () -> {
            final Map<String, Integer> result = new LinkedHashMap<>();

            for (final OreConfig oreConfig : serviceSupplier.get().getOreConfigs()) {
                for (final Material material : oreConfig.getSelectMaterials()) {
                    final int count = result.getOrDefault(material.toString(), 0);

                    result.put(material.toString(), count + 1);
                }
            }

            return result;
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("used_biomes", () -> {
            final Map<String, Integer> result = new LinkedHashMap<>();

            for (final OreConfig oreConfig : serviceSupplier.get().getOreConfigs()) {
                for (final Biome biome : oreConfig.getBiomes()) {
                    final int count = result.getOrDefault(biome.toString(), 0);

                    result.put(biome.toString(), count + 1);
                }
            }

            return result;
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("used_custom_data", () -> {
            final Map<String, Integer> result = new LinkedHashMap<>();

            for (final OreConfig oreConfig : serviceSupplier.get().getOreConfigs()) {
                for (final CustomData customData : oreConfig.getCustomData().keySet()) {
                    final int count = result.getOrDefault(customData.getName(), 0);

                    result.put(customData.getName(), count + 1);
                }
            }

            return result;
        }));

        metrics.addCustomChart(new Metrics.DrilldownPie("used_ore_generator", () -> {
            final Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

            for (final OreConfig oreConfig : serviceSupplier.get().getOreConfigs()) {
                final String oreGenerator = oreConfig.getOreGenerator();
                final Map<String, Integer> settingsMap = result.computeIfAbsent(oreGenerator, name -> new LinkedHashMap<>());

                settingsMap.put("used", settingsMap.getOrDefault("used", 0) + 1);

                for (final OreSetting oreSetting : oreConfig.getOreGeneratorOreSettings().getValues().keySet()) {
                    final int count = settingsMap.getOrDefault(oreSetting.getName(), 0);

                    settingsMap.put(oreSetting.getName(), count + 1);
                }
            }

            return result;
        }));

        metrics.addCustomChart(new Metrics.DrilldownPie("used_block_selector", () -> {
            final Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

            for (final OreConfig oreConfig : serviceSupplier.get().getOreConfigs()) {
                final String blockSelector = oreConfig.getBlockSelector();
                final Map<String, Integer> settingsMap = result.computeIfAbsent(blockSelector, name -> new LinkedHashMap<>());

                settingsMap.put("used", settingsMap.getOrDefault("used", 0) + 1);

                for (final OreSetting oreSetting : oreConfig.getBlockSelectorOreSettings().getValues().keySet()) {
                    final int count = settingsMap.getOrDefault(oreSetting.getName(), 0);

                    settingsMap.put(oreSetting.getName(), count + 1);
                }
            }

            return result;
        }));

        if (!metrics.isEnabled()) {
            plugin.getLogger().warning("meh");
        }

    }

}
