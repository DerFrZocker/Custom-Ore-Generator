package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataType;
import org.apache.commons.lang.Validate;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class AbstractVariantCustomData extends AbstractCustomData<AbstractVariantCustomData.VariantApplier> {

    public AbstractVariantCustomData(@NotNull final Function<String, Info> infoFunction) {
        super("VARIANT", CustomDataType.INTEGER, infoFunction);
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return getCustomDataApplier().canApply(oreConfig);
    }

    @Override
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        if (!(customData instanceof Integer))
            return false;

        return getCustomDataApplier().isValidCustomData((Integer) customData, oreConfig);
    }

    @NotNull
    @Override
    public Object normalize(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        return customData;
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        Validate.notNull(blockState, "BlockState can not be null");

        return getCustomDataApplier().hasCustomData(blockState);
    }

    @NotNull
    @Override
    public Integer getCustomData(@NotNull final BlockState blockState) {
        Validate.isTrue(hasCustomData(blockState), "The given BlockState '" + blockState.getType() + ", " + blockState.getLocation() + "' can not have the CustomData '" + getName() + "'");

        return getCustomDataApplier().getCustomData(blockState);
    }

    public interface VariantApplier extends CustomDataApplier {

        /**
         * Checks, if the given OreConfig can use this CustomData
         *
         * @param oreConfig that get's checked
         * @return true if this OreConfig can apply the CustomData
         * @throws IllegalArgumentException if oreConfig is null
         */
        boolean canApply(@NotNull OreConfig oreConfig);

        /**
         * Checks, if the given customData value is valid or not
         *
         * @param customData to check
         * @param oreConfig  which get's the customData
         * @return true if valid other wise false
         * @throws IllegalArgumentException if customData or OreConfig is null
         */
        boolean isValidCustomData(@NotNull Integer customData, @NotNull OreConfig oreConfig);

        /**
         * @param blockState to check
         * @return true if the blockState has a Variant
         */
        boolean hasCustomData(@NotNull BlockState blockState);

        /**
         * @param blockState to get the data from
         * @return the Variant as number
         */
        int getCustomData(@NotNull BlockState blockState);

    }

}
