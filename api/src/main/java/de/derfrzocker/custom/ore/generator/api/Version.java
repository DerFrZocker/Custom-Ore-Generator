package de.derfrzocker.custom.ore.generator.api;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public enum Version {

    v1_14_R1, v1_13_R2, v1_13_R1, v1_12_R1, v1_11_R1, v1_10_R1, v1_9_R2, v1_9_R1, v1_8_R3, v1_8_R2, v1_8_R1;

    private final List<Runnable> list = new ArrayList<>();

    @Getter
    private final static Version current;

    @Getter
    private final static boolean paper;

    public static void clear() {
        Stream.of(values()).forEach(value -> value.list.clear());
    }

    public void run() {
        list.forEach(Runnable::run);
    }

    public void add(Runnable runnable) {
        list.add(runnable);
    }

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

        try {
            current = valueOf(version.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("unknown server version: " + version);
        }

        paper = Bukkit.getBukkitVersion().toLowerCase().contains("paper");
    }

}
