package dev.lightdream.chunkcells.guis.functions.functions;

import com.google.gson.JsonElement;
import dev.lightdream.api.files.dto.GUIItem;
import dev.lightdream.api.gui.GUI;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.guis.functions.GUIFunction;
import dev.lightdream.chunkcells.utils.Utils;
import me.qalex.Cookies.CookiePlayer;
import me.qalex.Cookies.Cookies;

public class RentCell implements GUIFunction {

    @Override
    public void execute(User user, Object args) {
        if (user.hasCell()) {
            Main.instance.getMessageManager().sendMessage(user, Main.instance.lang.alreadyHaveCell);
            return;
        }

        CookiePlayer cPlayer = Cookies.getInstance().getCookiePlayer(user.uuid.toString());
        if (!cPlayer.hasCookies(Main.instance.config.cellRent)) {
            Main.instance.getMessageManager().sendMessage(user, Main.instance.lang.notEnoughCookies);
            return;
        }

        cPlayer.removeCookies(Main.instance.config.cellRent);
        String cellArg = ((GUI) Main.instance.inventoryManager.getInventory(user.getPlayer()).get().getProvider()).parse((String) ((GUIItem.GUIItemArgs) args).getFunctionArgs("rent_cell"), user.getPlayer());
        String axis = cellArg.split("_")[0];
        int cell = Integer.parseInt(cellArg.split("_")[1]) - 1;
        Utils.assignCell(user, axis, cell);
    }
}
