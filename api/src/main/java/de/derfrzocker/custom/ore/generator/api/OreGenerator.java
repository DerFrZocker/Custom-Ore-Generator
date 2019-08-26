package de.derfrzocker.custom.ore.generator.api;

import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Random;
import java.util.Set;

public interface OreGenerator {

    void generate(OreConfig config, World world, int x, int z, Random random, Biome biome);

    Set<OreSetting> getNeededOreSettings();

    String getName();

}
