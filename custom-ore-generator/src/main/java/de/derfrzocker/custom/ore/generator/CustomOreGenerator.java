/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.custom.ore.generator.impl.BiomeConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.CustomOreGeneratorServiceImpl;
import de.derfrzocker.custom.ore.generator.impl.OreConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.OreSettingsContainerYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.WorldConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.blockselector.CountRangeBlockSelector;
import de.derfrzocker.custom.ore.generator.impl.blockselector.HighestBlockBlockSelector;
import de.derfrzocker.custom.ore.generator.impl.customdata.AutoCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.BlockStateCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.CommandCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.DirectionCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.FacingCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.ItemModsCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.NBTTagCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.OraxenCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.SkullTextureCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.TickBlockCustomData;
import de.derfrzocker.custom.ore.generator.impl.customdata.VariantCustomData;
import de.derfrzocker.custom.ore.generator.impl.dao.OreConfigYamlDao;
import de.derfrzocker.custom.ore.generator.impl.dao.WorldConfigYamlDao;
import de.derfrzocker.custom.ore.generator.impl.dao.WorldConfigYamlDao_Old;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.GlowStoneGenerator;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.RootGenerator;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.SingleOreGenerator;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.CustomOreBlockPopulator_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.oregenerator.MinableGenerator_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.CustomOreBlockPopulator_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.oregenerator.MinableGenerator_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.CustomOreBlockPopulator_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.oregenerator.MinableGenerator_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.WorldHandler_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.oregenerator.MinableGenerator_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.WorldHandler_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.oregenerator.MinableGenerator_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.WorldHandler_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.oregenerator.MinableGenerator_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.WorldHandler_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.oregenerator.MinableGenerator_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R1.WorldHandler_v1_16_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R1.oregenerator.MinableGenerator_v1_16_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R2.WorldHandler_v1_16_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R2.oregenerator.MinableGenerator_v1_16_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R3.WorldHandler_v1_16_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_16_R3.oregenerator.MinableGenerator_v1_16_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_17_R1.WorldHandler_v1_17_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_17_R1.oregenerator.MinableGenerator_v1_17_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R1.WorldHandler_v1_18_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R1.oregenerator.MinableGenerator_v1_18_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R2.WorldHandler_v1_18_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_18_R2.oregenerator.MinableGenerator_v1_18_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R1.WorldHandler_v1_19_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R1.oregenerator.MinableGenerator_v1_19_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R2.WorldHandler_v1_19_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R2.oregenerator.MinableGenerator_v1_19_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R3.WorldHandler_v1_19_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_19_R3.oregenerator.MinableGenerator_v1_19_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R1.WorldHandler_v1_20_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R1.oregenerator.MinableGenerator_v1_20_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R2.WorldHandler_v1_20_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R2.oregenerator.MinableGenerator_v1_20_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.CustomOreBlockPopulator_v1_8_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.oregenerator.MinableGenerator_v1_8_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.CustomOreBlockPopulator_v1_8_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.oregenerator.MinableGenerator_v1_8_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.CustomOreBlockPopulator_v1_8_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.oregenerator.MinableGenerator_v1_8_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.CustomOreBlockPopulator_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.oregenerator.MinableGenerator_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.CustomOreBlockPopulator_v1_9_R2;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.oregenerator.MinableGenerator_v1_9_R2;
import de.derfrzocker.custom.ore.generator.utils.InfoUtil;
import de.derfrzocker.custom.ore.generator.utils.RegisterUtil;
import de.derfrzocker.spigot.utils.Config;
import de.derfrzocker.spigot.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;

