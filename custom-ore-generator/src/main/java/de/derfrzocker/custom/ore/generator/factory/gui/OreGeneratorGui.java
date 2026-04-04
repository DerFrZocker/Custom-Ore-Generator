package de.derfrzocker.custom.ore.generator.factory.gui;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.factory.OreConfigFactory;
import de.derfrzocker.custom.ore.generator.factory.gui.settings.OreGeneratorGuiSettings;
import de.derfrzocker.spigot.utils.gui.PageGui;
import de.derfrzocker.spigot.utils.message.MessageUtil;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class OreGeneratorGui extends PageGui<OreGenerator> {

    private static OreGeneratorGuiSettings oreGeneratorGuiSettings;

    public OreGeneratorGui(@NotNull final Plugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final OreConfigFactory oreConfigFactory, @NotNull final Consumer<OreConfigFactory> consumer) {
        super(plugin, checkSettings(plugin));

        final CustomOreGeneratorService service = serviceSupplier.get();
        final OreGenerator defaultOreGenerator = service.getDefaultOreGenerator();
        final OreGenerator[] oreGenerators = service.getOreGenerators().toArray(new OreGenerator[0]);


        addDecorations();
        init(oreGenerators, OreGenerator[]::new, this::getItemStack, (oreGenerator, inventoryClickEvent) -> {
            oreConfigFactory.getOreConfigBuilder().oreGenerator(oreGenerator);
            consumer.accept(oreConfigFactory);
        });

        addItem(oreGeneratorGuiSettings.getMenuSlot(), MessageUtil.replaceItemStack(plugin, oreGeneratorGuiSettings.getMenuItemStack()), inventoryClickEvent -> {
            oreConfigFactory.setRunning(false);
            new MenuGui(plugin, serviceSupplier, oreConfigFactory).openSync(inventoryClickEvent.getWhoClicked());
        });
        addItem(oreGeneratorGuiSettings.getAbortSlot(), MessageUtil.replaceItemStack(plugin, oreGeneratorGuiSettings.getAbortItemStack()), inventoryClickEvent -> closeSync(inventoryClickEvent.getWhoClicked()));
        if (defaultOreGenerator != null) {
            addItem(oreGeneratorGuiSettings.getDefaultOreGeneratorSlot(), MessageUtil.replaceItemStack(plugin, oreGeneratorGuiSettings.getDefaultOreGeneratorItemStack(),
                    new MessageValue("description", defaultOreGenerator.getInfo().getDescription()),
                    new MessageValue("name", defaultOreGenerator.getName()),
                    new MessageValue("display-name", defaultOreGenerator.getInfo().getDisplayName())
            ), inventoryClickEvent -> {
                oreConfigFactory.getOreConfigBuilder().oreGenerator(defaultOreGenerator);
                consumer.accept(oreConfigFactory);
            });
        }
    }

    private static OreGeneratorGuiSettings checkSettings(@NotNull final Plugin plugin) {
        if (oreGeneratorGuiSettings == null)
            oreGeneratorGuiSettings = new OreGeneratorGuiSettings(plugin, "data/factory/gui/ore-generator-gui.yml", true);

        return oreGeneratorGuiSettings;
    }

    private ItemStack getItemStack(@NotNull final OreGenerator oreGenerator) {
        final ItemStack itemStack = oreGeneratorGuiSettings.getOreGeneratorItemStack();

        itemStack.setType(oreGenerator.getInfo().getMaterial());

        return MessageUtil.replaceItemStack(getPlugin(), itemStack,
                new MessageValue("description", oreGenerator.getInfo().getDescription()),
                new MessageValue("name", oreGenerator.getName()),
                new MessageValue("display-name", oreGenerator.getInfo().getDisplayName())
        );
    }

}
