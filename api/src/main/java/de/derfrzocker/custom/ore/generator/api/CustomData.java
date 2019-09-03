package de.derfrzocker.custom.ore.generator.api;

public interface CustomData {

    String getName();

    CustomDataType getCustomDataType();

    boolean canApply(OreConfig oreConfig);

    boolean isValidCustomData(Object customData, OreConfig oreConfig);

    CustomDataApplier getCustomDataApplier();

}
