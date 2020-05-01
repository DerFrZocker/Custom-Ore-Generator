/*
 * MIT License
 *
 * Copyright (c) 2019 Marvin (DerFrZocker)
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
import de.derfrzocker.custom.ore.generator.command.OreGenCommand;
import de.derfrzocker.custom.ore.generator.impl.BiomeConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.CustomOreGeneratorServiceImpl;
import de.derfrzocker.custom.ore.generator.impl.OreConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.WorldConfigYamlImpl;
import de.derfrzocker.custom.ore.generator.impl.blockselector.CountRangeBlockSelector;
import de.derfrzocker.custom.ore.generator.impl.blockselector.HighestBlockBlockSelector;
import de.derfrzocker.custom.ore.generator.impl.customdata.*;
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
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.WorldHandler_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.oregenerator.MinableGenerator_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.WorldHandler_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.oregenerator.MinableGenerator_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.WorldHandler_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.oregenerator.MinableGenerator_v1_15_R1;
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
import de.derfrzocker.spigot.utils.Version;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Supplier;

public class CustomOreGenerator extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(BiomeConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(OreConfigYamlImpl.class);
        ConfigurationSerialization.registerClass(WorldConfigYamlImpl.class);
    }

    private CustomOreGeneratorMessages messages;
    private Permissions permissions;

    @Override
    public void onLoad() {
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
        getCommand("oregen").setExecutor(new OreGenCommand(CustomOreGeneratorServiceSupplier.INSTANCE, this, messages, permissions));

        final RegisterUtil registerUtil = new RegisterUtil(this, CustomOreGeneratorServiceSupplier.INSTANCE.get(), Version.getCurrent());

        initWorldHandler();
        registerStandardOreGenerators(registerUtil);
        registerStandardBlockSelector(registerUtil);
        registerStandardCustomDatas(registerUtil);

        new Metrics(this);
    }

    private void registerStandardOreGenerators(@NotNull final RegisterUtil registerUtil) {
        registerUtil.register(new GlowStoneGenerator(name -> InfoUtil.getOreGenerator(this, name)));
        registerUtil.register(new RootGenerator(name -> InfoUtil.getOreGenerator(this, name)));
        registerUtil.register(new SingleOreGenerator(name -> InfoUtil.getOreGenerator(this, name)));
        registerUtil.register(Version.v1_8_R1, Version.v1_8_R1, () -> new MinableGenerator_v1_8_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_8_R2, Version.v1_8_R2, () -> new MinableGenerator_v1_8_R2(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_8_R3, Version.v1_8_R3, () -> new MinableGenerator_v1_8_R3(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_9_R1, Version.v1_9_R1, () -> new MinableGenerator_v1_9_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_9_R2, Version.v1_9_R2, () -> new MinableGenerator_v1_9_R2(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_10_R1, Version.v1_10_R1, () -> new MinableGenerator_v1_10_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_11_R1, Version.v1_11_R1, () -> new MinableGenerator_v1_11_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_12_R1, Version.v1_12_R1, () -> new MinableGenerator_v1_12_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_13_R1, Version.v1_13_R1, () -> new MinableGenerator_v1_13_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_13_R2, Version.v1_13_R2, () -> new MinableGenerator_v1_13_R2(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_13_R2, Version.v1_13_R2, true, () -> new MinableGenerator_v1_13_R2_paper(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_14_R1, Version.v1_14_R1, () -> new MinableGenerator_v1_14_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
        registerUtil.register(Version.v1_15_R1, Version.v1_15_R1, () -> new MinableGenerator_v1_15_R1(name -> InfoUtil.getOreGenerator(this, name)), true);
    }

    private void registerStandardBlockSelector(@NotNull final RegisterUtil registerUtil) {
        registerUtil.register(new HighestBlockBlockSelector(name -> InfoUtil.getBlockSelectorInfo(this, name)));
        registerUtil.register(new CountRangeBlockSelector(name -> InfoUtil.getBlockSelectorInfo(this, name)), true);
    }

    private void registerStandardCustomDatas(@NotNull final RegisterUtil registerUtil) {
        final File fileFolder = new File(getDataFolder(), "files");

        registerUtil.register(new SkullTextureCustomData(name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(new CommandCustomData(name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(new NBTTagCustomData(name -> InfoUtil.getCustomData(this, name), fileFolder));
        registerUtil.register(Version.v1_9_R1, () -> new AutoCustomData(name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_10_R1, () -> new BlockStateCustomData(CustomOreGeneratorServiceSupplier.INSTANCE, name -> InfoUtil.getCustomData(this, name), fileFolder));
        registerUtil.register(Version.v1_8_R1, Version.v1_12_R1, () -> new VariantCustomData(name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new TickBlockCustomData(name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new FacingCustomData(name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.DOWN, name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.UP, name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.NORTH, name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.SOUTH, name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.EAST, name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, () -> new DirectionCustomData(BlockFace.WEST, name -> InfoUtil.getCustomData(this, name)));
        registerUtil.register(Version.v1_13_R1, "ItemMods", () -> new ItemModsCustomData(name -> InfoUtil.getCustomData(this, name)));
    }

    private void initWorldHandler() {
        switch (Version.getCurrent()) {
            case v1_15_R1:
                new WorldHandler_v1_15_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_14_R1:
                new WorldHandler_v1_14_R1(this, CustomOreGeneratorServiceSupplier.INSTANCE);
                return;
            case v1_13_R2:
                if (Version.isPaper())
                    new WorldHandler_v1_13_R2_paper(this, CustomOreGeneratorServiceSupplier.INSTANCE);
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
