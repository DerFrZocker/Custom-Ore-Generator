package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.spigot.utils.Permission;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Permissions {

    public final static Permission BASE_PERMISSION = new Permission(null, "custom.ore.gen", CustomOreGenerator.getInstance(), false);
    public final static Permission RELOAD_PERMISSION = new Permission(BASE_PERMISSION, "reload", CustomOreGenerator.getInstance(), true);
    public final static Permission SET_PERMISSION = new Permission(BASE_PERMISSION, "set", CustomOreGenerator.getInstance(), true);
    public final static Permission SET_VALUE_PERMISSION = new Permission(SET_PERMISSION, "value", CustomOreGenerator.getInstance(), true);
    public final static Permission SET_BIOME_PERMISSION = new Permission(SET_PERMISSION, "biome", CustomOreGenerator.getInstance(), true);

}
