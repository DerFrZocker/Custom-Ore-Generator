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
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.oregenerator.SingleOreGenerator_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.WorldHandler_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.oregenerator.MinableGenerator_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.oregenerator.SingleOreGenerator_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.WorldHandler_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.oregenerator.MinableGenerator_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.oregenerator.SingleOreGenerator_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.WorldHandler_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.oregenerator.MinableGenerator_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.oregenerator.SingleOreGenerator_v1_14_R1;
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

        switch (version) {
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
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_8_R2() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_8_R2(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_8_R2();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_8_R3() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_8_R3(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_8_R3();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_9_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_9_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_9_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_9_R2() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_9_R2(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_9_R2();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_10_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_10_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_10_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_11_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_11_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_11_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_12_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new CustomOreBlockPopulator_v1_12_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_12_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator());
    }

    private void init_V1_13_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_13_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_13_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator_v1_13_R1());
    }

    private void init_V1_13_R2() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_13_R2(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_13_R2();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator_v1_13_R2());
    }

    private void init_V1_13_R2_paper() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_13_R2_paper(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_13_R2_paper();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator_v1_13_R2_paper());
    }

    private void init_V1_14_R1() {
        final CustomOreGeneratorService service = serviceSupplier.get();

        new WorldHandler_v1_14_R1(javaPlugin, serviceSupplier);
        final OreGenerator oreGenerator = new MinableGenerator_v1_14_R1();

        service.registerOreGenerator(oreGenerator);
        service.setDefaultOreGenerator(oreGenerator);
        service.registerOreGenerator(new SingleOreGenerator_v1_14_R1());
    }

}
