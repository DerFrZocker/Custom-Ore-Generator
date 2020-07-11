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

package de.derfrzocker.custom.ore.generator.impl.dao;

import de.derfrzocker.custom.ore.generator.api.WorldConfig;
import de.derfrzocker.custom.ore.generator.impl.WorldConfigYamlImpl;
import de.derfrzocker.spigot.utils.Config;
import de.derfrzocker.spigot.utils.ReloadAble;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class LazyWorldConfigCache implements ReloadAble {

    private final Object look = new Object();
    @NotNull
    private final File file;

    @Nullable
    private WorldConfig worldConfig;

    public LazyWorldConfigCache(@NotNull final File file) {
        Validate.notNull(file, "File can not be null");
        Validate.isTrue(file.getName().endsWith(".yml"), "File " + file + " has not valid extension, must be '.yml'");

        if (file.exists())
            Validate.isTrue(file.isFile(), "File " + file + " is not a File?");

        this.file = file;
    }

    /**
     * When this LazyWorldConfigCache has a WorldConfig loaded, it will save it to disk.
     * Nothing will happen, if this LazyWorldConfigCache does not holds a WorldConfig
     */
    public void save() {
        if (worldConfig == null)
            return;

        final WorldConfig worldConfig;

        if (!(this.worldConfig instanceof ConfigurationSerializable)) {
            worldConfig = new WorldConfigYamlImpl(this.worldConfig.getName());
            WorldConfigYamlImpl.copyData(this.worldConfig, (WorldConfigYamlImpl) worldConfig);
        } else
            worldConfig = this.worldConfig;

        final Config config = new Config(file);

        config.set("value", worldConfig);

        try {
            config.options().header("Only edit this file if you 10000% sure you know what you are doing. \nYou can set everything via the plugin commands, no need to edit this file manully.").copyHeader(true);
            config.save(file);
        } catch (final IOException e) {
            throw new RuntimeException("Unexpected error while saving WorldOreConfig " + worldConfig.getName() + " to disk!", e);
        }
    }

    /**
     * When this LazyWorldConfigCache has a WorldConfig loaded, it will return it.
     * If not, then it will try to load if from a file and set it to the cache.
     *
     * @return the cached or fresh loaded WorldConfig
     * @throws RuntimeException if no WorldConfig is Cached and the file does not exists
     * @throws RuntimeException if no WorldConfig is Cached and the file does not contains a WorldConfig under the key "value"
     * @throws RuntimeException if no WorldConfig is Cached and the file name and WorldConfig name does not match
     */
    @NotNull
    public WorldConfig getWorldConfig() {
        if (worldConfig != null)
            return worldConfig;

        synchronized (look) {
            if (worldConfig != null)
                return worldConfig;

            if (!file.exists())
                throw new RuntimeException("File " + file + " does not exists, can not load WorldConfig from none existing file");

            final Config config = new Config(file);

            final Object object = config.get("value");

            if (!(object instanceof WorldConfig))
                throw new RuntimeException("File " + file + " does not have a WorldConfig under the key 'value'");

            final WorldConfig worldConfig = (WorldConfig) object;

            if (!worldConfig.getName().equals(file.getName().substring(0, file.getName().length() - 4)))
                throw new RuntimeException("File name " + file.getName() + " and WorldConfig name " + worldConfig.getName() + " does not match");

            this.worldConfig = worldConfig;
        }

        return worldConfig;
    }

    /**
     * @param worldConfig to set
     * @throws IllegalArgumentException if worldConfig is null
     * @throws RuntimeException         if the file name and the worldConfig name does't match
     */
    public void setWorldConfig(@NotNull final WorldConfig worldConfig) {
        Validate.notNull(worldConfig, "worldConfig can not be null");

        if (!worldConfig.getName().equals(file.getName().substring(0, file.getName().length() - 4)))
            throw new RuntimeException("File name " + file.getName() + " and WorldConfig name " + worldConfig.getName() + " does not match");

        this.worldConfig = worldConfig;
    }

    @Override
    public void reload() {
        worldConfig = null;
    }


}
