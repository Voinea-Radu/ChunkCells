package dev.lightdream.chunkcells.database;

import com.j256.ormlite.field.DatabaseField;
import dev.lightdream.api.files.dto.LocationRange;
import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.chunkcells.Main;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class User extends dev.lightdream.api.databases.User {

    @DatabaseField(columnName = "cell_range")
    public int cellX;
    @DatabaseField(columnName = "cell_y")
    public int cellZ;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.cellX = -1;
        this.cellZ = -1;
    }

    public LocationRange getRange() {
        if (!hasCell()) {
            return null;
        }
        return new LocationRange(new PluginLocation(Main.instance.config.cellWorld, cellX * Main.CELL_X_SIZE, Main.instance.config.cellPasteY, cellZ * Main.CELL_Z_SIZE),
                new PluginLocation(Main.instance.config.cellWorld, (cellX + 1) * Main.CELL_X_SIZE - 1, Main.instance.config.cellPasteY + Main.CELL_Y_SIZE, (cellZ + 1) * Main.CELL_Z_SIZE - 1));
    }

    public boolean hasCell(){
        return cellX != -1 && cellZ != -1;
    }

}
