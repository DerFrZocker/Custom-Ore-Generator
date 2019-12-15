/*
 * MIT License
 *
 * Copyright (c) 2019 DerFrZocker
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

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.dao.OreConfigDao;
import de.derfrzocker.spigot.utils.ReloadAble;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class OreConfigYamlDao implements OreConfigDao, ReloadAble {

    private final Map<String, LazyOreConfigCache> lazyOreConfigCacheMap = new HashMap<>();
    @NotNull
    private final File directory;

    public OreConfigYamlDao(@NotNull final File directory) {
        Validate.notNull(directory, "Directory can not be null");
        if (directory.exists())
            Validate.isTrue(directory.isDirectory(), "Directory is not a directory?");

        this.directory = directory;

        RELOAD_ABLES.add(this);
    }

    public void init() {
        reload();
    }

    @NotNull
    @Override
    public Optional<OreConfig> get(@NotNull final String key) {
        Validate.notNull(key, "Key can not be null");
        Validate.notEmpty(key, "Key can not be empty");

        final LazyOreConfigCache lazyOreConfigCache = lazyOreConfigCacheMap.get(key);

        if (lazyOreConfigCache != null)
            return Optional.of(lazyOreConfigCache.getOreConfig());

        final File file = new File(directory, key + ".yml");

        if (!file.exists() || !file.isFile())
            return Optional.empty();

        final LazyOreConfigCache lazyOreConfigCache1 = new LazyOreConfigCache(file);

        lazyOreConfigCacheMap.put(key, lazyOreConfigCache1);

        return Optional.of(lazyOreConfigCache1.getOreConfig());
    }

    @Override
    public void remove(@NotNull final OreConfig value) {
        Validate.notNull(value, "OreConfig can not be null");

        lazyOreConfigCacheMap.remove(value.getName());
        new File(directory, value.getName() + ".yml").delete();
    }

    @Override
    public void save(@NotNull final OreConfig value) {
        Validate.notNull(value, "OreConfig can not be null");

        LazyOreConfigCache lazyOreConfigCache = lazyOreConfigCacheMap.get(value.getName());

        if (lazyOreConfigCache == null) {
            lazyOreConfigCache = new LazyOreConfigCache(new File(directory, value.getName() + ".yml"));
            lazyOreConfigCacheMap.put(value.getName(), lazyOreConfigCache);
        }

        lazyOreConfigCache.setOreConfig(value);
        lazyOreConfigCache.save();
    }

    @Override
    public Set<OreConfig> getAll() {
        final Set<OreConfig> oreConfigs = new LinkedHashSet<>();

        lazyOreConfigCacheMap.forEach((name, lazyOreConfigCache) -> oreConfigs.add(lazyOreConfigCache.getOreConfig()));

        return oreConfigs;
    }

    @Override
    public void reload() {
        lazyOreConfigCacheMap.clear();

        final File[] files = directory.listFiles();

        if (files == null)
            return;

        for (final File file : files) {
            if (!file.isFile())
                continue;

            if (!file.getName().endsWith(".yml"))
                continue;

            lazyOreConfigCacheMap.put(file.getName().substring(0, file.getName().length() - 4), new LazyOreConfigCache(file));
        }
    }

}
