package dev.lightdream.chunkcells.guis;

import com.google.gson.JsonElement;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.gui.GUI;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.functions.GUIFunctions;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class ChoseAxisGUI extends GUI {

    public ChoseAxisGUI(LightDreamPlugin plugin) {
        super(plugin);
    }

    @Override
    public String parse(String raw, Player player) {
        return raw;
    }

    @Override
    public void setConfig() {
        config = Main.instance.config.choseAxisGUI;
    }

    @Override
    public InventoryProvider getProvider() {
        return new ChoseAxisGUI(plugin);
    }

    @Override
    public void functionCall(Player player, String function, JsonElement args) {
        User user = Main.instance.databaseManager.getUser(player);
        GUIFunctions.valueOf(function.toUpperCase()).function.execute(user, args);
    }

    @Override
    public boolean canAddItem(Item item, String key) {
        return true;
    }

}
