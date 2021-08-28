package dev.lightdream.chunkcells.managers;

import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventsManager implements Listener {

    private final Main plugin;

    public EventsManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        System.out.println("e1");
        User user = plugin.databaseManager.getUser(event.getPlayer());
        if (user.getPlayer().getLocation().getWorld().getName().equals(Main.instance.config.cellWorld)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        System.out.println("e2");
        User user = plugin.databaseManager.getUser(event.getPlayer());
        if (user.getPlayer().getLocation().getWorld().getName().equals(Main.instance.config.cellWorld)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        System.out.println("e3");
        User user = plugin.databaseManager.getUser(event.getPlayer());
        if (!user.getPlayer().getLocation().getWorld().getName().equals(Main.instance.config.cellWorld)) {
            System.out.println("not in world");
            return;
        }
        if (user.getRange() != null) {
            System.out.println("range is not null");
            if (user.getRange().check(user.getLocation())) {
                System.out.println("player is in range");
                return;
            }
        }
        System.out.println("event si canceled");
        event.setCancelled(true);
    }

}
