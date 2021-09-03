package dev.lightdream.chunkcells.guis.functions.functions;

import com.google.gson.JsonElement;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.functions.GUIFunction;

public class Unrent implements GUIFunction {
    @Override
    public void execute(User user, JsonElement args) {
        if(user.hasCell()){
            MessageUtils.sendMessage(user, Main.instance.lang.dontHaveCell);
            return;
        }

        user.unrent();
        MessageUtils.sendMessage(user, Main.instance.lang.cellUnrented);
    }
}
