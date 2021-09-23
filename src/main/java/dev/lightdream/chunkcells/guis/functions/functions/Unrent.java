package dev.lightdream.chunkcells.guis.functions.functions;

import com.google.gson.JsonElement;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.functions.GUIFunction;

public class Unrent implements GUIFunction {
    @Override
    public void execute(User user, Object args) {
        if (!user.hasCell()) {
            Main.instance.getMessageManager().sendMessage(user, Main.instance.lang.dontHaveCell);
            return;
        }

        user.unrent();
        Main.instance.getMessageManager().sendMessage(user, Main.instance.lang.cellUnrented);
    }
}
