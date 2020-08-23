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

package de.derfrzocker.custom.ore.generator.impl.v1_16_R2;

import com.mojang.serialization.Codec;
import de.derfrzocker.custom.ore.generator.api.*;
import net.minecraft.server.v1_16_R2.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class ChunkOverrider extends ChunkGenerator {

    private final static Method a;

    static {
        try {
            a = ChunkGenerator.class.getDeclaredMethod("a");
            a.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unexpected Error while get Method");
        }
    }

    @NotNull
    private final org.bukkit.World world;
    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final ChunkGenerator parent;
    @NotNull
    private final WorldHandler_v1_16_R2 worldHandler;

    public ChunkOverrider(@NotNull final org.bukkit.World world, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final ChunkGenerator parent, @NotNull WorldHandler_v1_16_R2 worldHandler) {
        super(null, null);
        Validate.notNull(world, "World cannot be null");
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(parent, "Parent ChunkGenerator can not be null");
        Validate.notNull(worldHandler, "WorldHandler can not be null");

        this.world = world;
        this.serviceSupplier = serviceSupplier;
        this.parent = parent;
        this.worldHandler = worldHandler;
    }

    @Override
    public void addDecorations(RegionLimitedWorldAccess regionLimitedWorldAccess, StructureManager structuremanager) {
        parent.addDecorations(regionLimitedWorldAccess, structuremanager);

        final Set<Biome> biomes = getBiomes(regionLimitedWorldAccess);

        final CustomOreGeneratorService service = serviceSupplier.get();

        final WorldConfig worldConfig;

        {
            final Optional<WorldConfig> optional = service.getWorldConfig(world.getName());

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
    protected Codec<? extends ChunkGenerator> a() {
        try {
            return (Codec<? extends ChunkGenerator>) a.invoke(parent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unexpected Error while invoke method getCarvingBiome", e);
        }
    }

    @Override
    public void buildBase(RegionLimitedWorldAccess regionLimitedWorldAccess, IChunkAccess iChunkAccess) {
        parent.buildBase(regionLimitedWorldAccess, iChunkAccess);
    }

    @Override
    public void buildNoise(GeneratorAccess generatorAccess, StructureManager structureManager, IChunkAccess iChunkAccess) {
        parent.buildNoise(generatorAccess, structureManager, iChunkAccess);
    }

    @Override
    public int getBaseHeight(int i, int i1, HeightMap.Type type) {
        return parent.getBaseHeight(i, i1, type);
    }

    @Override
    public IBlockAccess a(int i, int i1) {
        return parent.a(i, i1);
    }

    @Nullable
    @Override
    public BlockPosition findNearestMapFeature(WorldServer worldserver, StructureGenerator<?> structuregenerator, BlockPosition blockposition, int i, boolean flag) {
        return parent.findNearestMapFeature(worldserver, structuregenerator, blockposition, i, flag);
    }

    @Override
    public boolean a(ChunkCoordIntPair chunkcoordintpair) {
        return parent.a(chunkcoordintpair);
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
    public int getGenerationDepth() {
        return parent.getGenerationDepth();
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Override
    public int getSpawnHeight() {
        return parent.getSpawnHeight();
    }

    @Override
    public List<BiomeSettingsMobs.c> getMobsFor(BiomeBase biomebase, StructureManager structuremanager, EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        return parent.getMobsFor(biomebase, structuremanager, enumcreaturetype, blockposition);
    }

    @Override
    public StructureSettings getSettings() {
        return parent.getSettings();
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionlimitedworldaccess) {
        parent.addMobs(regionlimitedworldaccess);
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return parent.getWorldChunkManager();
    }


    @Override
    public void doCarving(long i, BiomeManager biomemanager, IChunkAccess ichunkaccess, WorldGenStage.Features worldgenstage_features) {
        parent.doCarving(i, biomemanager, ichunkaccess, worldgenstage_features);
    }

    @Override
    public void createBiomes(IRegistry<BiomeBase> iregistry, IChunkAccess ichunkaccess) {
        parent.createBiomes(iregistry, ichunkaccess);
    }

    @Override
    public void createStructures(IRegistryCustom iregistrycustom, StructureManager structuremanager, IChunkAccess ichunkaccess, DefinedStructureManager definedstructuremanager, long i) {
        parent.createStructures(iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i);
    }

    @Override
    public void storeStructures(GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager, IChunkAccess ichunkaccess) {
        parent.storeStructures(generatoraccessseed, structuremanager, ichunkaccess);
    }

    private Set<Biome> getBiomes(final RegionLimitedWorldAccess access) {
        final Set<Biome> set = new HashSet<>();
        final IRegistryWritable<BiomeBase> registry = access.r().b(IRegistry.ay);

        final int x = access.a() << 4;
        final int z = access.b() << 4;

        for (int x2 = x; x2 < x + 16; x2++)
            for (int z2 = z; z2 < z + 16; z2++) {
                final BiomeBase base = access.getBiome(new BlockPosition(x2, 60, z2));
                try {
                    set.add(Biome.valueOf(registry.getKey(base).getKey().toUpperCase()));
                } catch (Exception ignored) {
                    System.out.println("Biome not Found"); //TODO test and remove
                }
            }

        return set;
    }

    private void generate(final OreConfig oreConfig, final RegionLimitedWorldAccess access, final Biome biome, final CustomOreGeneratorService service) {
        final Optional<OreGenerator> optionalOreGenerator = service.getOreGenerator(oreConfig.getOreGenerator());
        final Optional<BlockSelector> optionalBlockSelector = service.getBlockSelector(oreConfig.getBlockSelector());
        final IRegistryWritable<BiomeBase> registry = access.r().b(IRegistry.ay);

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
        final BiomeBase biomeBase = registry.get(new MinecraftKey(biome.name().toLowerCase()));
        final BlockPosition chunkPosition = new BlockPosition(access.a() << 4, 0, access.b() << 4);
        final Set<org.bukkit.Material> replaceMaterials = oreConfig.getReplaceMaterials();
        Set<org.bukkit.Material> selectMaterials = oreConfig.getSelectMaterials();
        if (selectMaterials.isEmpty())
            selectMaterials = replaceMaterials;

        final Set<Block> selectBlocks = new HashSet<>();

        selectMaterials.forEach(material -> selectBlocks.add(CraftMagicNumbers.getBlock(material)));
        locations.stream().filter(location -> checkBlockAndBiome(access, chunkPosition, location, biomeBase, selectBlocks))
                .forEach(biomeLocations::add);

        oreGenerator.generate(oreConfig, new GeneratorAccessOverrider(access, oreConfig), access.a(), access.b(), random, biome, biomeLocations);
    }

    private boolean checkBlockAndBiome(final RegionLimitedWorldAccess access, final BlockPosition chunkPosition, final Location location, final BiomeBase biomeBase, final Set<Block> blocks) {
        final BlockPosition blockPosition = chunkPosition.b(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        final boolean isBiome = access.getBiome(blockPosition) == biomeBase;

        if (!isBiome)
            return false;

        return blocks.contains(access.getType(blockPosition).getBlock());
    }

}
