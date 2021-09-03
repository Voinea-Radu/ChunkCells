package dev.lightdream.chunkcells.guis.functions.functions;

import com.google.gson.JsonElement;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.functions.GUIFunction;
import dev.lightdream.chunkcells.utils.Utils;
import me.qalex.Cookies.CookiePlayer;
import me.qalex.Cookies.Cookies;

public class GenerateCell implements GUIFunction {
    @Override
    public void execute(User user, JsonElement args) {
        CookiePlayer cPlayer = Cookies.getInstance().getCookiePlayer(user.uuid.toString());
        if(!cPlayer.hasCookies(Main.instance.config.cellGeneratePrice)){
            MessageUtils.sendMessage(user, Main.instance.lang.notEnoughCookies);
            return;
        }


        MessageUtils.sendMessage(user, Main.instance.lang.generationRequestSent);
        cPlayer.removeCookies(Main.instance.config.cellGeneratePrice);
        Utils.generateCell(args.getAsString());
    }
}
