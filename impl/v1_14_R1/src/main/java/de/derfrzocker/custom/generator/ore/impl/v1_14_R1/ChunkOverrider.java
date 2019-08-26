package de.derfrzocker.custom.generator.ore.impl.v1_14_R1;

import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.api.*;
import de.derfrzocker.custom.generator.ore.util.CustomOreGeneratorUtil;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.block.Biome;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ChunkOverrider<C extends GeneratorSettingsDefault> extends ChunkGenerator<C> {

    final ChunkGenerator<C> parent;

    final static Method getCarvingBiome;
    final static Method getDecoratingBiome;

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

    public ChunkOverrider(ChunkGenerator<C> parent) {
        super(DummyGeneratorAccess.INSTANCE, null, null);
        this.parent = parent;
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
    public void addDecorations(RegionLimitedWorldAccess regionLimitedWorldAccess) {
        Set<Biome> biomes = getBiomes(regionLimitedWorldAccess);

        CustomOreGeneratorService service = CustomOreGenerator.getService();

        WorldConfig worldConfig;

        {
            Optional<WorldConfig> optional = service.getWorldConfig(parent.getWorld().getWorld().getName());

            if (!optional.isPresent())
                return;

            worldConfig = optional.get();
        }

        biomes.forEach(biome -> {
            Set<OreConfig> oreConfigs = new HashSet<>(worldConfig.getBiomeConfig(biome).map(BiomeConfig::getOreConfigs).orElseGet(HashSet::new));

            worldConfig.getOreConfigs().stream().filter(value -> oreConfigs.stream().noneMatch(value2 -> value2.getMaterial() == value.getMaterial())).forEach(oreConfigs::add);

            oreConfigs.forEach(oreConfig -> generate(oreConfig, regionLimitedWorldAccess, biome));
        });

        parent.addDecorations(regionLimitedWorldAccess);
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

    private Set<Biome> getBiomes(RegionLimitedWorldAccess access) {
        Set<Biome> set = new HashSet<>();

        int x = access.a() << 4;
        int z = access.b() << 4;

        for (int x2 = x; x2 < x + 16; x2++)
            for (int z2 = z; z2 < z + 16; z2++) {
                BiomeBase base = access.getBiome(new BlockPosition(x2, 60, z2));
                try {
                    set.add(Biome.valueOf(IRegistry.BIOME.getKey(base).getKey().toUpperCase()));
                } catch (Exception ignored) {
                }
            }

        return set;
    }

    private void generate(OreConfig oreConfig, RegionLimitedWorldAccess access, Biome biome) {
        CustomOreGeneratorService service = CustomOreGenerator.getService();

        Optional<OreGenerator> optional = service.getOreGenerator(oreConfig.getOreGenerator());

        if (!optional.isPresent())
            return;

        OreGenerator oreGenerator = optional.get();

        if (oreGenerator instanceof MinableGenerator_v1_14_R1) {
            ((MinableGenerator_v1_14_R1) oreGenerator).generate(oreConfig, parent.getWorld().getWorld(), access, CustomOreGeneratorUtil.getRandom(parent.getSeed() + oreConfig.getMaterial().toString().hashCode(), access.a(), access.b()), biome);
            return;
        }

        oreGenerator.generate(oreConfig, parent.getWorld().getWorld(), access.a(), access.b(), CustomOreGeneratorUtil.getRandom(parent.getSeed() + oreConfig.getMaterial().toString().hashCode(), access.a(), access.b()), biome);
    }
}
