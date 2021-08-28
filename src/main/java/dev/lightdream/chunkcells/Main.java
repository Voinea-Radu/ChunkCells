package dev.lightdream.chunkcells;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.utils.LangUtils;
import dev.lightdream.chunkcells.commands.GenerateCell;
import dev.lightdream.chunkcells.files.config.Config;
import dev.lightdream.chunkcells.files.config.Lang;
import dev.lightdream.chunkcells.files.config.Saves;
import dev.lightdream.chunkcells.managers.DatabaseManager;
import dev.lightdream.chunkcells.managers.EventsManager;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class Main extends LightDreamPlugin {

    public static final int CELL_X_SIZE = 47;
    public static final int CELL_Y_SIZE = 53;
    public static final int CELL_Z_SIZE = 49;

    public static Main instance;

    //Settings
    public Config config;
    public Lang lang;
    public Saves saves;

    //Managers
    public DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        init("ChunkCells", "cc", "1.0");
        instance = this;

        baseCommands.add(new GenerateCell(this));

        databaseManager = new DatabaseManager(this);
        new EventsManager(this);
    }

    @Override
    public void onDisable(){
        fileManager.save(saves);
    }

    @Override
    public @NotNull String parsePapi(OfflinePlayer offlinePlayer, String s) {
        return "";
    }

    @Override
    public void loadConfigs() {
        config = fileManager.load(Config.class);
        baseConfig = config;
        lang = (Lang) fileManager.load(LangUtils.getLang(Main.class, config.lang));
        baseLang = lang;
        saves = fileManager.load(Saves.class);
    }

    @Override
    public void loadBaseCommands() {
    }


}
