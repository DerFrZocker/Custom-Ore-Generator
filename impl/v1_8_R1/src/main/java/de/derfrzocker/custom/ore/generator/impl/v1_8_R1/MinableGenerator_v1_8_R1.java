package de.derfrzocker.custom.ore.generator.impl.v1_8_R1;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class MinableGenerator_v1_8_R1 implements OreGenerator {

    public final Predicate<IBlockData> blocks = (value) -> {
        if (value == null) {
            return false;
        } else {
            Block block = value.getBlock();
            return block == Blocks.STONE || block == Blocks.END_STONE || block == Blocks.NETHERRACK;
        }
    };
    private final Set<OreSetting> neededOreSettings = Collections.unmodifiableSet(Sets.newHashSet(OreSettings.VEIN_SIZE));

    @Override
    public void generate(@NotNull final OreConfig config, @NotNull final World world, final int x, final int z, @NotNull final Random random, @NotNull final Biome biome, @NotNull final Set<Location> locations) {
        final int veinSize = config.getValue(OreSettings.VEIN_SIZE).orElse(OreSettings.VEIN_SIZE.getSaveValue());

        final CraftWorld craftWorld = (CraftWorld) world;

        final IBlockData blockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();

        final WorldGenMinable generator = new WorldGenMinable(blockData, veinSize, blocks);
        final BlockPosition chunkPosition = new BlockPosition(x << 4, 0, z << 4);

        for (final Location location : locations) {
            craftWorld.getHandle().captureTreeGeneration = true;
            craftWorld.getHandle().captureBlockStates = true;

            generator.generate(craftWorld.getHandle(), random, chunkPosition.a(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

            craftWorld.getHandle().captureTreeGeneration = false;
            craftWorld.getHandle().captureBlockStates = false;

            for (org.bukkit.block.BlockState blockState : craftWorld.getHandle().capturedBlockStates) {
                final BlockPosition blockPosition = new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ());

                if (craftWorld.getHandle().setTypeAndData(blockPosition, blockData, 2)) {
                    config.getCustomData().forEach((customData, object) -> customData.getCustomDataApplier().apply(config, blockPosition, craftWorld.getHandle()));
                }
            }

            craftWorld.getHandle().capturedBlockStates.clear();
        }

    }

    @NotNull
    @Override
    public Set<OreSetting> getNeededOreSettings() {
        return neededOreSettings;
    }

    @NotNull
    @Override
    public String getName() {
        return "VANILLA_MINABLE_GENERATOR";
    }

}
