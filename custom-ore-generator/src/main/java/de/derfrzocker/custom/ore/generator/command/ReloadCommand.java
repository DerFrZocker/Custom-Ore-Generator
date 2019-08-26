package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.generator.ore.CustomOreGenerator;
import de.derfrzocker.custom.generator.ore.CustomOreGeneratorMessages;
import de.derfrzocker.custom.generator.ore.Permissions;
import de.derfrzocker.custom.generator.ore.util.ReloadAble;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Permissions.RELOAD_PERMISSION.hasPermission(sender))
            return false;

        Bukkit.getScheduler().runTaskAsynchronously(CustomOreGenerator.getInstance(), () -> {
            CustomOreGeneratorMessages.RELOAD_BEGIN.sendMessage(sender);

            ReloadAble.RELOAD_ABLES.forEach(ReloadAble::reload);

            CustomOreGeneratorMessages.RELOAD_END.sendMessage(sender);
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
