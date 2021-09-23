package dev.lightdream.chunkcells;

import dev.lightdream.api.API;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.files.config.SQLConfig;
import dev.lightdream.api.managers.MessageManager;
import dev.lightdream.chunkcells.commands.*;
import dev.lightdream.chunkcells.files.config.Config;
import dev.lightdream.chunkcells.files.config.Lang;
import dev.lightdream.chunkcells.files.config.Saves;
import dev.lightdream.chunkcells.managers.DatabaseManager;
import dev.lightdream.chunkcells.managers.EventsManager;
import dev.lightdream.chunkcells.managers.ScheduleManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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

        System.out.println(getSQLConfig());
        System.out.println(sqlConfig);

        databaseManager = new DatabaseManager(this);
        eventsManager = new EventsManager(this);
        new ScheduleManager(this);
    }

    @Override
    public @NotNull String parsePapi(OfflinePlayer offlinePlayer, String s) {
        return "";
    }

    @Override
    public void loadConfigs() {
        sqlConfig= fileManager.load(SQLConfig.class);
        config = fileManager.load(Config.class);
        baseConfig = config;
        lang = fileManager.load(Lang.class, fileManager.getFile(baseConfig.baseLang));
        baseLang = lang;
        saves = fileManager.load(Saves.class);
    }

    @Override
    public void disable() {
        databaseManager.save();

    }

    @Override
    public void registerFileManagerModules() {

    }

    @Override
    public void loadBaseCommands() {
        baseSubCommands.add(new GenerateCell(this));
        baseSubCommands.add(new UpgradeCommand(this));
        baseSubCommands.add(new RegenerateMine(this));
        baseSubCommands.add(new GetRaidTool(this));
        baseSubCommands.add(new TpCommand(this));
        baseSubCommands.add(new AdminModeCommand(this));
        baseSubCommands.add(new FixWallsCommand(this));
        baseSubCommands.add(new RentCommand(this));
    }


    @Override
    public MessageManager instantiateMessageManager() {
        return new MessageManager(this, Main.class);
    }

    @Override
    public void registerLangManager() {
        API.instance.langManager.register(Main.class, getLangs());
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void setLang(Player player, String s) {
        User user = databaseManager.getUser(player);
        user.setLang(s);
        databaseManager.save(user);
    }

    @Override
    public HashMap<String, Object> getLangs() {
        HashMap<String, Object> langs = new HashMap<>();

        baseConfig.langs.forEach(lang -> {
            Lang l = fileManager.load(Lang.class, fileManager.getFile(lang));
            langs.put(lang, l);
        });

        return langs;
    }


}
