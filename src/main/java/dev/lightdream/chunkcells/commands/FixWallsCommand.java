package dev.lightdream.chunkcells.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FixWallsCommand extends SubCommand {
    public FixWallsCommand(@NotNull LightDreamPlugin plugin) {
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
