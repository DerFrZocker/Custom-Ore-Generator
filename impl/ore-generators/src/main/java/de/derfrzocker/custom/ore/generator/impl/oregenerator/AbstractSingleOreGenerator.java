package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import org.apache.commons.lang.Validate;
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

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, final double value, @NotNull final OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(neededOreSettings.contains(oreSetting), "The OreGenerator '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        throw new IllegalArgumentException("The OreGenerator '" + getName() + "' does not need any OreSetting");
    }

}
