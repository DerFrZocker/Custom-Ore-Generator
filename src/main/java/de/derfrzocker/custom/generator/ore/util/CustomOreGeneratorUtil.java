package de.derfrzocker.custom.generator.ore.util;

import java.util.Random;

public class CustomOreGeneratorUtil {

    public static Random getRandom(long seed, int x, int z) {
        Random random = new Random(seed);

        long long1 = random.nextLong();
        long long2 = random.nextLong();
        long newseed = (long) x * long1 ^ (long) z * long2 ^ seed;
        random.setSeed(newseed);

        return random;
    }

}
