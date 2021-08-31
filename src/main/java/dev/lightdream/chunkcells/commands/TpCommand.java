package dev.lightdream.chunkcells.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TpCommand extends Command {
    public TpCommand(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("tp"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        User user = Main.instance.databaseManager.getUser((Player) commandSender);
        if(!user.hasCell()){
            MessageUtils.sendMessage(user, Main.instance.lang.noCell);
            return;
        }
        user.getPlayer().teleport(Utils.getCellLocation(user).toLocation());
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
