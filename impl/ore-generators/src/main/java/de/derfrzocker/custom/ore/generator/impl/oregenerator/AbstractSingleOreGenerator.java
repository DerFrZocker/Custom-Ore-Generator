package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSingleOreGenerator implements OreGenerator {

    private final Set<OreSetting> neededOreSettings = Collections.unmodifiableSet(new HashSet<>());

    @NotNull
    @Override
    public Set<OreSetting> getNeededOreSettings() {
        return neededOreSettings;
    }

    @NotNull
    @Override
    public String getName() {
        return "SINGLE_ORE_GENERATOR";
    }

}
