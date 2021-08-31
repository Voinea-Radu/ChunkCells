package dev.lightdream.chunkcells.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.utils.ItemBuilder;
import dev.lightdream.chunkcells.Main;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetRaidTool extends Command {
    public GetRaidTool(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("getRaidTool"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        Player player = (Player) commandSender;
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setLore(Main.instance.config.raidToolLore)
                .build());

    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
