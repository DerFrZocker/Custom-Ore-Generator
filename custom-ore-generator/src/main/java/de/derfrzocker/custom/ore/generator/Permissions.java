/*
 * MIT License
 *
 * Copyright (c) 2019 Marvin (DerFrZocker)
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

package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.spigot.utils.Permission;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Permissions {

    public final Permission BASE_PERMISSION;
    public final Permission RELOAD_PERMISSION;
    public final Permission CREATE_PERMISSION;
    public final Permission SET_PERMISSION;
    public final Permission SET_VALUE_PERMISSION;
    public final Permission SET_BIOME_PERMISSION;
    public final Permission SET_CUSTOMDATA_PERMISSION;
    public final Permission SET_REPLACE_MATERIAL_PERMISSION;
    public final Permission SET_SELECT_MATERIAL_PERMISSION;
    public final Permission SET_POSITION_PERMISSION;
    public final Permission ADD_PERMISSION;
    public final Permission ADD_ORE_CONFIG_PERMISSION;

    Permissions(@NotNull final JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        BASE_PERMISSION = new Permission(null, "custom.ore.gen", javaPlugin, false);
        RELOAD_PERMISSION = new Permission(BASE_PERMISSION, "reload", javaPlugin, true);
        CREATE_PERMISSION = new Permission(BASE_PERMISSION, "create", javaPlugin, true);

        SET_PERMISSION = new Permission(BASE_PERMISSION, "set", javaPlugin, true);
        SET_VALUE_PERMISSION = new Permission(SET_PERMISSION, "value", javaPlugin, true);
        SET_BIOME_PERMISSION = new Permission(SET_PERMISSION, "biome", javaPlugin, true);
        SET_CUSTOMDATA_PERMISSION = new Permission(SET_PERMISSION, "customdata", javaPlugin, true);
        SET_REPLACE_MATERIAL_PERMISSION = new Permission(SET_PERMISSION, "replace-material", javaPlugin, true);
        SET_SELECT_MATERIAL_PERMISSION = new Permission(SET_PERMISSION, "select-material", javaPlugin, true);
        SET_POSITION_PERMISSION = new Permission(SET_PERMISSION, "position", javaPlugin, true);

        ADD_PERMISSION = new Permission(BASE_PERMISSION, "add", javaPlugin, true);
        ADD_ORE_CONFIG_PERMISSION = new Permission(ADD_PERMISSION, "ore-config", javaPlugin, true);
    }

}
