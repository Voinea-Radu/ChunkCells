package dev.lightdream.chunkcells.utils;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.CuboidClipboard;
import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.api.files.dto.XMaterial;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.api.utils.WorldEditUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.files.dto.PositionRange;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.List;

@SuppressWarnings("deprecation")
public class Utils extends dev.lightdream.api.utils.Utils {

    public static void generateCell(String axis) {
        //Mark the cell as generated
        int cell = Main.instance.saves.axis.get(axis);
        Main.instance.saves.axis.put(axis, cell + Main.instance.config.numberOfCellsPerSchematic);

        //Load schematic
        CuboidClipboard clipboard = WorldEditUtils.load("schematics", "C1", Main.instance);

        //Get paste location
        PluginLocation location = getCellPasteLocation(axis, cell);

        //Paste
        clipboard.rotate2D((int) location.rotationX);
        WorldEditUtils.paste(location, clipboard);
        WorldEditUtils.paste(location, clipboard);

        Main.instance.config.extraCellGenerateCommands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
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
            case "wall":
                return "W";
        }
        return "";
    }

    public static void upgradeCell(User user, String type, int typeLevel) {
        //Teleport to wait position
        user.getPlayer().teleport(Main.instance.config.cellCreateWaitingLocation.toLocation());
        MessageUtils.sendMessage(user, Main.instance.lang.upgradingCell);

        //Get the short type
        String shortType = shortenType(type);

        //Get paste location
        PluginLocation location = getCellPasteLocation(user.cellAxis, user.cell);

        //Get the schematic
        CuboidClipboard clipboard;
        if (shortType.equals("")) {
            clipboard = WorldEditUtils.load("schematics", "C" + user.cellLevel, Main.instance);
        } else {
            clipboard = WorldEditUtils.load("schematics", "C" + user.cellLevel + shortType + typeLevel, Main.instance);
        }
        clipboard.rotate2D((int) location.rotationX);

        //Paste schematic at location
        WorldEditUtils.paste(location, clipboard);
        MessageUtils.sendMessage(user, Main.instance.lang.cellUpgraded);

        //Teleport your to the cell
        user.getPlayer().teleport(getCellLocation(user).toLocation());

        //Refill them mine
        mineRefill(user);
    }

    @SneakyThrows
    public static int mineRefill(User user) {
        if (!user.isOnline()) {
            return 0;
        }
        List<Block> blocks = getCellMine(user).toLocationRange(Main.instance.config.cellWorld).getBlocks();

        List<List<Block>> blockChunks = Lists.partition(blocks, Main.instance.config.mineRefillChunks);

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
                for (XMaterial material : Main.instance.config.cellMinesBlocks.keySet()) {
                    int chance = Main.instance.config.cellMinesBlocks.get(material);
                    if (rnd - chance <= 0) {
                        block.setType(material.parseMaterial());
                        break;
                    }
                    rnd -= chance;
                }
            }), i);
        }

        return i;
    }

    public static PositionRange getCellMine(User user) {
        PositionRange offset = user.getMineOffset();
        PositionRange range = user.getMineOffset();
        PluginLocation cellPasteLocation = getCellPasteLocation(user.cellAxis, user.cell);

        range.pos1.y += Main.instance.config.cellPasteY;
        range.pos2.y += Main.instance.config.cellPasteY;

        switch (user.cellAxis) {
            case "+X":
                if (user.cell % 2 == 0) {
                    range.pos1.x = (int) (cellPasteLocation.x - offset.pos1.x + 1);
                    range.pos2.x = (int) (cellPasteLocation.x - offset.pos2.x + 1);
                    range.pos1.z = (int) (cellPasteLocation.z - offset.pos1.z + 1);
                    range.pos2.z = (int) (cellPasteLocation.z - offset.pos2.z + 1);
                } else {
                    range.pos1.x = (int) (cellPasteLocation.x + offset.pos1.x - 1);
                    range.pos2.x = (int) (cellPasteLocation.x + offset.pos2.x - 1);
                    range.pos1.z = (int) (cellPasteLocation.z + offset.pos1.z - 1);
                    range.pos2.z = (int) (cellPasteLocation.z + offset.pos2.z - 1);
                }
                break;
            case "+Z":
                if (user.cell % 2 == 0) {
                    range.pos1.z = (int) (cellPasteLocation.z - offset.pos1.x + 1);
                    range.pos2.z = (int) (cellPasteLocation.z - offset.pos2.x + 1);
                    range.pos1.x = (int) (cellPasteLocation.x + offset.pos1.z - 1);
                    range.pos2.x = (int) (cellPasteLocation.x + offset.pos2.z - 1);
                } else {
                    range.pos1.z = (int) (cellPasteLocation.z + offset.pos1.x - 1);
                    range.pos2.z = (int) (cellPasteLocation.z + offset.pos2.x - 1);
                    range.pos1.x = (int) (cellPasteLocation.x - offset.pos1.z + 1);
                    range.pos2.x = (int) (cellPasteLocation.x - offset.pos2.z + 1);
                }
                break;
            case "-X":
                if (user.cell % 2 == 0) {
                    range.pos1.x = (int) (cellPasteLocation.x + offset.pos1.x - 1);
                    range.pos2.x = (int) (cellPasteLocation.x + offset.pos2.x - 1);
                    range.pos1.z = (int) (cellPasteLocation.z + offset.pos1.z - 1);
                    range.pos2.z = (int) (cellPasteLocation.z + offset.pos2.z - 1);
                } else {
                    range.pos1.x = (int) (cellPasteLocation.x - offset.pos1.x + 1);
                    range.pos2.x = (int) (cellPasteLocation.x - offset.pos2.x + 1);
                    range.pos1.z = (int) (cellPasteLocation.z - offset.pos1.z + 1);
                    range.pos2.z = (int) (cellPasteLocation.z - offset.pos2.z + 1);
                }
                break;
            case "-Z":
                if (user.cell % 2 == 0) {
                    range.pos1.z = (int) (cellPasteLocation.z + offset.pos1.x - 1);
                    range.pos2.z = (int) (cellPasteLocation.z + offset.pos2.x - 1);
                    range.pos1.x = (int) (cellPasteLocation.x - offset.pos1.z + 1);
                    range.pos2.x = (int) (cellPasteLocation.x - offset.pos2.z + 1);
                } else {
                    range.pos1.z = (int) (cellPasteLocation.z - offset.pos1.x + 1);
                    range.pos2.z = (int) (cellPasteLocation.z - offset.pos2.x + 1);
                    range.pos1.x = (int) (cellPasteLocation.x + offset.pos1.z - 1);
                    range.pos2.x = (int) (cellPasteLocation.x + offset.pos2.z - 1);
                }
                break;
        }
        return range;
    }

    public static PluginLocation getCellLocation(User user) {
        PluginLocation location = getCellPasteLocation(user.cellAxis, user.cell);
        location.y += Main.instance.config.tpOffsetY;
        switch (user.cellAxis) {
            case "+X":
                if (user.cell % 2 == 0) {
                    location.x -= Main.instance.config.tpOffsetX;
                    location.z -= Main.instance.config.tpOffsetZ;
                } else {
                    location.x += Main.instance.config.tpOffsetX;
                    location.z += Main.instance.config.tpOffsetZ;
                }
                break;
            case "+Z":
                if (user.cell % 2 == 0) {
                    location.z -= Main.instance.config.tpOffsetX;
                    location.x -= Main.instance.config.tpOffsetZ;
                } else {
                    location.z += Main.instance.config.tpOffsetX;
                    location.z += Main.instance.config.tpOffsetZ;
                }
                break;
            case "-X":
                if (user.cell % 2 == 0) {
                    location.x += Main.instance.config.tpOffsetX;
                    location.z += Main.instance.config.tpOffsetZ;
                } else {
                    location.x -= Main.instance.config.tpOffsetX;
                    location.z -= Main.instance.config.tpOffsetZ;
                }
                break;
            case "-Z":
                if (user.cell % 2 == 0) {
                    location.z += Main.instance.config.tpOffsetX;
                    location.x += Main.instance.config.tpOffsetZ;
                } else {
                    location.z -= Main.instance.config.tpOffsetX;
                    location.z -= Main.instance.config.tpOffsetZ;
                }
                break;
        }
        return location;
    }

    public static PluginLocation getCellPasteLocation(String axis, int cell) {
        PluginLocation location = new PluginLocation(Main.instance.config.cellWorld, 0, Main.instance.config.cellPasteY, 0);

        if (cell % 2 == 0) {
            location.x = (cell / 2 + 1) * Main.instance.config.cellSizeX - 1 + Main.instance.config.offsetFromCenter;
            location.z = Main.instance.config.cellSizeZ - 1 - Main.instance.config.offsetFromCenter / 2;
            location.rotationX = 180;
        } else {
            location.x = (cell / 2) * Main.instance.config.cellSizeX + Main.instance.config.offsetFromCenter;
            location.z = Main.instance.config.cellSizeZ - Main.instance.config.offsetFromCenter / 2;
        }
        double tmp;
        switch (axis) {
            case "+Z":
                tmp = location.x;
                location.x = location.z;
                location.z = tmp;
                location.rotationX += 90;
                break;
            case "-X":
                location.x *= -1;
                location.rotationX += 180;
                break;
            case "-Z":
                tmp = location.x;
                location.x = location.z * -1;
                location.z = tmp * -1;
                location.rotationX += 90 + 180;
                break;
        }

        return location;
    }

    public static void fixWalls(User user) {
        //Teleport to wait position
        user.getPlayer().teleport(Main.instance.config.cellCreateWaitingLocation.toLocation());
        MessageUtils.sendMessage(user, Main.instance.lang.fixingWalls);

        //Get paste location
        PluginLocation location = getCellPasteLocation(user.cellAxis, user.cell);

        //Get the schematic
        CuboidClipboard clipboard = WorldEditUtils.load("schematics", "C" + user.cellLevel + "W" + user.wallLevel, Main.instance);
        clipboard.rotate2D((int) location.rotationX);

        //Paste schematic at location
        WorldEditUtils.paste(location, clipboard);

        //
        MessageUtils.sendMessage(user, Main.instance.lang.fixedWalls);

        //Teleport your to the cell
        user.getPlayer().teleport(getCellLocation(user).toLocation());
    }

    public static void assignCell(User user, String axis, int cell) {
        user.cellAxis = axis;
        user.cell = cell;
        user.lastRent = System.currentTimeMillis();
        Main.instance.databaseManager.save(user);

        MessageUtils.sendMessage(user, Main.instance.lang.cellRented);

        mineRefill(user);

        if (user.isOnline()) {
            user.getPlayer().teleport(getCellLocation(user).toLocation());
        }

        boolean full = true;

        for (int i = 0; i < Main.instance.saves.axis.get(axis); i++) {
            if (Main.instance.databaseManager.getUser(axis, i) == null) {
                full = false;
                break;
            }
        }

        if (full) {
            generateCell(axis);
        }
    }

}
