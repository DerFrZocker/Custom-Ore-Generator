package de.derfrzocker.custom.ore.generator.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata.SkullTextureApplier_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata.SkullTextureApplier_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.customdata.SkullTextureApplier_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.SkullTextureApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.SkullTextureApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.SkullTextureApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.customdata.SkullTextureApplier_v1_8_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.customdata.SkullTextureApplier_v1_8_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata.SkullTextureApplier_v1_8_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.customdata.SkullTextureApplier_v1_9_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.customdata.SkullTextureApplier_v1_9_R2;
import de.derfrzocker.spigot.utils.Version;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkullTextureCustomData implements CustomData {

    public static final SkullTextureCustomData INSTANCE = new SkullTextureCustomData();

    private static Set<Material> materials = new HashSet<>();

    static {
        switch (Version.getCurrent()) {
            case v1_14_R1:
            case v1_13_R2:
            case v1_13_R1:
                materials.add(Material.valueOf("PLAYER_HEAD"));
                materials.add(Material.valueOf("PLAYER_WALL_HEAD"));
                break;
            case v1_12_R1:
            case v1_11_R1:
            case v1_10_R1:
            case v1_9_R2:
            case v1_9_R1:
            case v1_8_R3:
            case v1_8_R2:
            case v1_8_R1:
                materials.add(Material.valueOf("SKULL"));
                break;
        }

    }

    private CustomDataApplier customDataApplier;

    @Override
    public String getName() {
        return "SKULL_TEXTURE";
    }

    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.STRING;
    }

    @Override
    public boolean canApply(OreConfig oreConfig) {
        return materials.contains(oreConfig.getMaterial());
    }

    @Override
    // example decoded Base64 String: {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/59ac16f296b461d05ea0785d477033e527358b4f30c266aa02f020157ffca736"}}}
    public boolean isValidCustomData(Object customData, OreConfig oreConfig) { // TODO test method
        if (!(customData instanceof String))
            return false;

        try {
            JsonElement jsonElement = new JsonParser().parse(new String(Base64.getDecoder().decode((String) customData)));
            return jsonElement.getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString().startsWith("http://textures.minecraft.net/texture/");
        } catch (JsonParseException | IllegalStateException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public CustomDataApplier getCustomDataApplier() {
        if (customDataApplier == null)
            customDataApplier = getCustomDataApplier0();

        return customDataApplier;
    }

    private CustomDataApplier getCustomDataApplier0() {
        switch (Version.getCurrent()) {
            case v1_14_R1:
                return new SkullTextureApplier_v1_14_R1(this);
            case v1_13_R2:
                return new SkullTextureApplier_v1_13_R2(this);
            case v1_13_R1:
                return new SkullTextureApplier_v1_13_R1(this);
            case v1_12_R1:
                return new SkullTextureApplier_v1_12_R1(this);
            case v1_11_R1:
                return new SkullTextureApplier_v1_11_R1(this);
            case v1_10_R1:
                return new SkullTextureApplier_v1_10_R1(this);
            case v1_9_R2:
                return new SkullTextureApplier_v1_9_R2(this);
            case v1_9_R1:
                return new SkullTextureApplier_v1_9_R1(this);
            case v1_8_R3:
                return new SkullTextureApplier_v1_8_R3(this);
            case v1_8_R2:
                return new SkullTextureApplier_v1_8_R2(this);
            case v1_8_R1:
                return new SkullTextureApplier_v1_8_R1(this);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
