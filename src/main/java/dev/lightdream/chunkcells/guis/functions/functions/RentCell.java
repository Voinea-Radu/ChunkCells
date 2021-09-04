package dev.lightdream.chunkcells.guis.functions.functions;

import com.google.gson.JsonElement;
import dev.lightdream.api.gui.GUI;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.functions.GUIFunction;
import dev.lightdream.chunkcells.utils.Utils;
import me.qalex.Cookies.CookiePlayer;
import me.qalex.Cookies.Cookies;

public class RentCell implements GUIFunction {

    @Override
    public void execute(User user, JsonElement args) {
        if(user.hasCell()){
            MessageUtils.sendMessage(user, Main.instance.lang.alreadyHaveCell);
            return;
        }

        CookiePlayer cPlayer = Cookies.getInstance().getCookiePlayer(user.uuid.toString());
        if(!cPlayer.hasCookies(Main.instance.config.cellRent)){
            MessageUtils.sendMessage(user, Main.instance.lang.notEnoughCookies);
            return;
        }

        cPlayer.removeCookies(Main.instance.config.cellRent);
        String cellArg = ((GUI) Main.instance.inventoryManager.getInventory(user.getPlayer()).get().getProvider()).parse(args.getAsString(), user.getPlayer());
        String axis = cellArg.split("_")[0];
        int cell = Integer.parseInt(cellArg.split("_")[1])-1;
        Utils.assignCell(user, axis, cell);
    }
}
