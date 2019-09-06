package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.spigot.utils.CommandSeparator;


public class SetCommand extends CommandSeparator {

    public SetCommand(CustomOreGenerator customOreGenerator) {
        super(customOreGenerator);
        registerExecutor(new SetValueCommand(customOreGenerator), "value", Permissions.SET_VALUE_PERMISSION);
        registerExecutor(new SetBiomeCommand(customOreGenerator), "biome", Permissions.SET_BIOME_PERMISSION);
        registerExecutor(new SetCustomDataCommand(customOreGenerator), "customdata", Permissions.SET_CUSTOMDATA_PERMISSION);
    }

}
