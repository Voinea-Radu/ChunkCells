package dev.lightdream.chunkcells.guis;

import dev.lightdream.api.files.dto.GUIConfig;
import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.utils.ItemBuilder;
import dev.lightdream.chunkcells.Main;
import dev.lightdream.chunkcells.database.User;
import dev.lightdream.chunkcells.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.qalex.Cookies.CookiePlayer;
import me.qalex.Cookies.Cookies;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class UpgradeGUI implements InventoryProvider {

    private static GUIConfig config;

    public static SmartInventory getInventory() {
        config = Main.instance.config.upgradeGUI;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new UpgradeGUI())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .manager(Main.instance.inventoryManager)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));
        User user = Main.instance.databaseManager.getUser(player);

        config.items.forEach((key, value) -> {
            Item builder = value.item.clone();
            String type = key.replace("upgrade_", "");
            builder.displayName = parse(builder.displayName, user, type);
            builder.lore = parse(builder.lore, user, type);
            contents.set(Utils.getSlotPosition(value.item.slot), ClickableItem.of(ItemBuilder.makeItem(builder), e -> {
                int price = user.getUpgradePrice(type);
                if (price == 0) {
                    return;
                }
                CookiePlayer cPlayer = Cookies.getInstance().getCookiePlayer(player.getUniqueId().toString());
                if (!cPlayer.hasCookies(price)) {
                    Main.instance.getMessageManager().sendMessage(player, Main.instance.lang.notEnoughCookies);
                    return;
                }
                if (!user.upgrade(type)) {
                    Main.instance.getMessageManager().sendMessage(player, Main.instance.lang.cannotUpgrade);
                    return;
                }
                cPlayer.removeCookies(price);
            }));
        });


    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public String parse(String raw, User user, String type) {
        String parsed = raw;

        parsed = parsed.replace("%current_level%", String.valueOf(user.getLevel(type)));
        parsed = parsed.replace("%max_level%", String.valueOf(user.getMaxLevel(type)));
        parsed = parsed.replace("%price%", String.valueOf(user.getUpgradePrice(type)));

        return parsed;
    }

    public List<String> parse(List<String> raw, User user, String type) {
        List<String> output = new ArrayList<>();

        raw.forEach(line -> output.add(parse(line, user, type)));

        return output;
    }
}
