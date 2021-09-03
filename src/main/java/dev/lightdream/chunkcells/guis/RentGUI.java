package dev.lightdream.chunkcells.guis;

import com.google.gson.JsonElement;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.gui.GUI;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.guis.functions.GUIFunctions;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class RentGUI extends GUI {

    private final String axis;
    private final int page;
    private final int perPage;
    private int number;

    public RentGUI(LightDreamPlugin plugin, String axis, int page) {
        super(plugin);
        this.axis = axis;
        this.page = page;
        int perPage = 0;
        for (String key : config.items.keySet()) {
            if (key.contains("slot")) {
                perPage++;
            }
        }
        this.perPage = perPage;
        number = perPage * page;
    }

    @Override
    public String parse(String raw, Player player) {
        String parsed = raw;

        parsed = parsed.replace("%number%", String.valueOf(number));
        parsed = parsed.replace("%free%", String.valueOf(Main.instance.databaseManager.getUser(axis, number - 1) == null));
        parsed = parsed.replace("%axis%", axis);
        parsed = parsed.replace("%last_cell%", String.valueOf(Main.instance.saves.axis.get(axis) - 1));

        return parsed;
    }

    @Override
    public void setConfig() {
        config = Main.instance.config.rentGUI;
    }

    @Override
    public InventoryProvider getProvider() {
        return new RentGUI(plugin, axis, page);
    }

    @Override
    public void functionCall(Player player, String function, JsonElement args) {
        if (function.equals("back_page")) {
            new RentGUI(plugin, axis, page - 1).getInventory().open(player);
            return;
        }
        if (function.equals("next_page")) {
            new RentGUI(plugin, axis, page + 1).getInventory().open(player);
            return;
        }
        System.out.println(function + " -> " + args);
        GUIFunctions.valueOf(function.toUpperCase()).function.execute(Main.instance.databaseManager.getUser(player), args);
    }

    @Override
    public boolean canAddItem(Item item, String key) {
        System.out.println(item);
        if (key.equals("next")) {
            int foo = Main.instance.saves.axis.get(axis) - 1;
            int pages = foo / perPage;
            return page != pages;
        }
        if (key.equals("back")) {
            return page != 0;
        }
        if (key.contains("slot_")) {
            if (number < Main.instance.saves.axis.get(axis)) {
                number++;
                return true;
            }
            return false;
        }
        return true;
    }
}
