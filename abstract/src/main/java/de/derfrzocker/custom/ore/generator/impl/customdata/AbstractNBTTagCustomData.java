package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import org.apache.commons.lang.Validate;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Function;

public abstract class AbstractNBTTagCustomData extends FileReadAbleCustomData<AbstractNBTTagCustomData.NBTTagApplier> {

    public AbstractNBTTagCustomData(@NotNull final Function<String, Info> infoFunction, @NotNull final File fileFolder) {
        super("NBT_TAG", infoFunction, fileFolder);
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        return getCustomDataApplier().canApply(oreConfig);
    }

    @Override
    protected boolean isValidCustomData0(@NotNull final String customData, @NotNull final OreConfig oreConfig) {
        return getCustomDataApplier().isValidCustomData(customData, oreConfig);
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        Validate.notNull(blockState, "BlockState can not be null");

        return getCustomDataApplier().hasCustomData(blockState);
    }

    @NotNull
    @Override
    public String getCustomData(@NotNull final BlockState blockState) {
        Validate.isTrue(hasCustomData(blockState), "The given BlockState '" + blockState.getType() + ", " + blockState.getLocation() + "' can not have the CustomData '" + getName() + "'");

        return getCustomDataApplier().getCustomData(blockState);
    }

    public interface NBTTagApplier extends CustomDataApplier {

        /**
         * Checks, if the given customData string is a valid NBT tag
         *
         * @param customData to check
         * @param oreConfig  which get's the customData
         * @return true if valid other wise false
         * @throws IllegalArgumentException if customData or OreConfig is null
         */
        boolean isValidCustomData(@NotNull final String customData, @NotNull final OreConfig oreConfig);

        /**
         * Checks, if the given OreConfig can use this CustomData
         *
         * @param oreConfig that get's checked
         * @return true if this OreConfig can apply the CustomData
         * @throws IllegalArgumentException if oreConfig is null
         */
        boolean canApply(@NotNull OreConfig oreConfig);

        /**
         * @param blockState to check
         * @return true if the blockState has a NBT Tag
         */
        boolean hasCustomData(@NotNull BlockState blockState);

        /**
         * @param blockState to get the data from
         * @return the NBT Tag as String
         */
        @NotNull
        String getCustomData(@NotNull BlockState blockState);

    }

}
