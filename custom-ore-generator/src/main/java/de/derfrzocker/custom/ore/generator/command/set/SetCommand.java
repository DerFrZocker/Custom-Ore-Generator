package de.derfrzocker.custom.ore.generator.command.set;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.spigot.utils.CommandSeparator;


public class SetCommand extends CommandSeparator {

    public SetCommand() {
        super(CustomOreGenerator.getInstance());
        registerExecutor(new SetValueCommand(), "value", Permissions.SET_VALUE_PERMISSION);
        registerExecutor(new SetBiomeCommand(), "biome", Permissions.SET_BIOME_PERMISSION);
    }

}
