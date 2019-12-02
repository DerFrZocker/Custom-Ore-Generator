package de.derfrzocker.custom.ore.generator.impl.customdata;

import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.itemmods.main.CustomBlockManager;
import com.gitlab.codedoctorde.itemmods.main.Main;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.ItemModsApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.ItemModsApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.ItemModsApplier_v1_14_R1;
import de.derfrzocker.spigot.utils.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemModsCustomData implements CustomData {

    public static final ItemModsCustomData INSTANCE = new ItemModsCustomData();

    @Nullable
    private CustomDataApplier customDataApplier;

    private ItemModsCustomData() {
    }

    @NotNull
    @Override
    public String getName() {
        return "ITEM_MODS";
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.STRING;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        final CustomBlockManager customBlockManager = Main.getPlugin().getCustomBlockManager();
        final List<BlockConfig> blockConfigs = customBlockManager.getBlockConfigs();

        return blockConfigs.stream().anyMatch(blockConfig -> blockConfig.getBlock().getMaterial() == oreConfig.getMaterial());
    }

    @Override
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        if (!(customData instanceof String))
            return false;

        final CustomBlockManager customBlockManager = Main.getPlugin().getCustomBlockManager();
        final List<BlockConfig> blockConfigs = customBlockManager.getBlockConfigs();

        return blockConfigs.stream().filter(blockConfig -> blockConfig.getBlock().getMaterial() == oreConfig.getMaterial()).anyMatch(blockConfig -> blockConfig.getName().equals(customData));
    }

    @NotNull
    @Override
    public CustomDataApplier getCustomDataApplier() {
        if (customDataApplier == null)
            customDataApplier = getCustomDataApplier0();

        return customDataApplier;
    }

    private CustomDataApplier getCustomDataApplier0() {
        switch (Version.getCurrent()) {
            case v1_14_R1:
                return new ItemModsApplier_v1_14_R1(this);
            case v1_13_R2:
                return new ItemModsApplier_v1_13_R2(this);
            case v1_13_R1:
                return new ItemModsApplier_v1_13_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
