package de.derfrzocker.custom.ore.generator.impl.blockselector;

import de.derfrzocker.custom.ore.generator.api.BlockSelector;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractBlockSelector implements BlockSelector {

    @NotNull
    private final String name;
    @NotNull
    private final Set<OreSetting> neededOreSettings;
    @NotNull
    private final Info info;
    @NotNull
    private final Map<OreSetting, Info> oreSettingInfoMap;

    /**
     * The infoFunction gives the name of the BlockSelector as value.
     * The oreSettingInfo gives the name of the BlockSelector and the OreSetting as values.
     *
     * @param name              of the BlockSelector
     * @param neededOreSettings a set which contains the needed OreSetting
     * @param infoFunction      function to get the info object of this BlockSelector
     * @param oreSettingInfo    biFunction to get the info object of a given OreSetting
     * @throws IllegalArgumentException if one of the arguments are null
     */
    public AbstractBlockSelector(@NotNull final String name, @NotNull final Set<OreSetting> neededOreSettings, @NotNull final Function<String, Info> infoFunction, @NotNull final BiFunction<String, OreSetting, Info> oreSettingInfo) {
        Validate.notNull(name, "Name can not be null");
        Validate.notNull(neededOreSettings, "OreSettings can not be null");
        Validate.notNull(infoFunction, "InfoFunction can not be null");

        this.name = name;
        this.neededOreSettings = neededOreSettings;
        this.info = infoFunction.apply(getName());

        final Map<OreSetting, Info> oreSettingInfoMap = new HashMap<>(this.neededOreSettings.size());
        this.neededOreSettings.forEach(oreSetting -> oreSettingInfoMap.put(oreSetting, Objects.requireNonNull(oreSettingInfo.apply(this.name, oreSetting))));
        this.oreSettingInfoMap = Collections.unmodifiableMap(oreSettingInfoMap);
    }

    @NotNull
    @Override
    public Set<OreSetting> getNeededOreSettings() {
        return this.neededOreSettings;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @NotNull
    @Override
    public Info getInfo() {
        return this.info;
    }

    @NotNull
    @Override
    public Info getOreSettingInfo(@NotNull final OreSetting oreSetting) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.isTrue(getNeededOreSettings().contains(oreSetting), "The OreGenerator '" + getName() + "' does not have the OreSetting '" + oreSetting + "'");

        return this.oreSettingInfoMap.get(oreSetting);
    }

}
