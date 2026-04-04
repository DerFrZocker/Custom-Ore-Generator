package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomData;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataType;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class AbstractCustomData<T extends CustomDataApplier> implements CustomData {

    @NotNull
    private final String name;
    @NotNull
    private final CustomDataType customDataType;
    @NotNull
    private final Info info;

    @Nullable
    private T customDataApplier;

    public AbstractCustomData(@NotNull final String name, @NotNull final CustomDataType customDataType, @NotNull final Function<String, Info> infoFunction) {
        Validate.notNull(name, "Name can not be null");
        Validate.notNull(customDataType, "CustomDataType can not be null");
        Validate.notNull(infoFunction, "InfoFunction can not be null");

        this.name = name;
        this.customDataType = customDataType;
        this.info = infoFunction.apply(getName());
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return this.customDataType;
    }

    @NotNull
    @Override
    public T getCustomDataApplier() {
        if (this.customDataApplier == null)
            this.customDataApplier = getCustomDataApplier0();

        return this.customDataApplier;
    }

    @NotNull
    protected abstract T getCustomDataApplier0();

    @NotNull
    @Override
    public Info getInfo() {
        return this.info;
    }

}
