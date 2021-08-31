package dev.lightdream.chunkcells.files.dto;


import dev.lightdream.api.files.dto.LocationRange;
import dev.lightdream.api.files.dto.PluginLocation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PositionRange {

    public Position pos1;
    public Position pos2;

    public LocationRange toLocationRange(String world) {
        return new LocationRange(new PluginLocation(world, pos1.x, pos1.y, pos1.z), new PluginLocation(world, pos2.x, pos2.y, pos2.z));
    }

    public PositionRange clone(){
        return new PositionRange(pos1, pos2);
    }

    public PositionRange deepClone(){
        return new PositionRange(pos1.clone(),pos2.clone());
    }

    @Override
    public String toString() {
        return "PositionRange{" +
                "pos1=" + pos1 +
                ", pos2=" + pos2 +
                '}';
    }
}
