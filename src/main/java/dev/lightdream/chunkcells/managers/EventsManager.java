package dev.lightdream.chunkcells.managers;

import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.files.dto.PositionRange;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        User user = plugin.databaseManager.getUser(event.getPlayer());
        PositionRange range = user.getMineRange();
        range.pos1.y += Main.instance.config.cellPasteY;
        range.pos2.y += Main.instance.config.cellPasteY;
        if (user.cellZ == 0) {
            int x = (user.cellX + 1) * Main.CELL_X_SIZE;
            int z = Main.CELL_Z_SIZE;
            range.pos1.x = x - range.pos1.x;
            range.pos2.x = x - range.pos2.x;
            range.pos1.z = z - range.pos1.z;
            range.pos2.z = z - range.pos2.z;
        } else {
            int x = user.cellX * Main.CELL_X_SIZE;
            int z = user.cellX * Main.CELL_X_SIZE;
            range.pos1.x = x + range.pos1.x;
            range.pos2.x = x + range.pos2.x;
            range.pos1.z = z + range.pos1.z;
            range.pos2.z = z + range.pos2.z;
        }

        if (range.toLocationRange(plugin.config.cellWorld).check(new PluginLocation(event.getBlock().getLocation()))) {
            return;
        }
        if (user.getPlayer().getLocation().getWorld().getName().equals(Main.instance.config.cellWorld)) {
            event.setCancelled(true);
        }
        //todo raids
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
