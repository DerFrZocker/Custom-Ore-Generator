package de.derfrzocker.custom.ore.generator.utils;

import de.derfrzocker.custom.ore.generator.CustomOreBlockPopulator;
import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.MinableGenerator_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.MinableGenerator_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.MinableGenerator_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.MinableGenerator_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.WorldHandler_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.MinableGenerator_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.WorldHandler_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.MinableGenerator_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.paper.WorldHandler_v1_13_R2_paper;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.MinableGenerator_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.WorldHandler_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.MinableGenerator_v1_8_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.MinableGenerator_v1_8_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.MinableGenerator_v1_8_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.MinableGenerator_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.MinableGenerator_v1_9_R2;
import de.derfrzocker.spigot.utils.Version;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VersionPicker {

    private boolean init;

    @NonNull
    private final Version version;

    @NonNull
    private final CustomOreGeneratorService customOreGeneratorService;

    @NonNull
    private final CustomOreGenerator customOreGenerator;

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
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_8_R1());
    }

    private void init_V1_8_R2() {
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_8_R2());
    }

    private void init_V1_8_R3() {
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_8_R3());
    }

    private void init_V1_9_R1() {
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_9_R1());
    }

    private void init_V1_9_R2() {
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_9_R2());
    }

    private void init_V1_10_R1() {
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_10_R1());
    }

    private void init_V1_11_R1() {
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_11_R1());
    }

    private void init_V1_12_R1() {
        new CustomOreBlockPopulator(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_12_R1());
    }

    private void init_V1_13_R1() {
        new WorldHandler_v1_13_R1(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_13_R1());
    }

    private void init_V1_13_R2() {
        new WorldHandler_v1_13_R2(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_13_R2());
    }

    private void init_V1_13_R2_paper() {
        new WorldHandler_v1_13_R2_paper(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_13_R2_paper());
    }

    private void init_V1_14_R1() {
        new WorldHandler_v1_14_R1(customOreGenerator);
        customOreGeneratorService.setDefaultOreGenerator(new MinableGenerator_v1_14_R1());
    }

}
