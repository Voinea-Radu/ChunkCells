package dev.lightdream.chunkcells.guis.functions.functions;

import com.google.gson.JsonElement;
import dev.lightdream.api.files.dto.GUIItem;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.functions.GUIFunction;
import dev.lightdream.chunkcells.utils.Utils;
import me.qalex.Cookies.CookiePlayer;
import me.qalex.Cookies.Cookies;

public class GenerateCell implements GUIFunction {
    @Override
    public void execute(User user, Object args) {
        CookiePlayer cPlayer = Cookies.getInstance().getCookiePlayer(user.uuid.toString());
        if (!cPlayer.hasCookies(Main.instance.config.cellGeneratePrice)) {
            Main.instance.getMessageManager().sendMessage(user, Main.instance.lang.notEnoughCookies);
            return;
        }


        Main.instance.getMessageManager().sendMessage(user, Main.instance.lang.generationRequestSent);
        cPlayer.removeCookies(Main.instance.config.cellGeneratePrice);
        Utils.generateCell((String) ((GUIItem.GUIItemArgs)args).getFunctionArgs("generate_cell"));
    }
}
