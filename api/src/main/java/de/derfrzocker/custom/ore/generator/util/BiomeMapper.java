package de.derfrzocker.custom.ore.generator.util;

import com.google.common.collect.Sets;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.bukkit.block.Biome;

public final class BiomeMapper {

    private static final boolean KEYED;

    private static final Method GET_KEY;
    private static final Object BIOME_REGISTRY;
    private static final Constructor<?> NAMESPACE_CONSTRUCTOR;
    private static final Method GET_BIOME;
    private static final Method ITERATOR;

    static {
        Method tmpGetKey;
        Object tmpBiomeRegistry;
        Constructor<?> tmpNamespaceConstructor;
        Method tmpGetBiome;
        Method tmpIterator;
        try {
            tmpGetKey = Biome.class.getMethod("getKey");

            tmpBiomeRegistry = Class.forName("org.bukkit.Registry").getDeclaredField("BIOME").get(null);
            tmpNamespaceConstructor = Class
                    .forName("org.bukkit.NamespacedKey")
                    .getConstructor(String.class, String.class);
            tmpGetBiome = Class
                    .forName("org.bukkit.Registry")
                    .getMethod("get", Class.forName("org.bukkit.NamespacedKey"));
            tmpIterator = Class.forName("org.bukkit.Registry").getMethod("iterator");
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | NoSuchMethodException e) {
            tmpGetKey = null;
            tmpBiomeRegistry = null;
            tmpNamespaceConstructor = null;
            tmpGetBiome = null;
            tmpIterator = null;
            e.printStackTrace();
        }

        GET_KEY = tmpGetKey;
        BIOME_REGISTRY = tmpBiomeRegistry;
        NAMESPACE_CONSTRUCTOR = tmpNamespaceConstructor;
        GET_BIOME = tmpGetBiome;
        ITERATOR = tmpIterator;
        KEYED = GET_KEY != null;
    }

    private BiomeMapper() {

    }

    public static String mapToString(Biome biome) {
        if (KEYED) {
            try {
                return GET_KEY.invoke(biome).toString();
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(String.format("Failed to map biome '%s' to string", biome), e);
            }
        } else {
            return biome.toString();
        }
    }

    public static Biome mapFromString(String biomeString) {
        if (!KEYED) {
            return Biome.valueOf(biomeString.toUpperCase(Locale.ROOT));
        }

        biomeString = biomeString.toLowerCase(Locale.ROOT);

        String namespace;
        String key;
        if (biomeString.contains(":")) {
            namespace = biomeString.substring(0, biomeString.indexOf(':'));
            key = biomeString.substring(biomeString.indexOf(':') + 1);
        } else {
            namespace = "minecraft";
            key = biomeString;
        }

        try {
            Object namespacedKey = NAMESPACE_CONSTRUCTOR.newInstance(namespace, key);
            return (Biome) GET_BIOME.invoke(BIOME_REGISTRY, namespacedKey);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<Biome> listAll() {
        if (KEYED) {
            try {
                Set<Biome> biomes = Sets.newHashSet();
                Iterator<Biome> iterator = (Iterator<Biome>) ITERATOR.invoke(BIOME_REGISTRY);
                iterator.forEachRemaining(biomes::add);
                return biomes;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return Sets.newHashSet(Biome.values());
    }
}
