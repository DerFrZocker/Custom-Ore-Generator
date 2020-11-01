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

package de.derfrzocker.custom.ore.generator.impl.v1_13_R1;

import de.derfrzocker.custom.ore.generator.api.*;
import net.minecraft.server.v1_13_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongSet;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class ChunkOverrieder<C extends GeneratorSettings> implements ChunkGenerator<C> {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final ChunkGenerator<C> parent;

    public ChunkOverrieder(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final ChunkGenerator<C> parent) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(parent, "Parent ChunkGenerator can not be null");

        this.serviceSupplier = serviceSupplier;
        this.parent = parent;
    }

    @Override
    public void createChunk(IChunkAccess iChunkAccess) {
        parent.createChunk(iChunkAccess);
    }

    @Override
    public void addFeatures(RegionLimitedWorldAccess regionLimitedWorldAccess, WorldGenStage.Features features) {
        parent.addFeatures(regionLimitedWorldAccess, features);
    }

    @Override
    public void addDecorations(final RegionLimitedWorldAccess regionLimitedWorldAccess) {
        parent.addDecorations(regionLimitedWorldAccess);

        final Set<Biome> biomes = getBiomes(regionLimitedWorldAccess);

        final CustomOreGeneratorService service = serviceSupplier.get();

        final WorldConfig worldConfig;

        {
            final Optional<WorldConfig> optional = service.getWorldConfig(parent.getWorld().getWorld().getName());

            if (!optional.isPresent())
                return;

            worldConfig = optional.get();
        }

        biomes.forEach(biome -> {
            final List<OreConfig> oreConfigs = Arrays.asList(worldConfig.getOreConfigs().stream().filter(oreConfig -> oreConfig.getBiomes().contains(biome) || oreConfig.shouldGeneratedAll()).filter(OreConfig::isActivated).toArray(OreConfig[]::new));

            oreConfigs.forEach(oreConfig -> generate(oreConfig, regionLimitedWorldAccess, biome, service));
        });
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionLimitedWorldAccess) {
        parent.addMobs(regionLimitedWorldAccess);
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumCreatureType, BlockPosition blockPosition) {
        return parent.getMobsFor(enumCreatureType, blockPosition);
    }

    @Nullable
    @Override
    public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockPosition, int i) {
        return parent.findNearestMapFeature(world, s, blockPosition, i);
    }

    @Override
    public C getSettings() {
        return parent.getSettings();
    }

    @Override
    public int a(World world, boolean b, boolean b1) {
        return parent.a(world, b, b1);
    }

    @Override
    public boolean canSpawnStructure(BiomeBase biomeBase, StructureGenerator<? extends WorldGenFeatureConfiguration> structureGenerator) {
        return parent.canSpawnStructure(biomeBase, structureGenerator);
    }

    @Nullable
    @Override
    public WorldGenFeatureConfiguration getFeatureConfiguration(BiomeBase biomeBase, StructureGenerator<? extends WorldGenFeatureConfiguration> structureGenerator) {
        return parent.getFeatureConfiguration(biomeBase, structureGenerator);
    }

    @Override
    public Long2ObjectMap<StructureStart> getStructureStartCache(StructureGenerator<? extends WorldGenFeatureConfiguration> structureGenerator) {
        return parent.getStructureStartCache(structureGenerator);
    }

    @Override
    public Long2ObjectMap<LongSet> getStructureCache(StructureGenerator<? extends WorldGenFeatureConfiguration> structureGenerator) {
        return parent.getStructureCache(structureGenerator);
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return parent.getWorldChunkManager();
    }

    @Override
    public long getSeed() {
        return parent.getSeed();
    }

    @Override
    public int getSpawnHeight() {
        return parent.getSpawnHeight();
    }

    @Override
    public int e() {
        return parent.e();
    }

    @Override
    public World getWorld() {
        return parent.getWorld();
    }

    private Set<Biome> getBiomes(final RegionLimitedWorldAccess access) {
        final Set<Biome> set = new HashSet<>();

        final int x = access.a() << 4;
        final int z = access.b() << 4;

        for (int x2 = x; x2 < x + 16; x2++)
            for (int z2 = z; z2 < z + 16; z2++) {
                final BiomeBase base = access.getBiome(new BlockPosition(x2, 60, z2));
                try {
                    set.add(Biome.valueOf(BiomeBase.REGISTRY_ID.b(base).getKey().toUpperCase()));
                } catch (Exception ignored) {
                }
            }

        return set;
    }

    private void generate(final OreConfig oreConfig, final RegionLimitedWorldAccess access, final Biome biome, final CustomOreGeneratorService service) {
        final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(oreConfig.getOreGenerator());
        final Optional<BlockSelector> optionalBlockSelector = service.getBlockSelector(oreConfig.getBlockSelector());

        if (!optionalOreGenerator.isPresent())
            return;

        if (!optionalBlockSelector.isPresent())
            return;

        final OreGenerator oreGenerator = optionalOreGenerator.get();
        final BlockSelector blockSelector = optionalBlockSelector.get();
        final Random random = service.createRandom(access.getSeed() + oreConfig.getMaterial().toString().hashCode(), access.a(), access.b());
        final BlockPosition blockPosition = new BlockPosition(access.a() << 4, 0, access.b() << 4);

        final Set<Location> locations = blockSelector.selectBlocks((x, z) -> access.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, blockPosition.a(x, 0, z)).getY(), oreConfig, random);
        final Set<Location> biomeLocations = new HashSet<>();
        final BiomeBase biomeBase = BiomeBase.REGISTRY_ID.get(new MinecraftKey(biome.name().toLowerCase()));
        final BlockPosition chunkPosition = new BlockPosition(access.a() << 4, 0, access.b() << 4);
        Set<org.bukkit.Material> selectMaterials = oreConfig.getSelectMaterials();
        if (selectMaterials.isEmpty())
            selectMaterials = oreConfig.getReplaceMaterials();

        final Set<Block> selectBlocks = new HashSet<>();
        selectMaterials.forEach(material -> selectBlocks.add(CraftMagicNumbers.getBlock(material)));
        locations.stream().filter(location -> checkBlockAndBiome(access, chunkPosition, location, biomeBase, selectBlocks))
                .forEach(biomeLocations::add);

        oreGenerator.generate(oreConfig, new GeneratorAccessOverrider(access, oreConfig), access.a(), access.b(), random, biome, biomeLocations);
    }

    private boolean checkBlockAndBiome(final RegionLimitedWorldAccess access, final BlockPosition chunkPosition, final Location location, final BiomeBase biomeBase, final Set<Block> blocks) {
        final BlockPosition blockPosition = chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        final boolean isBiome = access.getBiome(blockPosition) == biomeBase;

        if (!isBiome)
            return false;

        return blocks.contains(access.getType(blockPosition).getBlock());
    }

}
