package dev.lightdream.chunkcells.files.config;

import dev.lightdream.api.files.dto.PluginLocation;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Config extends dev.lightdream.api.files.config.Config {

    public String cellWorld = "world";
    public PluginLocation cellCreateWaitingLocation = new PluginLocation("world", 0,5,0);
    public int cellPasteY = 100;
}
