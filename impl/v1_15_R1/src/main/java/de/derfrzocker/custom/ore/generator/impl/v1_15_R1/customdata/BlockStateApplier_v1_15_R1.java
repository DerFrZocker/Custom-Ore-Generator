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

package de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.customdata.AbstractBlockStateCustomData;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class BlockStateApplier_v1_15_R1 implements AbstractBlockStateCustomData.BlockStateApplier {

    @NotNull
    private final Map<String, Cache> caches = new ConcurrentHashMap<>(); //TODO remove possible memory leak
    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final CustomData customData;

    public BlockStateApplier_v1_15_R1(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final CustomData customData) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(customData, "CustomData can not be null");

        this.serviceSupplier = serviceSupplier;
        this.customData = customData;
    }

    @Override
    public void apply(@NotNull final OreConfig oreConfig, @NotNull final Object location, @NotNull final Object blockAccess) {
        final BlockPosition blockPosition = (BlockPosition) location;
        final GeneratorAccess generatorAccess = (GeneratorAccess) blockAccess;
        IBlockData iBlockData = generatorAccess.getType(blockPosition);

        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent()) {
            serviceSupplier.get().getLogger().warning("No CustomData value found for the CustomData '" + customData.getName() + "' in the ore-config with the name '" + oreConfig.getName() + "'!");
            serviceSupplier.get().getLogger().warning("Ignoring it!");
            return;
        }

        final String customData = (String) objectOptional.get();

        Cache cache = caches.get(oreConfig.getName());
        if (cache == null || cache.getHash() != customData.hashCode() || cache.getMaterial() != oreConfig.getMaterial()) {
            cache = buildCache(iBlockData.getBlock(), oreConfig.getMaterial(), customData);
            caches.put(oreConfig.getName(), cache);
        }

        for (final Pair<?, ?> entry : cache.getAll()) {
            iBlockData = entry.apply(iBlockData);
        }

        generatorAccess.setTypeAndData(blockPosition, iBlockData, 2);
    }


    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return !CraftMagicNumbers.getBlock(oreConfig.getMaterial()).getStates().a().isEmpty();
    }

    @Override
    public boolean isValidCustomData(@NotNull String customData, @NotNull final OreConfig oreConfig) {
        if (!customData.startsWith("[")) {
            return false;
        }

        if (!customData.endsWith("]")) {
            return false;
        }

        // minimum possible BlockState with value is 5 e.g. "[x=y]"
        // This means a string with a length of 4 or less is not valid
        if (customData.length() < 5)
            return false;

        customData = customData.substring(1);
        customData = customData.substring(0, customData.length() - 1);

        final String[] keyValues = customData.split(",");
        final BlockStateList<Block, IBlockData> blockStateList = CraftMagicNumbers.getBlock(oreConfig.getMaterial()).getStates();

        for (final String keyValue : keyValues) {
            final String[] split = keyValue.split("=");

            if (split.length != 2)
                return false;

            final String key = split[0].trim().toLowerCase();
            final String value = split[1].trim().toLowerCase();

            final IBlockState iBlockState = blockStateList.a(key);

            if (iBlockState == null) {
                return false;
            }

            final Optional optional = iBlockState.b(value);

            if (!optional.isPresent()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean hasCustomData(@NotNull final org.bukkit.block.BlockState blockState) {
        final IBlockData iBlockData = ((CraftWorld) blockState.getWorld()).getHandle().getType(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()));

        return iBlockData.getBlock().getStates().d().size() != 0;
    }

    @NotNull
    @Override
    public String getCustomData(@NotNull final org.bukkit.block.BlockState blockState) {
        final IBlockData iBlockData = ((CraftWorld) blockState.getWorld()).getHandle().getType(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()));

        final StringBuilder stringBuilder = new StringBuilder("[");
        boolean first = true;

        for (final IBlockState iBlockState : iBlockData.getBlock().getStates().d()) {
            if (!first) {
                stringBuilder.append(",");
            } else {
                first = false;
            }

            stringBuilder.append(iBlockState.a());
            stringBuilder.append(",");
            stringBuilder.append(iBlockState.a(iBlockData.get(iBlockState)));
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    @NotNull
    private Cache buildCache(@NotNull final Block block, @NotNull final Material material, @NotNull String input) {
        final Cache cache = new Cache(input.hashCode(), material);

        if (input.startsWith("[")) {
            input = input.substring(1);
        }

        if (input.endsWith("]")) {
            input = input.substring(0, input.length() - 1);
        }

        final String[] keyValues = input.split(",");
        final BlockStateList<Block, IBlockData> blockStateList = block.getStates();

        for (final String keyValue : keyValues) {
            final String[] split = keyValue.split("=");
            final String key = split[0].trim().toLowerCase();
            final String value = split[1].trim().toLowerCase();

            final IBlockState iBlockState = blockStateList.a(key);

            if (iBlockState == null) {
                serviceSupplier.get().getLogger().warning("Found no IBlockState with the name '" + key + "' in the block with the name '" + IRegistry.BLOCK.getKey(block) + "'!");
                serviceSupplier.get().getLogger().warning("Ignoring it!");
                continue;
            }

            final Optional optional = iBlockState.b(value);

            if (optional.isPresent()) {
                cache.add(iBlockState, (Comparable) optional.get());
            } else {
                serviceSupplier.get().getLogger().warning("Found no value with the name '" + value + "' in the IBlockState with the name '" + key + "'!");
                serviceSupplier.get().getLogger().warning("Ignoring it!");
            }
        }

        return cache;
    }

    private static final class Cache {

        private final int hash;
        @NotNull
        private final Material material;
        @NotNull
        private final Set<Pair<?, ?>> values = new HashSet<>();

        private Cache(final int hash, @NotNull final Material material) {
            this.hash = hash;
            this.material = material;
        }

        private <T extends Comparable<T>, V extends T> void add(@NotNull final IBlockState<T> key, @NotNull final V value) {
            this.values.add(new Pair<>(key, value));
        }

        private Set<Pair<?, ?>> getAll() {
            return this.values;
        }

        private int getHash() {
            return this.hash;
        }

        @NotNull
        public Material getMaterial() {
            return this.material;
        }

    }

    private static final class Pair<T extends Comparable<T>, V extends T> {

        @NotNull
        private final IBlockState<T> iBlockState;
        @NotNull
        private final V value;

        private Pair(@NotNull IBlockState<T> iBlockState, @NotNull V value) {
            this.iBlockState = iBlockState;
            this.value = value;
        }

        @NotNull
        private IBlockData apply(@NotNull final IBlockData iBlockData) {
            return iBlockData.set(iBlockState, value);
        }

    }

}
