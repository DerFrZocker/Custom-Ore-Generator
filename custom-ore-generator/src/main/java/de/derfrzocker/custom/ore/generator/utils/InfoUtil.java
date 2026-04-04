package de.derfrzocker.custom.ore.generator.utils;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import de.derfrzocker.custom.ore.generator.impl.InfoImpl;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class InfoUtil {

    @NotNull
    public static Info getBlockSelectorInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name) {
        return new InfoImpl(javaPlugin, "data/info/block-selector/" + name + "/" + name + ".yml");
    }

    @NotNull
    public static Info getOreGeneratorInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name) {
        return new InfoImpl(javaPlugin, "data/info/ore-generator/" + name + "/" + name + ".yml");
    }

    @NotNull
    public static Info getCustomDataInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name) {
        return new InfoImpl(javaPlugin, "data/info/custom-data/" + name + ".yml");
    }

    @NotNull
    public static Info getBlockSelectorOreSettingInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name, @NotNull final OreSetting oreSetting) {
        return new InfoImpl(javaPlugin, "data/info/block-selector/" + name + "/ore-setting/" + oreSetting.getName() + ".yml");
    }

    @NotNull
    public static Info getOreGeneratorOreSettingInfo(@NotNull final JavaPlugin javaPlugin, @NotNull final String name, @NotNull final OreSetting oreSetting) {
        return new InfoImpl(javaPlugin, "data/info/ore-generator/" + name + "/ore-setting/" + oreSetting.getName() + ".yml");
    }

}
