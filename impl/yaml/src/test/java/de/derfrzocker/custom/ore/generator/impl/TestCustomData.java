package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public class TestCustomData implements CustomData {

    @NotNull
    private final String name;

    public TestCustomData(@NotNull final String name) {
        Validate.notNull(name, "Name can not be null");
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.STRING;
    }

    @Override
    public boolean canApply(@NotNull OreConfig oreConfig) {
        return false;
    }

    @Override
    public boolean isValidCustomData(@NotNull Object customData, @NotNull OreConfig oreConfig) {
        return false;
    }

    @NotNull
    @Override
    public CustomDataApplier getCustomDataApplier() {
        return null;
    }
}
