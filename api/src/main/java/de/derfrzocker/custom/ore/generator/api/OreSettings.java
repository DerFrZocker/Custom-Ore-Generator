package de.derfrzocker.custom.ore.generator.api;

/**
 * Some standard OreSettings
 */
public final class OreSettings {

    public final static OreSetting VEIN_SIZE = OreSetting.createOreSetting("VEIN_SIZE", 0);
    public final static OreSetting VEINS_PER_CHUNK = OreSetting.createOreSetting("VEINS_PER_CHUNK", 0);
    public final static OreSetting HEIGHT_RANGE = OreSetting.createOreSetting("HEIGHT_RANGE", -1);
    public final static OreSetting HEIGHT_CENTER = OreSetting.createOreSetting("HEIGHT_CENTER", -1);
    public final static OreSetting MINIMUM_ORES_PER_CHUNK = OreSetting.createOreSetting("MINIMUM_ORES_PER_CHUNK", -1);
    public final static OreSetting ORES_PER_CHUNK_RANGE = OreSetting.createOreSetting("ORES_PER_CHUNK_RANGE", -1);
    public final static OreSetting MINIMUM_HEIGHT = OreSetting.createOreSetting("MINIMUM_HEIGHT", 0);
    public final static OreSetting HEIGHT_SUBTRACT_VALUE = OreSetting.createOreSetting("HEIGHT_SUBTRACT_VALUE", -1);

    private OreSettings() {
    }

}
