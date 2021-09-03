package dev.lightdream.chunkcells.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.guis.ChoseAxisGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RentCommand extends Command {
    public RentCommand(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("rent"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        new ChoseAxisGUI(Main.instance).getInventory().open((Player) commandSender);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
