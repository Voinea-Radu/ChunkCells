package dev.lightdream.chunkcells.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.UpgradeGUI;
import dev.lightdream.chunkcells.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpgradeCommand extends Command {
    public UpgradeCommand(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("upgrade"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        User user = Main.instance.databaseManager.getUser(((Player)commandSender).getUniqueId());
        if(!user.hasCell()){
            MessageUtils.sendMessage(commandSender, Main.instance.lang.dontHaveCell);
            return;
        }
        UpgradeGUI.getInventory().open((Player) commandSender);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
