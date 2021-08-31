package dev.lightdream.chunkcells.managers;

import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.utils.Utils;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleManager {

    private final Main plugin;

    public ScheduleManager(Main plugin) {
        this.plugin = plugin;
        registerMineRefill();
    }

    public void registerMineRefill() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            MessageUtils.broadcast(plugin.lang.startedMineRefill);
            AtomicInteger delay = new AtomicInteger();
            plugin.databaseManager.getUsers().forEach(user -> {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    //delay.addAndGet(Utils.mineRefill(user));
                }, delay.get());
            });
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                MessageUtils.broadcast(plugin.lang.finishedMineRefill);
            }, delay.get());
        }, 0, Main.instance.config.mineRefillInterval * 20L);
    }

}
