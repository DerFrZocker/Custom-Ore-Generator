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
import de.derfrzocker.custom.ore.generator.api.WorldHandler;
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
import de.derfrzocker.custom.ore.generator.impl.v1_20_R3.WorldHandler_v1_20_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R3.oregenerator.MinableGenerator_v1_20_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R4.WorldHandler_v1_20_R4;
import de.derfrzocker.custom.ore.generator.impl.v1_20_R4.oregenerator.MinableGenerator_v1_20_R4;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R1.WorldHandler_v1_21_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_21_R1.oregenerator.MinableGenerator_v1_21_R1;
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
import de.derfrzocker.custom.ore.generator.plugin.oraxen.v1.OraxenCustomData_v1;
import de.derfrzocker.custom.ore.generator.plugin.oraxen.v2.OraxenCustomData_v2;
import de.derfrzocker.custom.ore.generator.utils.InfoUtil;
import de.derfrzocker.custom.ore.generator.utils.RegisterUtil;
import de.derfrzocker.spigot.utils.Config;
import de.derfrzocker.spigot.utils.version.InternalVersion;
import de.derfrzocker.spigot.utils.version.ServerVersion;
import de.derfrzocker.spigot.utils.version.ServerVersionRange;
import dev.linwood.api.server.Version;
import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CustomOreGenerator extends JavaPlugin {

    private static final ServerVersionRange[] SUPPORTED_VERSION = new ServerVersionRange[]{ServerVersionRange.V1_21, ServerVersionRange.V1_20, ServerVersionRange.V1_19,
            ServerVersionRange.V1_18, ServerVersionRange.V1_17, ServerVersionRange.V1_16, ServerVersionRange.V1_15, ServerVersionRange.V1_14,
            ServerVersionRange.V1_13, ServerVersionRange.V1_12, ServerVersionRange.V1_11, ServerVersionRange.V1_10, ServerVersionRange.V1_9,
            ServerVersionRange.V1_8};

    static {
        ConfigurationSerialization.registerClass(BiomeConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(WorldConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreSettingsContainerYamlImpl.class);
    }

    private CustomOreGeneratorMessages messages;
    private Permissions permissions;
    private ServerVersion version = ServerVersion.NONE;
    private boolean loaded = false;

    @Override
    public void onLoad() {
        version = ServerVersion.getCurrentVersion(getServer());
        if (!ServerVersion.isSupportedVersion(getLogger(), version, SUPPORTED_VERSION)) {
            return;
        }

        if (InternalVersion.v1_17_R1.getServerVersionRange().isInRange(version)) {
            try {
                Class.forName("org.bukkit.generator.WorldInfo");
            } catch (ClassNotFoundException e) {
                // Unsupported version
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

        loaded = true;
    }

    @Override
    public void onEnable() {
        if (!loaded) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("oregen").setExecutor(new OreGenCommand(CustomOreGeneratorServiceSupplier.INSTANCE, this, messages, permissions));

        if (version.isNewerThanOrSameAs(InternalVersion.v1_14_R1.getServerVersionRange().minInclusive())) {
            checkFile("data/factory/gui/menu-gui.yml");
        }

        checkFile("data/factory/gui/biome-gui.yml");

        final RegisterUtil registerUtil = new RegisterUtil(this, CustomOreGeneratorServiceSupplier.INSTANCE.get(), version);

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
        registerUtil.register(InternalVersion.v1_8_R1, InternalVersion.v1_8_R1, () -> new MinableGenerator_v1_8_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_8_R2, InternalVersion.v1_8_R2, () -> new MinableGenerator_v1_8_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_8_R3, InternalVersion.v1_8_R3, () -> new MinableGenerator_v1_8_R3(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_9_R1, InternalVersion.v1_9_R1, () -> new MinableGenerator_v1_9_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_9_R2, InternalVersion.v1_9_R2, () -> new MinableGenerator_v1_9_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_10_R1, InternalVersion.v1_10_R1, () -> new MinableGenerator_v1_10_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_11_R1, InternalVersion.v1_11_R1, () -> new MinableGenerator_v1_11_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_12_R1, InternalVersion.v1_12_R1, () -> new MinableGenerator_v1_12_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_13_R1, InternalVersion.v1_13_R1, () -> new MinableGenerator_v1_13_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_13_R2, InternalVersion.v1_13_R2, () -> new MinableGenerator_v1_13_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_14_R1, InternalVersion.v1_14_R1, () -> new MinableGenerator_v1_14_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_15_R1, InternalVersion.v1_15_R1, () -> new MinableGenerator_v1_15_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_16_R1, InternalVersion.v1_16_R1, () -> new MinableGenerator_v1_16_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_16_R2, InternalVersion.v1_16_R2, () -> new MinableGenerator_v1_16_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_16_R3, InternalVersion.v1_16_R3, () -> new MinableGenerator_v1_16_R3(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_17_R1, InternalVersion.v1_17_R1, () -> new MinableGenerator_v1_17_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_18_R1, InternalVersion.v1_18_R1, () -> new MinableGenerator_v1_18_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_18_R2, InternalVersion.v1_18_R2, () -> new MinableGenerator_v1_18_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_19_R1, InternalVersion.v1_19_R1, () -> new MinableGenerator_v1_19_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_19_R2, InternalVersion.v1_19_R2, () -> new MinableGenerator_v1_19_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_19_R3, InternalVersion.v1_19_R3, () -> new MinableGenerator_v1_19_R3(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_20_R1, InternalVersion.v1_20_R1, () -> new MinableGenerator_v1_20_R1(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_20_R2, InternalVersion.v1_20_R2, () -> new MinableGenerator_v1_20_R2(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_20_R3, InternalVersion.v1_20_R3, () -> new MinableGenerator_v1_20_R3(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_20_R4, InternalVersion.v1_20_R4, () -> new MinableGenerator_v1_20_R4(infoFunction, oreSettingInfoBiFunction), true);
        registerUtil.register(InternalVersion.v1_21_R1, InternalVersion.v1_21_R1, () -> new MinableGenerator_v1_21_R1(infoFunction, oreSettingInfoBiFunction), true);
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
        registerUtil.register(InternalVersion.v1_9_R1, () -> new AutoCustomData(infoFunction));
        registerUtil.register(InternalVersion.v1_10_R1, () -> new BlockStateCustomData(CustomOreGeneratorServiceSupplier.INSTANCE, infoFunction, fileFolder));
        registerUtil.register(InternalVersion.v1_8_R1, InternalVersion.v1_12_R1, () -> new VariantCustomData(infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new TickBlockCustomData(infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new FacingCustomData(infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new DirectionCustomData(BlockFace.DOWN, infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new DirectionCustomData(BlockFace.UP, infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new DirectionCustomData(BlockFace.NORTH, infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new DirectionCustomData(BlockFace.SOUTH, infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new DirectionCustomData(BlockFace.EAST, infoFunction));
        registerUtil.register(InternalVersion.v1_13_R1, () -> new DirectionCustomData(BlockFace.WEST, infoFunction));
        registerUtil.register(InternalVersion.v1_14_R1, "ItemMods", () -> new ItemModsCustomData(infoFunction));
        registerUtil.register(InternalVersion.v1_17_R1, "Oraxen", plugin -> plugin.getDescription().getVersion().startsWith("1."), () -> new OraxenCustomData_v1(infoFunction));
        registerUtil.register(InternalVersion.v1_18_R1, "Oraxen", plugin -> plugin.getDescription().getVersion().startsWith("2."), () -> new OraxenCustomData_v2(infoFunction));
    }

    private WorldHandler initWorldHandler() {
        if (InternalVersion.v1_21_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_21_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_20_R4.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_20_R4(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_20_R3.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_20_R3(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_20_R2.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_20_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_20_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_20_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_19_R3.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_19_R3(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_19_R2.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_19_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_19_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_19_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_18_R2.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_18_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_18_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_18_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_17_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_17_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_16_R3.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_16_R3(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_16_R2.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_16_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_16_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_16_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_15_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_15_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_14_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_14_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_13_R2.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_13_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_13_R1.getServerVersionRange().isInRange(version)) {
            return new WorldHandler_v1_13_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_12_R1.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_12_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_11_R1.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_11_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_10_R1.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_10_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_9_R2.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_9_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_9_R1.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_9_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_8_R3.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_8_R3(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_8_R2.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_8_R2(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        } else if (InternalVersion.v1_8_R1.getServerVersionRange().isInRange(version)) {
            return new CustomOreBlockPopulator_v1_8_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
        }

        throw new IllegalStateException(String.format("No NMSReplacer found for version '%s', this is a bug!", version));
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

}
