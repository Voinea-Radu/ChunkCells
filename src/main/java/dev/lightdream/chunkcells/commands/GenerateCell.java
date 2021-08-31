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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenerateCell extends Command {
    public GenerateCell(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("generateCell"), "", "", true, false, "[axis]");
    }

    public final List<String> validAxis = Arrays.asList("+X","-X","+Z","-Z");

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        if(args.size()==0){
            sendUsage(commandSender);
            return;
        }
        if(!validAxis.contains(args.get(0))){
            MessageUtils.sendMessage(commandSender, Main.instance.lang.invalidAxis);
            return;
        }
        User user = Main.instance.databaseManager.getUser((Player) commandSender);
        if (args.size() == 2) {
            if (args.get(1).equals("-f")) {
                if (!user.getPlayer().hasPermission(permission + ".force")) {
                    MessageUtils.sendMessage(user, Main.instance.lang.noPermission);
                    return;
                }
                Utils.generateCell(user.getPlayer(), args.get(0));
                return;
            }
        }
        if (user.hasCell()) {
            MessageUtils.sendMessage(user, Main.instance.lang.alreadyHaveCell);
            return;
        }
        Utils.generateCell(user.getPlayer(), args.get(0));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
