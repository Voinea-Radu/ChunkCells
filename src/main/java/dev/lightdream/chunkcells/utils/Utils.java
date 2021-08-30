package dev.lightdream.chunkcells.utils;

import com.google.common.collect.Lists;
import dev.lightdream.api.files.dto.LocationRange;
import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.api.files.dto.XMaterial;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.files.dto.PositionRange;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
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
        Main.instance.permission.playerRemove(player, "worldedit.*");
        player.teleport(tpLocation);
        MessageUtils.sendMessage(player, Main.instance.lang.cellCreated);
        Main.instance.databaseManager.save(user);
        mineRefill(user);
    }

    public static String shortenType(String type) {
        switch (type) {
            case "furnace":
                return "Fu";
            case "farm":
                return "Fa";
            case "mine":
                return "M";
            case "crops":
                return "C";
            case "blocks":
                return "B";
        }
        return "";
    }

    public static void upgradeCell(Player player, int cellLevel, String type, int typeLevel) {
        String shortType = shortenType(type);

        User user = Main.instance.databaseManager.getUser(player);

        MessageUtils.sendMessage(player, Main.instance.lang.upgradingCell);
        player.teleport(Main.instance.config.cellCreateWaitingLocation.toLocation());
        Main.instance.permission.playerAdd(player, "worldedit.*");
        if (!shortType.equals("")) {
            Bukkit.dispatchCommand(player, "/schem load C" + cellLevel + shortType + typeLevel);
        } else {
            Bukkit.dispatchCommand(player, "/schem load C" + cellLevel);
        }
        if (!toggledPlayers.contains(player.getUniqueId())) {
            Bukkit.dispatchCommand(player, "/toggleplace");
            toggledPlayers.add(player.getUniqueId());
        }

        int posX;
        int posZ;
        Location tpLocation;

        if (user.cellZ == 0) {
            posX = (user.cellX + 1) * Main.CELL_X_SIZE - 1;
            posZ = Main.CELL_Z_SIZE - 1;
            tpLocation = new PluginLocation(Main.instance.config.cellWorld, posX - 31.5f, Main.instance.config.cellPasteY + 23, posZ).toLocation();
            tpLocation.setYaw(180);
            Bukkit.dispatchCommand(player, "/rotate 180");
        } else {
            posX = user.cellX * Main.CELL_X_SIZE;
            posZ = user.cellZ * Main.CELL_Z_SIZE;
            tpLocation = new PluginLocation(Main.instance.config.cellWorld, posX + 31.5f, Main.instance.config.cellPasteY + 23, posZ).toLocation();
            tpLocation.setYaw(0);
        }

        Bukkit.dispatchCommand(player, "/pos1 " + posX + "," + Main.instance.config.cellPasteY + "," + posZ);
        Bukkit.dispatchCommand(player, "/paste");
        Main.instance.permission.playerRemove(player, "worldedit.*");
        player.teleport(tpLocation);
        MessageUtils.sendMessage(player, Main.instance.lang.cellUpgraded);
        mineRefill(user);
    }

    @SneakyThrows
    public static int mineRefill(User user) {
        if (!user.isOnline()) {
            return 0;
        }
        //todo to utils
        PositionRange range = user.getMineRange();
        range.pos1.y += Main.instance.config.cellPasteY;
        range.pos2.y += Main.instance.config.cellPasteY;
        if (user.cellZ == 0) {
            int x = (user.cellX + 1) * Main.CELL_X_SIZE;
            int z = Main.CELL_Z_SIZE;
            range.pos1.x = x - range.pos1.x;
            range.pos2.x = x - range.pos2.x;
            range.pos1.z = z - range.pos1.z;
            range.pos2.z = z - range.pos2.z;
        } else {
            int x = user.cellX * Main.CELL_X_SIZE;
            int z = user.cellX * Main.CELL_X_SIZE;
            range.pos1.x = x + range.pos1.x;
            range.pos2.x = x + range.pos2.x;
            range.pos1.z = z + range.pos1.z;
            range.pos2.z = z + range.pos2.z;
        }
        List<Block> blocks = range.toLocationRange(Main.instance.config.cellWorld).getBlocks();
        System.out.println(blocks.size());

        //todo config
        List<List<Block>> blockChunks = Lists.partition(blocks, 500);

        int i;
        int sum = 0;

        for (XMaterial material : Main.instance.config.cellMinesBlocks.keySet()) {
            sum += Main.instance.config.cellMinesBlocks.get(material);
        }

        for (i = 0; i < blockChunks.size(); i++) {
            List<Block> b = blockChunks.get(i);
            int finalSum = sum;
            Bukkit.getScheduler().runTaskLater(Main.instance, () -> b.forEach(block -> {
                int rnd = generateRandom(0, finalSum);
                System.out.println("Initial rnd = " + rnd);
                for (XMaterial material : Main.instance.config.cellMinesBlocks.keySet()) {
                    int chance = Main.instance.config.cellMinesBlocks.get(material);
                    System.out.println(rnd);
                    if (rnd - chance <= 0) {
                        System.out.println("Setting block at " + block.getLocation()+ " to " + material);
                        block.setType(material.parseMaterial());
                        break;
                    }
                    rnd -= chance;
                }
            }), i);
        }

        return i;

    }

}
