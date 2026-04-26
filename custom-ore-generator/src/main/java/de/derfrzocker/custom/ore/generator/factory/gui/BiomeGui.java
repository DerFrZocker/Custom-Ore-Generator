package de.derfrzocker.custom.ore.generator.factory.gui;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.BiomeGuiSettings;
import de.derfrzocker.custom.ore.generator.util.BiomeMapper;
import de.derfrzocker.spigot.utils.gui.PageGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Biome;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BiomeGui extends PageGui<Biome> {

    private static BiomeGuiSettings biomeGuiSettings;

    @NotNull
    private final OreConfigFactory oreConfigFactory;

    public BiomeGui(@NotNull final Plugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer) {
        super(plugin, checkSettings(plugin));

        Validate.notNull(oreConfigFactory, "OreConfigFactory can not be null");

        this.oreConfigFactory = oreConfigFactory;

        addDecorations();
        init(BiomeMapper.listAll().toArray(new Biome[0]), Biome[]::new, this::getItemStack, this::handleNormalClick);

        addItem(biomeGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, biomeGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(biomeGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, biomeGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        addItem(biomeGuiSettings.getNextSlot(), MessageUtil.replaceItemStack(plugin, biomeGuiSettings.getNextItemStack()), inventoryClickEvent -> consumer.accept(oreConfigFactory));
    }

    private static BiomeGuiSettings checkSettings(@NotNull final Plugin plugin) {
        if (biomeGuiSettings == null) {
            biomeGuiSettings = new BiomeGuiSettings(plugin, "data/factory/gui/biome-gui.yml", true);
        }

        return biomeGuiSettings;
    }

    private ItemStack getItemStack(@NotNull final Biome biome) {
        final ItemStack itemStack;
        if (oreConfigFactory.getOreConfigBuilder().containsBiome(biome)) {
            itemStack = biomeGuiSettings.getActivatedItemStack();
        } else {
            itemStack = biomeGuiSettings.getDeactivatedItemStack();
        }

        itemStack.setType(biomeGuiSettings.getBiomeMaterial(BiomeMapper.mapToString(biome)));

        return MessageUtil.replaceItemStack(getPlugin(), itemStack,
                new MessageValue("name", BiomeMapper.mapToString(biome))
        );
    }

    private void handleNormalClick(@NotNull final Biome biome, @NotNull final InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) {
            return;
        }

        final OreConfigBuilder oreConfigBuilder = oreConfigFactory.getOreConfigBuilder();

        if (oreConfigBuilder.containsBiome(biome)) {
            oreConfigBuilder.removeBiome(biome);
        } else {
            oreConfigBuilder.addBiome(biome);
        }

        updateItemStack(biome, getItemStack(biome));
    }

}
