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

package de.derfrzocker.custom.ore.generator.utils;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.impl.oregenerator.SingleOreGenerator;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.CustomOreBlockPopulator_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.oregenerator.MinableGenerator_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.CustomOreBlockPopulator_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.oregenerator.MinableGenerator_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.CustomOreBlockPopulator_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.oregenerator.MinableGenerator_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.WorldHandler_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.oregenerator.MinableGenerator_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.WorldHandler_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.oregenerator.MinableGenerator_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.WorldHandler_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.oregenerator.MinableGenerator_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.WorldHandler_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.oregenerator.MinableGenerator_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.WorldHandler_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.oregenerator.MinableGenerator_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.CustomOreBlockPopulator_v1_8_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.oregenerator.MinableGenerator_v1_8_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.CustomOreBlockPopulator_v1_8_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.oregenerator.MinableGenerator_v1_8_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.CustomOreBlockPopulator_v1_8_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.oregenerator.MinableGenerator_v1_8_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.CustomOreBlockPopulator_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.oregenerator.MinableGenerator_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.CustomOreBlockPopulator_v1_9_R2;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.oregenerator.MinableGenerator_v1_9_R2;
import de.derfrzocker.spigot.utils.Version;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class VersionPicker {

    @NotNull
    private final Supplier<CustomOreGeneratorService> serviceSupplier;
    @NotNull
    private final JavaPlugin javaPlugin;
    @NotNull
    private final Version version;
    private boolean init;

    public VersionPicker(@NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final JavaPlugin javaPlugin, @NotNull final Version version) {
        Validate.notNull(serviceSupplier, "Service supplier can not be null");
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");
        Validate.notNull(version, "Version can not be null");

        this.serviceSupplier = serviceSupplier;
        this.javaPlugin = javaPlugin;
        this.version = version;
    }

    public void init() {
        if (init) {
            throw new IllegalStateException("The VersionPicker was already initialized");
        }

        init = true;

        final CustomOreGeneratorService service = serviceSupplier.get();
        service.registerOreGenerator(new SingleOreGenerator());

        switch (version) {
            case v1_15_R1:
                init_V1_15_R1();
                return;
            case v1_14_R1:
                init_V1_14_R1();
                return;
            case v1_13_R2:
                if (Version.isPaper())
                    init_V1_13_R2_paper();
                else
                    init_V1_13_R2();
                return;
            case v1_13_R1:
                init_V1_13_R1();
                return;
            case v1_12_R1:
                init_V1_12_R1();
                return;
            case v1_11_R1:
                init_V1_11_R1();
                return;
            case v1_10_R1:
                init_V1_10_R1();
                return;
            case v1_9_R2:
                init_V1_9_R2();
                return;
            case v1_9_R1:
                init_V1_9_R1();
                return;
            case v1_8_R3:
                init_V1_8_R3();
                return;
            case v1_8_R2:
                init_V1_8_R2();
                return;
            case v1_8_R1:
                init_V1_8_R1();
                return;
        }

        throw new IllegalArgumentException("The version: " + version + " is not supported");
    }

    private void init_V1_8_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_8_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_8_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_8_R2() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_8_R2(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_8_R2();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_8_R3() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_8_R3(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_8_R3();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_9_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_9_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_9_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_9_R2() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_9_R2(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_9_R2();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_10_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_10_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_10_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_11_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_11_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_11_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_12_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_12_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_12_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_13_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_13_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_13_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_13_R2() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_13_R2(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_13_R2();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_13_R2_paper() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_13_R2_paper(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_13_R2_paper();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

    private void init_V1_14_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_14_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_14_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }


    private void init_V1_15_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_15_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_15_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
    }

}
