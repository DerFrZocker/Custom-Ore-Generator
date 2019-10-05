package de.derfrzocker.custom.ore.generator.impl.v1_13_R2;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Set;

public interface OreGenerator_v1_13_R2 extends OreGenerator {

    void generate(@NotNull OreConfig config, @NotNull World world, @NotNull GeneratorAccessOverrider access, @NotNull Random random, @NotNull Biome biome, @NotNull Set<Location> locations);

}
