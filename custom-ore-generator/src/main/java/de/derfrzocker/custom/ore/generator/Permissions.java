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
    public final Permission SET_REPLACEMATERIAL_PERMISSION;
    public final Permission SET_SELECTMATERIAL_PERMISSION;
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
        SET_REPLACEMATERIAL_PERMISSION = new Permission(SET_PERMISSION, "replacematerial", javaPlugin, true);
        SET_SELECTMATERIAL_PERMISSION = new Permission(SET_PERMISSION, "selectmaterial", javaPlugin, true);
        SET_POSITION_PERMISSION = new Permission(SET_PERMISSION, "position", javaPlugin, true);

        ADD_PERMISSION = new Permission(BASE_PERMISSION, "add", javaPlugin, true);
        ADD_ORE_CONFIG_PERMISSION = new Permission(ADD_PERMISSION, "ore-config", javaPlugin, true);
    }

}
