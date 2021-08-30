package dev.lightdream.chunkcells.database;

import com.j256.ormlite.field.DatabaseField;
import dev.lightdream.api.files.dto.LocationRange;
import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.files.dto.PositionRange;
import dev.lightdream.chunkcells.files.dto.UpgradePath;
import dev.lightdream.chunkcells.utils.Utils;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class User extends dev.lightdream.api.databases.User {

    @DatabaseField(columnName = "cell_range")
    public int cellX;
    @DatabaseField(columnName = "cell_y")
    public int cellZ;
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

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.cellX = -1;
        this.cellZ = -1;
        this.cellLevel = 1;
        this.furnaceLevel = 1;
        this.farmLevel = 1;
        this.mineLevel = 1;
        this.cropsLevel = 1;
        this.blocksLevel = 1;
    }

    public LocationRange getRange() {
        if (!hasCell()) {
            return null;
        }
        return new LocationRange(new PluginLocation(Main.instance.config.cellWorld, cellX * Main.CELL_X_SIZE, Main.instance.config.cellPasteY, cellZ * Main.CELL_Z_SIZE),
                new PluginLocation(Main.instance.config.cellWorld, (cellX + 1) * Main.CELL_X_SIZE - 1, Main.instance.config.cellPasteY + Main.CELL_Y_SIZE, (cellZ + 1) * Main.CELL_Z_SIZE - 1));
    }

    public boolean hasCell() {
        return cellX != -1 && cellZ != -1;
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
                    this.cropsLevel == getMaxLevel("crops"))) {
                return false;
            }
        }
        if (this.getLevel(type) != getMaxLevel(type)) {
            int newLevel = getLevel(type) + 1;
            setLevel(type, newLevel);
            Utils.upgradeCell(getPlayer(), cellLevel, type, newLevel);
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
        }
        return 0;
    }

    public PositionRange getMineRange(){
        return Main.instance.config.mines.get(this.cellLevel).get(this.mineLevel).deepClone();
    }

}
