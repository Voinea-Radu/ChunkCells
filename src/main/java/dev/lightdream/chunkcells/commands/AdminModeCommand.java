package dev.lightdream.chunkcells.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminModeCommand extends Command {
    public AdminModeCommand(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("adminMode"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        User user = Main.instance.databaseManager.getUser((Player) commandSender);
        if (Main.instance.eventsManager.adminMode.contains(user.uuid)) {
            Main.instance.eventsManager.adminMode.remove(user.uuid);
        } else {
            Main.instance.eventsManager.adminMode.add(user.uuid);
        }
        MessageUtils.sendMessage(user, Main.instance.lang.adminMode);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
