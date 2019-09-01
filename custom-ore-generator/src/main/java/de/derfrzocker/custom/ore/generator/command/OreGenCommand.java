package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.custom.ore.generator.command.set.SetCommand;
import de.derfrzocker.spigot.utils.CommandSeparator;

public class OreGenCommand extends CommandSeparator {

    public OreGenCommand() {
        super(CustomOreGenerator.getInstance());
        registerExecutor(new SetCommand(), "set", Permissions.SET_PERMISSION);
        registerExecutor(new ReloadCommand(), "reload", Permissions.RELOAD_PERMISSION);

        final HelpCommand helpCommand = new HelpCommand();

        this.setHelp(true);
        registerExecutor(helpCommand, "help", null);
        registerExecutor(helpCommand, null, null);

    }
}
