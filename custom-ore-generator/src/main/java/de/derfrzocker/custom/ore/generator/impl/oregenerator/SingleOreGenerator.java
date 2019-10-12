package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public class SingleOreGenerator extends AbstractSingleOreGenerator {

    @Override
    public void generate(@NotNull OreConfig config, @NotNull World world, int x, int z, @NotNull Random random, @NotNull Biome biome, @NotNull Set<Location> locations) {
        final Location chunkLocation = new Location(null, x << 4, 0, z << 4);
        final Material material = config.getMaterial();

        for (final Location location : locations) {
            final Location blockLocation = chunkLocation.clone().add(location.toVector()); // use toVector to prevent mis matching worlds error

            world.getBlockAt(blockLocation).setType(material);
        }
    }

}