public class CustomOreGenerator extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(BiomeConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(WorldConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreSettingsContainerYamlImpl.class);
    }

    private CustomOreGeneratorMessages messages;
    private Permissions permissions;
    private Version version = Version.UNKNOWN;
    private boolean shouldLoad = true;

    @Override
    public void onLoad() {
        version = Version.getServerVersion(getServer());

        // if no suitable version was found, log and return
        if (version == Version.UNKNOWN) {
            getLogger().warning("The Server version which you are running is unsupported, you are running version '" + version + "'");
            getLogger().warning("The plugin supports following versions " + combineVersions(Version.v1_8_R1, Version.v1_8_R2, Version.v1_8_R3,
                    Version.v1_9_R1, Version.v1_9_R2, Version.v1_10_R1, Version.v1_11_R1, Version.v1_12_R1, Version.v1_13_R1, Version.v1_13_R2,
                    Version.v1_14_R1, Version.v1_15_R1, Version.v1_16_R1, Version.v1_16_R2, Version.v1_16_R3, Version.v1_17_R1, Version.v1_18_R1,
                    Version.v1_18_R2, Version.v1_19_R1, Version.v1_19_R2, Version.v1_19_R3, Version.v1_20_R1, Version.v1_20_R2));
            getLogger().warning("(Spigot / Paper version 1.8 - 1.20.2), if you are running such a Minecraft version, than your bukkit implementation is unsupported, in this case please contact the developer, so he can resolve this Issue");

            if (version == Version.UNKNOWN) {
                getLogger().warning("The Version '" + version + "' can indicate, that you are using a newer Minecraft version than currently supported.");
                getLogger().warning("In this case please update to the newest version of this plugin. If this is the newest Version, than please be patient. It can take some weeks until the plugin is updated");
            }
            shouldLoad = false;
            return;
        }

        if (version == Version.v1_17_R1) {
            try {
                Class.forName("org.bukkit.generator.WorldInfo");
            } catch (ClassNotFoundException e) {
                // Unsupported version
                shouldLoad = false;
                getLogger().warning("The server version which you are running is unsupported");
                getLogger().warning("Make sure that you are running 1.17.1 Spigot build 3218 or higher, 1.17 is not supported.");
                return;
            }
        }

        messages = new CustomOreGeneratorMessages(this);
        permissions = new Permissions(this);

        final WorldConfigYamlDao worldConfigYamlDao = new WorldConfigYamlDao(new File(getDataFolder(), "data/world-config"));
        final OreConfigYamlDao oreConfigYamlDao = new OreConfigYamlDao(new File(getDataFolder(), "data/ore-config"));
        final CustomOreGeneratorService service = new CustomOreGeneratorServiceImpl(worldConfigYamlDao, oreConfigYamlDao, getLogger());

        getServer().getServicesManager().register(CustomOreGeneratorService.class, service, this, ServicePriority.Normal);

        oreConfigYamlDao.init();
        worldConfigYamlDao.init();

        checkOldStorageType();
    }

    @Override
    public void onEnable() {
        if (version == Version.UNKNOWN || !shouldLoad) {
            // print a stack Trace, so that the server owner can easily spot, that the plugin is not working
            getLogger().log(Level.WARNING, "No compatible Server version found!", new IllegalStateException("No compatible Server version found!"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("oregen").setExecutor(new OreGenCommand(CustomOreGeneratorServiceSupplier.INSTANCE, this, messages, permissions));

        if (version.isNewerOrSameThan(Version.v1_14_R1)) {
            checkFile("data/factory/gui/menu-gui.yml");
        }

        checkFile("data/factory/gui/biome-gui.yml");

        final RegisterUtil registerUtil = new RegisterUtil(this, CustomOreGeneratorServiceSupplier.INSTANCE.get(), version, Version.isPaper(getServer()));

        initWorldHandler();
        registerStandardOreGenerators(registerUtil);
        registerStandardBlockSelector(registerUtil);
        registerStandardCustomDatas(registerUtil);

        new CustomOreGeneratorMetrics(this, CustomOreGeneratorServiceSupplier.INSTANCE);

        final CustomOreGeneratorService service = CustomOreGeneratorServiceSupplier.INSTANCE.get();
        // re-save all ore-configs to apply new save formats
        service.getOreConfigs().forEach(service::saveOreConfig);

        // re-save all world-configs to apply new save formats
        service.getWorldConfigs().forEach(service::saveWorldConfig);

    }

    private void registerStandardOreGenerators(@NotNull final RegisterUtil registerUtil) {
        final Function<String, Info> infoFunction = name -> InfoUtil.getOreGeneratorInfo(this, name);
        final BiFunction<String, OreSetting, Info> oreSettingInfoBiFunction = (name, oreSetting) -> InfoUtil.getOreGeneratorOreSettingInfo(this, name, oreSetting);

        registerUtil.register(new GlowStoneGenerator(infoFunction, oreSettingInfoBiFunction));
        registerUtil.register(new RootGenerator(infoFunction, oreSettingInfoBiFunction));
        registerUtil.register(new SingleOreGenerator(infoFunction, oreSettingInfoBiFunction));
        registerUtil.register(Version.v1_8_R1, Version.v1_8_R1, () -> new MinableGenerator_v1_8_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_8_R2, Version.v1_8_R2, () -> new MinableGenerator_v1_8_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_8_R3, Version.v1_8_R3, () -> new MinableGenerator_v1_8_R3(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_9_R1, Version.v1_9_R1, () -> new MinableGenerator_v1_9_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_9_R2, Version.v1_9_R2, () -> new MinableGenerator_v1_9_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_10_R1, Version.v1_10_R1, () -> new MinableGenerator_v1_10_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_11_R1, Version.v1_11_R1, () -> new MinableGenerator_v1_11_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_12_R1, Version.v1_12_R1, () -> new MinableGenerator_v1_12_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_13_R1, Version.v1_13_R1, () -> new MinableGenerator_v1_13_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_13_R2, Version.v1_13_R2, false, () -> new MinableGenerator_v1_13_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_14_R1, Version.v1_14_R1, () -> new MinableGenerator_v1_14_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_15_R1, Version.v1_15_R1, () -> new MinableGenerator_v1_15_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_16_R1, Version.v1_16_R1, () -> new MinableGenerator_v1_16_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_16_R2, Version.v1_16_R2, () -> new MinableGenerator_v1_16_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_16_R3, Version.v1_16_R3, () -> new MinableGenerator_v1_16_R3(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_17_R1, Version.v1_17_R1, () -> new MinableGenerator_v1_17_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_18_R1, Version.v1_18_R1, () -> new MinableGenerator_v1_18_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_18_R2, Version.v1_18_R2, () -> new MinableGenerator_v1_18_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_19_R1, Version.v1_19_R1, () -> new MinableGenerator_v1_19_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_19_R2, Version.v1_19_R2, () -> new MinableGenerator_v1_19_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_19_R3, Version.v1_19_R3, () -> new MinableGenerator_v1_19_R3(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_20_R1, Version.v1_20_R1, () -> new MinableGenerator_v1_20_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(Version.v1_20_R2, Version.v1_20_R2, () -> new MinableGenerator_v1_20_R2(infoFunction, oreSettingInfoBiFunction), true);
    }

    private void registerStandardBlockSelector(@NotNull final RegisterUtil registerUtil) {
        final Function<String, Info> infoFunction = name -> InfoUtil.getBlockSelectorInfo(this, name);
        final BiFunction<String, OreSetting, Info> oreSettingInfoBiFunction = (name, oreSetting) -> InfoUtil.getBlockSelectorOreSettingInfo(this, name, oreSetting);

        registerUtil.register(new HighestBlockBlockSelector(infoFunction, oreSettingInfoBiFunction));
        registerUtil.register(new CountRangeBlockSelector(infoFunction, oreSettingInfoBiFunction), true);
    }

    private void registerStandardCustomDatas(@NotNull final RegisterUtil registerUtil) {
        final Function<String, Info> infoFunction = name -> InfoUtil.getCustomDataInfo(this, name);
        final File fileFolder = new File(getDataFolder(), "files");

        registerUtil.register(new SkullTextureCustomData(infoFunction));
        registerUtil.register(new CommandCustomData(infoFunction));
        registerUtil.register(new NBTTagCustomData(infoFunction, fileFolder));
        registerUtil.register(Version.v1_9_R1, () -> new AutoCustomData(infoFunction));
        registerUtil.register(Version.v1_10_R1, () -> new BlockStateCustomData(CustomOreGeneratorServiceSupplier.INSTANCE, infoFunction, fileFolder));
        registerUtil.register(Version.v1_8_R1, Version.v1_12_R1, () -> new VariantCustomData(infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new TickBlockCustomData(infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new FacingCustomData(infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.DOWN, infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.UP, infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.NORTH, infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.SOUTH, infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.EAST, infoFunction));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.WEST, infoFunction));
        registerUtil.register(Version.v1_14_R1, "ItemMods", () -> new ItemModsCustomData(infoFunction));
        registerUtil.register(Version.v1_14_R1, "Oraxen", () -> new OraxenCustomData(infoFunction));
    }

    private void initWorldHandler() {
        switch (version) {
            case v1_20_R2:
                new WorldHandler_v1_20_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                break;
            case v1_20_R1:
                new WorldHandler_v1_20_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                break;
            case v1_19_R3:
                new WorldHandler_v1_19_R3(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                break;
            case v1_19_R2:
                new WorldHandler_v1_19_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                break;
            case v1_19_R1:
                new WorldHandler_v1_19_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                break;
            case v1_18_R2:
                new WorldHandler_v1_18_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                break;
            case v1_18_R1:
                new WorldHandler_v1_18_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_17_R1:
                new WorldHandler_v1_17_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_16_R3:
                new WorldHandler_v1_16_R3(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_16_R2:
                new WorldHandler_v1_16_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_16_R1:
                new WorldHandler_v1_16_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_15_R1:
                new WorldHandler_v1_15_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_14_R1:
                new WorldHandler_v1_14_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_13_R2:
                if (Version.isPaper(getServer()))
                    getLogger().warning("Paper is not supported on this version of Minecraft");
                else
                    new WorldHandler_v1_13_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_13_R1:
                new WorldHandler_v1_13_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_12_R1:
                new CustomOreBlockPopulator_v1_12_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_11_R1:
                new CustomOreBlockPopulator_v1_11_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_10_R1:
                new CustomOreBlockPopulator_v1_10_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_9_R2:
                new CustomOreBlockPopulator_v1_9_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_9_R1:
                new CustomOreBlockPopulator_v1_9_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_8_R3:
                new CustomOreBlockPopulator_v1_8_R3(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_8_R2:
                new CustomOreBlockPopulator_v1_8_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_8_R1:
                new CustomOreBlockPopulator_v1_8_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
        }
    }

    @Deprecated
    private void checkOldStorageType() {
        final File file = new File(getDataFolder(), "data/world_configs.yml");

        if (!file.exists())
            return;

        if (file.isDirectory()) {
            getLogger().info("WTF?? why??");
            return;
        }

        getLogger().info("Found old storage type, convert to new one");

        final WorldConfigYamlDao_Old worldConfigYamlDao = new WorldConfigYamlDao_Old(new File(getDataFolder(), "data/world_configs.yml"));
        worldConfigYamlDao.init();

        if (!file.delete())
            throw new RuntimeException("Can not delete File " + file);

        getLogger().info("Finish converting old storage format to new one");
    }

    private void checkFile(@NotNull final String name) {
        final File file = new File(getDataFolder(), name);

        if (!file.exists())
            return;

        final YamlConfiguration configuration = new Config(file);

        final YamlConfiguration configuration2 = new Config(getResource(name));

        if (configuration.getInt("version") == configuration2.getInt("version"))
            return;

        getLogger().warning("File " + name + " has an outdated / new version, replacing it!");

        if (!file.delete())
            throw new RuntimeException("can't delete file " + name + " stop plugin start!");

        saveResource(name, true);
    }

    private static final class CustomOreGeneratorServiceSupplier implements Supplier<CustomOreGeneratorService> {

        private static final CustomOreGeneratorServiceSupplier INSTANCE = new CustomOreGeneratorServiceSupplier();

        private CustomOreGeneratorService service;

        @Override
        public CustomOreGeneratorService get() {
            final CustomOreGeneratorService tempService = Bukkit.getServicesManager().load(CustomOreGeneratorService.class);

            if (service == null && tempService == null)
                throw new NullPointerException("The Bukkit Service has no CustomOreGeneratorService and no CustomOreGeneratorService is cached!");

            if (tempService != null && service != tempService)
                service = tempService;

            return service;
        }

    }

    private String combineVersions(Version... versions) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean first = true;

        for (Version version : versions) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(" ");
            }

            stringBuilder.append("'");
            stringBuilder.append(version);
            stringBuilder.append("'");
        }

        return stringBuilder.toString();
    }

}
