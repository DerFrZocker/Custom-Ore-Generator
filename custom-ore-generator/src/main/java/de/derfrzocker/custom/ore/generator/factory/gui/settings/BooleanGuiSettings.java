package de.derfrzocker.custom.ore.generator.factory.gui.settings;

import de.derfrzocker.spigot.utils.gui.BasicSettings;
import de.derfrzocker.spigot.utils.gui.VerifyGui;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BooleanGuiSettings extends BasicSettings implements VerifyGui.VerifyGuiSettingsInterface {

    public BooleanGuiSettings(@NotNull final Plugin plugin, @NotNull final String file) {
        super(plugin, file);
    }

    public BooleanGuiSettings(@NotNull final Plugin plugin, @NotNull final String file, final boolean copy) {
        super(plugin, file, copy);
    }

    public BooleanGuiSettings(@NotNull final Plugin plugin, @NotNull final Supplier<ConfigurationSection> configurationSectionSupplier) {
        super(plugin, configurationSectionSupplier);
    }

    @Override
    public int getAcceptSlot() {
        return getSection().getInt("true.slot");
    }

    @Override
    public ItemStack getAcceptItemStack() {
        return getSection().getItemStack("true.item-stack");
    }

    @Override
    public ItemStack getDenyItemStack() {
        return getSection().getItemStack("false.item-stack").clone();
    }

    @Override
    public int getDenySlot() {
        return getSection().getInt("false.slot");
    }

}
