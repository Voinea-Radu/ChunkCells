package dev.lightdream.chunkcells.guis.functions;

import dev.lightdream.chunkcells.guis.functions.functions.GenerateCell;
import dev.lightdream.chunkcells.guis.functions.functions.OpenGUI;
import dev.lightdream.chunkcells.guis.functions.functions.RentCell;
import dev.lightdream.chunkcells.guis.functions.functions.Unrent;

public enum GUIFunctions {

    OPEN_GUI(new OpenGUI()),
    RENT_CELL(new RentCell()),
    GENERATE_CELL(new GenerateCell()),
    UNRENT_CELL(new Unrent());

    public GUIFunction function;

    GUIFunctions(GUIFunction function) {
        this.function = function;
    }
}
