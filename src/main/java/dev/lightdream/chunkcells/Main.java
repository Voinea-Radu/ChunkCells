package dev.lightdream.chunkcells;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.utils.LangUtils;
import dev.lightdream.chunkcells.commands.*;
import dev.lightdream.chunkcells.files.config.Config;
import dev.lightdream.chunkcells.files.config.Lang;
import dev.lightdream.chunkcells.files.config.Saves;
import dev.lightdream.chunkcells.managers.DatabaseManager;
import dev.lightdream.chunkcells.managers.EventsManager;
import dev.lightdream.chunkcells.managers.ScheduleManager;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class Main extends LightDreamPlugin {

    public static Main instance;

    //Settings
    public Config config;
    public Lang lang;
    public Saves saves;

    //Managers
    public DatabaseManager databaseManager;
    public EventsManager eventsManager;

    @Override
    public void onEnable() {
        init("ChunkCells", "cc", "1.0");
        instance = this;

        baseCommands.add(new GenerateCell(this));
        baseCommands.add(new UpgradeCommand(this));
        baseCommands.add(new RegenerateMine(this));
        baseCommands.add(new GetRaidTool(this));
        baseCommands.add(new TpCommand(this));
        baseCommands.add(new AdminModeCommand(this));
        baseCommands.add(new FixWalls(this));

        databaseManager = new DatabaseManager(this);
        eventsManager = new EventsManager(this);
        new ScheduleManager(this);
    }

    @Override
    public void onDisable() {
        fileManager.save(saves);

        databaseManager.save();
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
