package de.derfrzocker.custom.ore.generator.factory;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class OreConfigBuilder {
    @Nullable
    private String name;
    @NotNull
    private Material material;
    @Nullable
    private OreGenerator oreGenerator;
    @Nullable
    private BlockSelector blockSelector;
    @Nullable
    private Set<Material> replaceMaterial;

    public static OreConfigBuilder newBuilder(){
        return new OreConfigBuilder();
    }

    @NotNull
    public OreConfigBuilder name(@Nullable final String name){
        this.name = name;
        return this;
    }

    @NotNull
    public OreConfigBuilder material(@Nullable final Material material){
        this.material = material;
        return this;
    }

}
