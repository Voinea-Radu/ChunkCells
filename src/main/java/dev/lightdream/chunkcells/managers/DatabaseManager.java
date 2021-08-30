package dev.lightdream.chunkcells.managers;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.chunkcells.database.User;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class DatabaseManager extends dev.lightdream.api.managers.DatabaseManager {
    @SneakyThrows
    public DatabaseManager(LightDreamPlugin plugin) {
        super(plugin);
        createTable(User.class);
        createDao(User.class).setAutoCommit(getDatabaseConnection(), false);
    }

    @SneakyThrows
    public @NotNull List<User> getUsers() {
        return (List<User>) getDao(User.class).queryForAll();
    }

    public @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = getUsers().stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        save(user);
        return user;
    }

    public @Nullable User getUser(@NotNull String name) {
        Optional<User> optionalUser = getUsers().stream().filter(user -> user.name.equals(name)).findFirst();

        return optionalUser.orElse(null);
    }

    public @NotNull User getUser(@NotNull OfflinePlayer player) {
        return getUser(player.getUniqueId());
    }
}