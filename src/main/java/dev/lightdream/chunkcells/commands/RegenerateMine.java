package dev.lightdream.chunkcells.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegenerateMine extends Command {
    public RegenerateMine(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("regenerate"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        Utils.mineRefill(Main.instance.databaseManager.getUser((Player) commandSender));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
