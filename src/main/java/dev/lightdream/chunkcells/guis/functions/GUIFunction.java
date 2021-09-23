package dev.lightdream.chunkcells.guis.functions;

import com.google.gson.JsonElement;
import dev.lightdream.chunkcells.database.User;

public interface GUIFunction {

    void execute(User user, Object args);

}
