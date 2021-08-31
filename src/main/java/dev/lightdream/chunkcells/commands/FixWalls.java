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

public class FixWalls extends Command {
    public FixWalls(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("fixWalls"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
