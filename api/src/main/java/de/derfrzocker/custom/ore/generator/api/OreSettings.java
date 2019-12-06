package de.derfrzocker.custom.ore.generator.api;

/**
 * Some standard OreSettings
 */
public final class OreSettings {

    public final static OreSetting VEIN_SIZE = OreSetting.createOreSetting("VEIN_SIZE");
    public final static OreSetting VEINS_PER_CHUNK = OreSetting.createOreSetting("VEINS_PER_CHUNK");
    public final static OreSetting HEIGHT_RANGE = OreSetting.createOreSetting("HEIGHT_RANGE");
    public final static OreSetting HEIGHT_CENTER = OreSetting.createOreSetting("HEIGHT_CENTER");
    public final static OreSetting MINIMUM_ORES_PER_CHUNK = OreSetting.createOreSetting("MINIMUM_ORES_PER_CHUNK");
    public final static OreSetting ORES_PER_CHUNK_RANGE = OreSetting.createOreSetting("ORES_PER_CHUNK_RANGE");
    public final static OreSetting MINIMUM_HEIGHT = OreSetting.createOreSetting("MINIMUM_HEIGHT");
    public final static OreSetting HEIGHT_SUBTRACT_VALUE = OreSetting.createOreSetting("HEIGHT_SUBTRACT_VALUE");

    private OreSettings() {
    }

}
