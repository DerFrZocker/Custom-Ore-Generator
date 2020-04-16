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

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
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
import de.derfrzocker.custom.ore.generator.utils.InfoUtil;
import de.derfrzocker.custom.ore.generator.utils.VersionPicker;
import de.derfrzocker.spigot.utils.Version;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

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

        Bukkit.getServicesManager().register(CustomOreGeneratorService.class, service, this, ServicePriority.Normal);

        //register BlockSelector
        final BlockSelector blockSelector = new CountRangeBlockSelector(name -> InfoUtil.getBlockSelectorInfo(this, name));
        service.registerBlockSelector(blockSelector);
        service.setDefaultBlockSelector(blockSelector);
        service.registerBlockSelector(new HighestBlockBlockSelector(name -> InfoUtil.getBlockSelectorInfo(this, name)));
        service.registerOreGenerator(new GlowStoneGenerator(name -> InfoUtil.getOreGenerator(this, name)));
        service.registerOreGenerator(new RootGenerator(name -> InfoUtil.getOreGenerator(this, name)));

        // register CustomData
        service.registerCustomData(new SkullTextureCustomData(name -> InfoUtil.getCustomData(this, name)));

        service.registerCustomData(new CommandCustomData(name -> InfoUtil.getCustomData(this, name)));
        service.registerCustomData(new NBTTagCustomData(name -> InfoUtil.getCustomData(this, name)));

        if (Version.v1_9_R1.isNewerOrSameVersion(Version.getCurrent()))
            service.registerCustomData(new AutoCustomData(name -> InfoUtil.getCustomData(this, name)));

        if (Version.v1_13_R1.isNewerOrSameVersion(Version.getCurrent())) {
            service.registerCustomData(new TickBlockCustomData(name -> InfoUtil.getCustomData(this, name)));
            service.registerCustomData(new FacingCustomData(name -> InfoUtil.getCustomData(this, name)));
            service.registerCustomData(new DirectionCustomData(BlockFace.DOWN, name -> InfoUtil.getCustomData(this, name)));
            service.registerCustomData(new DirectionCustomData(BlockFace.UP, name -> InfoUtil.getCustomData(this, name)));
            service.registerCustomData(new DirectionCustomData(BlockFace.NORTH, name -> InfoUtil.getCustomData(this, name)));
            service.registerCustomData(new DirectionCustomData(BlockFace.SOUTH, name -> InfoUtil.getCustomData(this, name)));
            service.registerCustomData(new DirectionCustomData(BlockFace.EAST, name -> InfoUtil.getCustomData(this, name)));
            service.registerCustomData(new DirectionCustomData(BlockFace.WEST, name -> InfoUtil.getCustomData(this, name)));
        }

        if (Version.v1_10_R1.isNewerOrSameVersion(Version.getCurrent()))
            service.registerCustomData(new BlockStateCustomData(CustomOreGeneratorServiceSupplier.INSTANCE, name -> InfoUtil.getCustomData(this, name)));

        if (Version.v1_12_R1.isOlderOrSameVersion(Version.getCurrent()))
            service.registerCustomData(new VariantCustomData(name -> InfoUtil.getCustomData(this, name)));

        oreConfigYamlDao.init();
        worldConfigYamlDao.init();

        checkOldStorageType();
    }

    @Override
    public void onEnable() {
        new VersionPicker(CustomOreGeneratorServiceSupplier.INSTANCE, this, Version.getCurrent()).init();

        getCommand("oregen").setExecutor(new OreGenCommand(CustomOreGeneratorServiceSupplier.INSTANCE, this, messages, permissions));

        if (getServer().getPluginManager().getPlugin("ItemMods") != null && Version.v1_13_R1.isNewerOrSameVersion(Version.getCurrent())) {
            CustomOreGeneratorServiceSupplier.INSTANCE.get().registerCustomData(new ItemModsCustomData(name -> InfoUtil.getCustomData(this, name)));
        }

        new Metrics(this);
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
