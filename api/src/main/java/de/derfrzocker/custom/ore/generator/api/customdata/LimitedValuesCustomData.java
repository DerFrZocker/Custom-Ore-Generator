package de.derfrzocker.custom.ore.generator.api.customdata;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public interface LimitedValuesCustomData extends CustomData {

    Set<Object> BOOLEAN_VALUE = Collections.unmodifiableSet(Sets.newHashSet(true, false));

    /**
     * @param material to use
     * @return a set with all possible value object this CustomData can have
     * @throws IllegalArgumentException if material is null
     */
    @NotNull
    Set<Object> getPossibleValues(@NotNull Material material);

}
