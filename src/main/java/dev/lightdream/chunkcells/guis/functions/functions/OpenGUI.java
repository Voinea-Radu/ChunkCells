package dev.lightdream.chunkcells.guis.functions.functions;

import com.google.gson.JsonElement;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.ChoseAxisGUI;
import dev.lightdream.chunkcells.guis.RentGUI;
import dev.lightdream.chunkcells.guis.UpgradeGUI;
import dev.lightdream.chunkcells.guis.functions.GUIFunction;

import java.util.Locale;

public class OpenGUI implements GUIFunction {
    @Override
    public void execute(User user, JsonElement args) {
        String gui = args.getAsString();

        if(gui.contains("rent_")){
            gui = gui.replace("rent_", "");
            new RentGUI(Main.instance, gui.toUpperCase(), 0).getInventory().open(user.getPlayer());
            return;
        }

        switch (gui){
            case "chose_axis":
                new ChoseAxisGUI(Main.instance).getInventory().open(user.getPlayer());
                break;
            case "upgrade":
                UpgradeGUI.getInventory().open(user.getPlayer());
                break;
        }



    }
}
