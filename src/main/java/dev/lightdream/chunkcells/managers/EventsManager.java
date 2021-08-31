package dev.lightdream.chunkcells.managers;

import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.api.files.dto.XMaterial;
import dev.lightdream.api.utils.MessageUtils;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class EventsManager implements Listener {

    private final Main plugin;
    private final HashMap<User, User> raidMode = new HashMap<>();
    public List<UUID> adminMode = new ArrayList<>();

    public EventsManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        User user = plugin.databaseManager.getUser(event.getPlayer());
        if (adminMode.contains(user.uuid)) {
            return;
        }
        Block block = event.getBlock();

        if (Utils.getCellMine(user).toLocationRange(plugin.config.cellWorld).check(new PluginLocation(block.getLocation()))) {
            return;
        }
        boolean isWall = false;
        for (XMaterial material : Main.instance.config.wallMaterials.get(user.wallLevel)) {
            if (block.getType().equals(material.parseMaterial()) && block.getData() == material.getData()) {
                isWall = true;
                break;
            }
        }
        if (isWall) {
            ItemStack item = user.getPlayer().getItemInHand();
            if (item.hasItemMeta()) {
                if (item.getItemMeta().getLore().equals(Main.instance.config.raidToolLore)) {
                    item.setDurability((short) (item.getDurability() + Main.instance.config.raidDurabilityUse * user.wallLevel));
                    user.getPlayer().updateInventory();
                    return;
                }
            }
        }
        if (event.getBlock().getWorld().getName().equals(Main.instance.config.cellWorld)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        User user = plugin.databaseManager.getUser(event.getPlayer());
        if (adminMode.contains(user.uuid)) {
            return;
        }
        if (event.getBlock().getWorld().getName().equals(Main.instance.config.cellWorld)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        User user = plugin.databaseManager.getUser(event.getPlayer());
        if (adminMode.contains(user.uuid)) {
            return;
        }
        if (!user.getPlayer().getLocation().getWorld().getName().equals(Main.instance.config.cellWorld)) {
            return;
        }
        if (user.getRange() != null) {
            if (user.getRange().check(user.getLocation())) {
                return;
            }
        }
        if (raidMode.containsKey(user)) {
            return;
        }
        List<User> neighbors = user.getNeighbors();
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            for (User neighbor : neighbors) {
                if (!neighbor.getRange().check(new PluginLocation(event.getClickedBlock().getLocation()))) {
                    continue;
                }
                if (raidMode.containsKey(user)) {
                    return;
                }
                raidMode.put(user, neighbor);
                if (neighbor.isOnline()) {
                    MessageUtils.sendMessage(neighbor, Main.instance.lang.cellRaided);
                }
                MessageUtils.sendMessage(user, Main.instance.lang.raidStarted);
                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                    MessageUtils.sendMessage(user, Main.instance.lang.raidEnded);
                    raidMode.remove(user);
                    Utils.fixWalls(user);
                    Utils.fixWalls(neighbor);
                }, Main.instance.config.raidDuration * 20L);
                return;

            }
        }
        event.setCancelled(true);
    }

}
