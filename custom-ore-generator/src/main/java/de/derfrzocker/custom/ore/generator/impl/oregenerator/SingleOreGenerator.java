package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import de.derfrzocker.custom.ore.generator.api.ChunkAccess;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SingleOreGenerator extends AbstractSingleOreGenerator {

    /**
     * The infoFunction gives the name of the OreGenerator as value.
     * The oreSettingInfo gives the name of the OreGenerator and the OreSetting as values.
     *
     * @param infoFunction   function to get the info object of this OreGenerator
     * @param oreSettingInfo biFunction to get the info object of a given OreSetting
     * @throws IllegalArgumentException if one of the arguments are null
     */
    public SingleOreGenerator(@NotNull Function<String, Info> infoFunction, @NotNull BiFunction<String, OreSetting, Info> oreSettingInfo) {
        super(infoFunction, oreSettingInfo);
    }

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final ChunkAccess chunkAccess, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final Location chunkLocation = new Location(null, x << 4, 0, z << 4);
        final Material material = config.getMaterial();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();

        for (final Location location : locations) {
            final int xPosition = chunkLocation.getBlockX() + location.getBlockX();
            final int yPosition = chunkLocation.getBlockY() + location.getBlockY();
            final int zPosition = chunkLocation.getBlockZ() + location.getBlockZ();

            final Material blockMaterial = chunkAccess.getMaterial(xPosition, yPosition, zPosition);
            if (replaceMaterials.contains(blockMaterial))
                chunkAccess.setMaterial(material, xPosition, yPosition, zPosition);
        }
    }

}
