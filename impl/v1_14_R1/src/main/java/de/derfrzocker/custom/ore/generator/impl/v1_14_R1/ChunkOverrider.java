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

package de.derfrzocker.custom.ore.generator.impl.v1_14_R1;

import de.derfrzocker.custom.ore.generator.api.*;
import net.minecraft.server.v1_14_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class ChunkOverrider<C extends GeneratorSettingsDefault> extends ChunkGenerator<C> {

    private final static Method getCarvingBiome;
    private final static Method getDecoratingBiome;

    static {
        try {
            getCarvingBiome = ChunkGenerator.class.getDeclaredMethod("getCarvingBiome", IChunkAccess.class);
            getCarvingBiome.setAccessible(true);

            getDecoratingBiome = ChunkGenerator.class.getDeclaredMethod("getDecoratingBiome", RegionLimitedWorldAccess.class, BlockPosition.class);
            getDecoratingBiome.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unexpected Error while get Method");
        }
    }

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final ChunkGenerator<C> parent;
    @NotNull
    private final WorldHandler_v1_14_R1 worldHandler;

    public ChunkOverrider(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final ChunkGenerator<C> parent, @NotNull WorldHandler_v1_14_R1 worldHandler) {
        super(DummyGeneratorAccess.INSTANCE, null, null);
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(parent, "Parent ChunkGenerator can not be null");
        Validate.notNull(worldHandler, "WorldHandler can not be null");

        this.serviceSupplier = serviceSupplier;
        this.parent = parent;
        this.worldHandler = worldHandler;
    }

    @Override
    public void createBiomes(IChunkAccess ichunkaccess) {
        parent.createBiomes(ichunkaccess);
    }

    @Override
    protected BiomeBase getCarvingBiome(IChunkAccess ichunkaccess) {
        try {
            return (BiomeBase) getCarvingBiome.invoke(parent, ichunkaccess);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unexpected Error while invoke method getCarvingBiome", e);
        }
    }

    @Override
    protected BiomeBase getDecoratingBiome(RegionLimitedWorldAccess regionlimitedworldaccess, BlockPosition blockposition) {
        try {
            return (BiomeBase) getDecoratingBiome.invoke(parent, regionlimitedworldaccess, blockposition);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unexpected Error while invoke method getDecoratingBiome", e);
        }
    }

    @Override
    public void doCarving(IChunkAccess ichunkaccess, WorldGenStage.Features worldgenstage_features) {
        parent.doCarving(ichunkaccess, worldgenstage_features);
    }

    @Nullable
    @Override
    public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockPosition, int i, boolean b) {
        return parent.findNearestMapFeature(world, s, blockPosition, i, b);
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
    public void buildBase(IChunkAccess iChunkAccess) {
        parent.buildBase(iChunkAccess);
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionLimitedWorldAccess) {
        parent.addMobs(regionLimitedWorldAccess);
    }

    @Override
    public C getSettings() {
        return parent.getSettings();
    }

    @Override
    public int getSpawnHeight() {
        return parent.getSpawnHeight();
    }

    @Override
    public void doMobSpawning(WorldServer worldserver, boolean flag, boolean flag1) {
        parent.doMobSpawning(worldserver, flag, flag1);
    }

    @Override
    public boolean canSpawnStructure(BiomeBase biomeBase, StructureGenerator<? extends WorldGenFeatureConfiguration> structureGenerator) {
        return parent.canSpawnStructure(biomeBase, structureGenerator);
    }

    @Nullable
    @Override
    public <C1 extends WorldGenFeatureConfiguration> C1 getFeatureConfiguration(BiomeBase biomebase, StructureGenerator<C1> structuregenerator) {
        return parent.getFeatureConfiguration(biomebase, structuregenerator);
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
    public int getGenerationDepth() {
        return parent.getGenerationDepth();
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumCreatureType, BlockPosition blockPosition) {
        return parent.getMobsFor(enumCreatureType, blockPosition);
    }

    @Override
    public void createStructures(IChunkAccess ichunkaccess, ChunkGenerator<?> chunkgenerator, DefinedStructureManager definedstructuremanager) {
        parent.createStructures(ichunkaccess, chunkgenerator, definedstructuremanager);
    }

    @Override
    public void storeStructures(GeneratorAccess generatoraccess, IChunkAccess ichunkaccess) {
        parent.storeStructures(generatoraccess, ichunkaccess);
    }

    @Override
    public void buildNoise(GeneratorAccess generatorAccess, IChunkAccess iChunkAccess) {
        parent.buildNoise(generatorAccess, iChunkAccess);
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Override
    public int getBaseHeight(int i, int i1, HeightMap.Type type) {
        return parent.getBaseHeight(i, i1, type);
    }

    @Override
    public int b(int i, int j, HeightMap.Type heightmap_type) {
        return parent.b(i, j, heightmap_type);
    }

    @Override
    public int c(int i, int j, HeightMap.Type heightmap_type) {
        return parent.c(i, j, heightmap_type);
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
                    set.add(Biome.valueOf(IRegistry.BIOME.getKey(base).getKey().toUpperCase()));
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

        final Set<Location> locations = blockSelector.selectBlocks((x, z) -> access.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, blockPosition.b(x, 0, z)).getY(), oreConfig, random);
        final Set<Location> biomeLocations = new HashSet<>();
        final BiomeBase biomeBase = IRegistry.BIOME.get(new MinecraftKey(biome.name().toLowerCase()));
        final BlockPosition chunkPosition = new BlockPosition(access.a() << 4, 0, access.b() << 4);
        final Set<org.bukkit.Material> replaceMaterials = oreConfig.getReplaceMaterials();
        Set<org.bukkit.Material> selectMaterials = oreConfig.getSelectMaterials();
        if (selectMaterials.isEmpty())
            selectMaterials = replaceMaterials;

        final Set<Block> replaceBlocks = new HashSet<>();
        final Set<Block> selectBlocks = new HashSet<>();

        replaceMaterials.forEach(material -> replaceBlocks.add(CraftMagicNumbers.getBlock(material)));
        selectMaterials.forEach(material -> selectBlocks.add(CraftMagicNumbers.getBlock(material)));
        locations.stream().filter(location -> checkBlockAndBiome(access, chunkPosition, location, biomeBase, selectBlocks))
                .forEach(biomeLocations::add);

        worldHandler.add(replaceBlocks);

        oreGenerator.generate(oreConfig, new GeneratorAccessOverrider(access, oreConfig), access.a(), access.b(), random, biome, biomeLocations);
        worldHandler.remove();
    }

    private boolean checkBlockAndBiome(final RegionLimitedWorldAccess access, final BlockPosition chunkPosition, final Location location, final BiomeBase biomeBase, final Set<Block> blocks) {
        final BlockPosition blockPosition = chunkPosition.b(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        final boolean isBiome = access.getBiome(blockPosition) == biomeBase;

        if (!isBiome)
            return false;

        return blocks.contains(access.getType(blockPosition).getBlock());
    }

}
