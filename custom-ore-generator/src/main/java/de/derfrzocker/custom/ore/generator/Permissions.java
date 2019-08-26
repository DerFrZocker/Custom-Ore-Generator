package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.spigot.utils.Permission;
import lombok.*;
import org.bukkit.permissions.Permissible;

import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Permissions {

    public final static Permission BASE_PERMISSION = new Permission(null, "custom.ore.gen", CustomOreGenerator.getInstance(), false);
    public final static Permission RELOAD_PERMISSION = new Permission(BASE_PERMISSION, "reload", CustomOreGenerator.getInstance(), true);
    public final static Permission SET_PERMISSION = new Permission(BASE_PERMISSION, "set", CustomOreGenerator.getInstance(), true);
    public final static Permission SET_BIOME_PERMISSION = new Permission(SET_PERMISSION, "biome", CustomOreGenerator.getInstance(), true);

}
