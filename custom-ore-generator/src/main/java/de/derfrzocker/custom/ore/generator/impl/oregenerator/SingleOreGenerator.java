package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public class SingleOreGenerator extends AbstractSingleOreGenerator {

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final World world, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final Location chunkLocation = new Location(null, x << 4, 0, z << 4);
        final Material material = config.getMaterial();
        final Set<Material> replaceMaterials = config.getReplaceMaterials();

        for (final Location location : locations) {
            final Location blockLocation = chunkLocation.clone().add(location.toVector()); // use toVector to prevent mis matching worlds error

            final Block block = world.getBlockAt(blockLocation);
            if (replaceMaterials.contains(block.getType()))
                block.setType(material);
        }
    }

}
