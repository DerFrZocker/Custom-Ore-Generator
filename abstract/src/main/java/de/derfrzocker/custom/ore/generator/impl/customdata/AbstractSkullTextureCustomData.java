/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.custom.ore.generator.impl.customdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.derfrzocker.custom.ore.generator.api.Info;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.customdata.CustomDataType;
import org.apache.commons.lang.Validate;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.function.Function;

public abstract class AbstractSkullTextureCustomData extends AbstractCustomData<AbstractSkullTextureCustomData.SkullTextureApplier> {

    public AbstractSkullTextureCustomData(@NotNull final Function<String, Info> infoFunction) {
        super("SKULL_TEXTURE", CustomDataType.STRING, infoFunction);
    }

    @Override
    // example decoded Base64 String: {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/59ac16f296b461d05ea0785d477033e527358b4f30c266aa02f020157ffca736"}}}
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        if (!(customData instanceof String))
            return false;

        try {
            JsonElement jsonElement = new JsonParser().parse(new String(Base64.getDecoder().decode((String) customData)));
            return jsonElement.getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString().contains("minecraft.net");
        } catch (JsonParseException | IllegalStateException | IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    @NotNull
    @Override
    public Object normalize(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        return customData;
    }

    @Override
    public boolean hasCustomData(@NotNull final BlockState blockState) {
        Validate.notNull(blockState, "BlockState can not be null");

        if (!(blockState instanceof Skull))
            return false;

        return getCustomDataApplier().hasCustomData((Skull) blockState);
    }

    @NotNull
    @Override
    public String getCustomData(@NotNull final BlockState blockState) {
        Validate.isTrue(hasCustomData(blockState), "The given BlockState '" + blockState.getType() + ", " + blockState.getLocation() + "' can not have the CustomData '" + getName() + "'");

        return getCustomDataApplier().getCustomData((Skull) blockState);
    }

    public interface SkullTextureApplier extends CustomDataApplier {

        /**
         * @param skull to check
         * @return true if the blockState has a custom texture
         */
        boolean hasCustomData(@NotNull Skull skull);

        /**
         * @param skull to get the data from
         * @return the skull texture link as Base64 encoded
         */
        String getCustomData(@NotNull Skull skull);

    }

}
