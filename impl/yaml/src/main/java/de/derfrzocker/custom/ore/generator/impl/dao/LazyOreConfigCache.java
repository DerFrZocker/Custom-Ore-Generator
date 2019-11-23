package de.derfrzocker.custom.ore.generator.impl.dao;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.OreConfigYamlImpl;
import de.derfrzocker.spigot.utils.Config;
import de.derfrzocker.spigot.utils.ReloadAble;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class LazyOreConfigCache implements ReloadAble {

    private final Object look = new Object();
    @NotNull
    private final File file;

    @Nullable
    private OreConfig oreConfig;

    public LazyOreConfigCache(@NotNull final File file) {
        Validate.notNull(file, "File can not be null");
        Validate.isTrue(file.getName().endsWith(".yml"), "File " + file + " has not valid extension, must be '.yml'");

        if (file.exists())
            Validate.isTrue(file.isFile(), "File " + file + " is not a File?");

        this.file = file;
    }

    /**
     * @param oreConfig to set
     * @throws IllegalArgumentException if oreConfig is null
     * @throws RuntimeException         if the file name and the oreConfig name does't match
     */
    public void setOreConfig(@NotNull final OreConfig oreConfig) {
        Validate.notNull(oreConfig, "OreConfig can not be null");

        if (!oreConfig.getName().equals(file.getName().substring(0, file.getName().length() - 4)))
            throw new RuntimeException("File name " + file.getName() + " and OreConfig name " + oreConfig.getName() + " does not match");

        this.oreConfig = oreConfig;
    }

    /**
     * When this LazyOreConfigCache has a OreConfig loaded, it will save it to disk.
     * Nothing will happen, if this LazyOreConfigCache does not holds a OreConfig
     */
    public void save() {
        if (this.oreConfig == null)
            return;

        final OreConfig oreConfig;

        if (!(this.oreConfig instanceof ConfigurationSerializable)) {
            oreConfig = new OreConfigYamlImpl(this.oreConfig.getName(), this.oreConfig.getMaterial(), this.oreConfig.getOreGenerator(), this.oreConfig.getBlockSelector());
            OreConfigYamlImpl.copyData(this.oreConfig, (OreConfigYamlImpl) oreConfig);
        } else
            oreConfig = this.oreConfig;


        final Config config = new Config(file);

        config.set("value", oreConfig);

        try {
            config.save(file);
        } catch (final IOException e) {
            throw new RuntimeException("Unexpected error while saving OreConfig " + oreConfig.getName() + " to disk!", e);
        }
    }

    /**
     * When this LazyOreConfigCache has a OreConfig loaded, it will return it.
     * If not, then it will try to load if from a file and set it to the cache.
     *
     * @return the cached or fresh loaded OreConfig
     * @throws RuntimeException if no OreConfig is Cached and the file does not exists
     * @throws RuntimeException if no OreConfig is Cached and the file does not contains a OreConfig under the key "value"
     * @throws RuntimeException if no OreConfig is Cached and the file name and OreConfig name does not match
     */
    @NotNull
    public OreConfig getOreConfig() {
        if (oreConfig != null)
            return oreConfig;

        synchronized (look) {
            if (oreConfig != null)
                return oreConfig;

            if (!file.exists())
                throw new RuntimeException("File " + file + " does not exists, can not load OreConfig from none existing file");

            final Config config = new Config(file);

            final Object object = config.get("value");

            if (!(object instanceof OreConfig))
                throw new RuntimeException("File " + file + " does not have a OreConfig under the key 'value'");

            final OreConfig oreConfig = (OreConfig) object;

            if (!oreConfig.getName().equals(file.getName().substring(0, file.getName().length() - 4)))
                throw new RuntimeException("File name " + file.getName() + " and OreConfig name " + oreConfig.getName() + " does not match");

            this.oreConfig = oreConfig;
        }

        return oreConfig;
    }

    @Override
    public void reload() {
        oreConfig = null;
    }

}
