package dev.lightdream.chunkcells.files.dto;

import dev.lightdream.api.files.dto.PluginLocation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Position {

    public int x;
    public int y;
    public int z;

    public PluginLocation toPluginLocation(String world) {
        return new PluginLocation(world, x, y, z);
    }

    public Position clone() {
        return new Position(x, y, z);
    }

}
