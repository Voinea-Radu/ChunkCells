package dev.lightdream.chunkcells.database;

import com.j256.ormlite.field.DatabaseField;
import dev.lightdream.api.files.dto.LocationRange;
import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.files.dto.PositionRange;
import dev.lightdream.chunkcells.files.dto.UpgradePath;
import dev.lightdream.chunkcells.utils.Utils;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class User extends dev.lightdream.api.databases.User {

    @DatabaseField(columnName = "cell_axis")
    public String cellAxis;
    @DatabaseField(columnName = "cell")
    public int cell;
    @DatabaseField(columnName = "cell_level")
    public int cellLevel;
    @DatabaseField(columnName = "furnace_level")
    public int furnaceLevel;
    @DatabaseField(columnName = "farm_level")
    public int farmLevel;
    @DatabaseField(columnName = "mine_level")
    public int mineLevel;
    @DatabaseField(columnName = "crops_level")
    public int cropsLevel;
    @DatabaseField(columnName = "blocks_level")
    public int blocksLevel;
    @DatabaseField(columnName = "wall_level")
    public int wallLevel;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.cell = -1;
        this.cellLevel = 1;
        this.furnaceLevel = 1;
        this.farmLevel = 1;
        this.mineLevel = 1;
        this.cropsLevel = 1;
        this.blocksLevel = 1;
        this.wallLevel = 1;
    }

    public LocationRange getRange() {
        if (!hasCell()) {
            return null;
        }
        PluginLocation pos1 = Utils.getCellPasteLocation(cellAxis, cell);
        PluginLocation pos2 = Utils.getCellPasteLocation(cellAxis, cell - 2);
        pos2.y += Main.instance.config.cellSizeY;

        switch (cellAxis) {
            case "+X":
                pos2.z -= Main.instance.config.cellSizeZ;
                break;
            case "+Z":
                pos2.x -= Main.instance.config.cellSizeZ;
                break;
            case "-X":
                pos2.z += Main.instance.config.cellSizeZ;
                break;
            case "-Z":
                pos2.x += Main.instance.config.cellSizeZ;
                break;
        }

        LocationRange range = new LocationRange(pos1, pos2);
        System.out.println(range);
        return range;
    }

    public boolean hasCell() {
        return cell != -1;
    }

    public int getMaxLevel(String type) {
        switch (type) {
            case "furnace":
                return Main.instance.config.upgrades.get(cellLevel).furnace;
            case "farm":
                return Main.instance.config.upgrades.get(cellLevel).farm;
            case "mine":
                return Main.instance.config.upgrades.get(cellLevel).mine;
            case "crops":
                return Main.instance.config.upgrades.get(cellLevel).crops;
            case "blocks":
                return Main.instance.config.upgrades.get(cellLevel).blocks;
            case "cell":
                return Main.instance.config.upgrades.size();
            case "wall":
                return Main.instance.config.upgrades.get(cellLevel).wall;
        }
        return 0;
    }

    public int getLevel(String type) {
        switch (type) {
            case "furnace":
                return this.furnaceLevel;
            case "farm":
                return this.farmLevel;
            case "mine":
                return this.mineLevel;
            case "crops":
                return this.cropsLevel;
            case "blocks":
                return this.blocksLevel;
            case "cell":
                return this.cellLevel;
            case "wall":
                return this.wallLevel;
        }
        return 0;
    }

    public void setLevel(String type, int level) {
        switch (type) {
            case "furnace":
                this.furnaceLevel = level;
                break;
            case "farm":
                this.farmLevel = level;
                break;
            case "mine":
                this.mineLevel = level;
                break;
            case "crops":
                this.cropsLevel = level;
                break;
            case "blocks":
                this.blocksLevel = level;
                break;
            case "wall":
                this.wallLevel = level;
            case "cell":
                this.cellLevel = level;
                this.furnaceLevel = 1;
                this.farmLevel = 1;
                this.mineLevel = 1;
                this.cropsLevel = 1;
                this.blocksLevel = 1;
                break;
        }
        Main.instance.databaseManager.save(this);
    }

    public boolean upgrade(String type) {
        if (type.equals("cell")) {
            if (!(this.furnaceLevel == getMaxLevel("furnace") &&
                    this.farmLevel == getMaxLevel("farm") &&
                    this.cropsLevel == getMaxLevel("crops") &&
                    this.blocksLevel == getMaxLevel("blocks") &&
                    this.cropsLevel == getMaxLevel("crops")) &&
                    this.wallLevel == getMaxLevel("wall")) {
                return false;
            }
        }
        if (this.getLevel(type) != getMaxLevel(type)) {
            int newLevel = getLevel(type) + 1;
            setLevel(type, newLevel);
            Utils.upgradeCell(this, type, newLevel);
            return true;
        }

        return false;
    }

    public int getUpgradePrice(String type) {
        UpgradePath path;
        switch (type) {
            case "furnace":
                path = Main.instance.config.upgrades.get(cellLevel);
                if (path == null) {
                    return 0;
                }
                return path.furnacePrice;
            case "farm":
                path = Main.instance.config.upgrades.get(cellLevel);
                if (path == null) {
                    return 0;
                }
                return path.farmPrice;
            case "mine":
                path = Main.instance.config.upgrades.get(cellLevel);
                if (path == null) {
                    return 0;
                }
                return path.minePrice;
            case "crops":
                path = Main.instance.config.upgrades.get(cellLevel);
                if (path == null) {
                    return 0;
                }
                return path.cropsPrice;
            case "blocks":
                path = Main.instance.config.upgrades.get(cellLevel);
                if (path == null) {
                    return 0;
                }
                return path.blocksPrice;
            case "cell":
                path = Main.instance.config.upgrades.get(cellLevel);
                if (path == null) {
                    return 0;
                }
                return path.cellPrice;
            case "wall":
                path = Main.instance.config.upgrades.get(cellLevel);
                if (path == null) {
                    return 0;
                }
                return path.wallPrice;
        }
        return 0;
    }

    public PositionRange getMineOffset() {
        return Main.instance.config.mines.get(this.cellLevel).get(this.mineLevel).deepClone();
    }

    public List<User> getNeighbors() {
        User n1 = Main.instance.databaseManager.getUser(cellAxis, cell - 2);
        User n2 = Main.instance.databaseManager.getUser(cellAxis, cell + 2);

        List<User> neighbors = new ArrayList<>();
        if (n1 != null) {
            neighbors.add(n1);
        }
        if (n2 != null) {
            neighbors.add(n2);
        }
        return neighbors;
    }

}
