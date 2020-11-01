/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
