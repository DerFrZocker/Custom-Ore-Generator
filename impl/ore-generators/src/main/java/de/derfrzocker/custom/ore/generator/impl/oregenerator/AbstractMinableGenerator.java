package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractMinableGenerator implements OreGenerator {

    private final Set<OreSetting> neededOreSettings = Collections.unmodifiableSet(Sets.newHashSet(OreSettings.VEIN_SIZE));

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

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, final double value, @NotNull final OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(neededOreSettings.contains(oreSetting), "The OreGenerator '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        return value >= 0;
    }

}
