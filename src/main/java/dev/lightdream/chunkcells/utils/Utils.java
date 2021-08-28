package dev.lightdream.chunkcells.utils;

import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils extends dev.lightdream.api.utils.Utils {

    private static final List<UUID> toggledPlayers = new ArrayList<>();

    public static void generateCell(Player player) {
        User user = Main.instance.databaseManager.getUser(player);
        user.cellX = Main.instance.saves.x;
        user.cellZ = Main.instance.saves.z;

        MessageUtils.sendMessage(player, Main.instance.lang.creatingCell);
        player.teleport(Main.instance.config.cellCreateWaitingLocation.toLocation());
        Main.instance.permission.playerAdd(player, "worldedit.*");
        Bukkit.dispatchCommand(player, "/schem load C1");
        if (!toggledPlayers.contains(player.getUniqueId())) {
            Bukkit.dispatchCommand(player, "/toggleplace");
            toggledPlayers.add(player.getUniqueId());
        }

        int posX;
        int posZ;
        Location tpLocation;

        if (Main.instance.saves.z == 0) {
            Main.instance.saves.z++;
            posX = (Main.instance.saves.x + 1) * Main.CELL_X_SIZE - 1;
            posZ = Main.instance.saves.z * Main.CELL_Z_SIZE - 1;
            tpLocation = new PluginLocation(Main.instance.config.cellWorld, posX - 31.5f, Main.instance.config.cellPasteY + 23, posZ).toLocation();
            tpLocation.setYaw(180);
            Bukkit.dispatchCommand(player, "/rotate 180");
        } else {
            posX = Main.instance.saves.x * Main.CELL_X_SIZE;
            posZ = Main.instance.saves.z * Main.CELL_Z_SIZE;
            Main.instance.saves.z = 0;
            Main.instance.saves.x++;
            tpLocation = new PluginLocation(Main.instance.config.cellWorld, posX + 31.5f, Main.instance.config.cellPasteY + 23, posZ).toLocation();
            tpLocation.setYaw(0);
        }

        Bukkit.dispatchCommand(player, "/pos1 " + posX + "," + Main.instance.config.cellPasteY + "," + posZ);
        Bukkit.dispatchCommand(player, "/paste");
        Main.instance.permission.playerAdd(player, "worldedit.*");
        player.teleport(tpLocation);
        MessageUtils.sendMessage(player, Main.instance.lang.cellCreated);
        Main.instance.databaseManager.save(user);
    }

}
