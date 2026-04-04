package de.derfrzocker.custom.ore.generator.impl;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.spigot.utils.Config;
import de.derfrzocker.spigot.utils.ReloadAble;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class InfoImpl implements Info, ReloadAble {

    @NotNull
    private final Supplier<ConfigurationSection> configurationSectionSupplier;
    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private ConfigurationSection section;

    public InfoImpl(@NotNull final JavaPlugin plugin, @NotNull final String file) {
        this(plugin, file, true);
    }

    public InfoImpl(@NotNull final JavaPlugin plugin, @NotNull final String file, final boolean copy) {
        this(plugin, () -> copy ? Config.getConfig(plugin, file) : new Config(plugin.getResource(file)));
    }

    public InfoImpl(@NotNull final JavaPlugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
        Validate.notNull(plugin, "JavaPlugin can not be null");
        Validate.notNull(configurationSectionSupplier, "Supplier can not be null");

        this.configurationSectionSupplier = configurationSectionSupplier;
        this.plugin = plugin;
        this.section = configurationSectionSupplier.get();
        RELOAD_ABLES.add(this);
        reload();
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return MessageUtil.replacePlaceHolder(plugin, section.getString("display-name"));
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return Material.valueOf(section.getString("material"));
    }

    @NotNull
    @Override
    public String getDescription() {
        return MessageUtil.replacePlaceHolder(plugin, section.getString("description"));
    }

    @Override
    public void reload() {
        section = configurationSectionSupplier.get();
    }

}
