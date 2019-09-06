package de.derfrzocker.custom.ore.generator.command;

import de.derfrzocker.custom.ore.generator.CustomOreGenerator;
import de.derfrzocker.custom.ore.generator.CustomOreGeneratorMessages;
import de.derfrzocker.custom.ore.generator.Permissions;
import de.derfrzocker.spigot.utils.ReloadAble;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class ReloadCommand implements TabExecutor {

    @NonNull
    private final CustomOreGenerator customOreGenerator;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Permissions.RELOAD_PERMISSION.hasPermission(sender))
            return false;

        Bukkit.getScheduler().runTaskAsynchronously(customOreGenerator, () -> {
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
