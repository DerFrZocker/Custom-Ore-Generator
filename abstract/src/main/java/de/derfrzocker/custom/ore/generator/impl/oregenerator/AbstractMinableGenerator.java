/*
 * MIT License
 *
 * Copyright (c) 2019 Marvin (DerFrZocker)
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

package de.derfrzocker.custom.ore.generator.impl.oregenerator;

import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.api.OreSettings;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractMinableGenerator extends AbstractOreGenerator {

    private final static Set<OreSetting> NEEDED_ORE_SETTINGS = Collections.unmodifiableSet(Sets.newHashSet(OreSettings.VEIN_SIZE));

    public AbstractMinableGenerator(@NotNull final Info info) {
        super("VANILLA_MINABLE_GENERATOR", NEEDED_ORE_SETTINGS, info);
    }

    public AbstractMinableGenerator(@NotNull final Function<String, Info> infoFunction) {
        super("VANILLA_MINABLE_GENERATOR", NEEDED_ORE_SETTINGS, infoFunction);
    }

    @Override
    public boolean isSaveValue(@NotNull final OreSetting oreSetting, final double value, @NotNull final OreConfig oreConfig) {
        Validate.notNull(oreSetting, "OreSetting can not be null");
        Validate.notNull(oreConfig, "OreConfig can not be null");
        Validate.isTrue(getNeededOreSettings().contains(oreSetting), "The OreGenerator '" + getName() + "' does not need the OreSetting '" + oreSetting.getName() + "'");

        return value >= 0;
    }

}
