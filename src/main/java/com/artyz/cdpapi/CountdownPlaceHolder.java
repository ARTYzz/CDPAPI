package com.artyz.cdpapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountdownPlaceHolder extends PlaceholderExpansion {

    private final CountdownManager countdownManager;
    private final Map<String, CountdownTask> placeholders = new HashMap<>();
    private CDPAPI main;

    public CountdownPlaceHolder(CDPAPI main) {
        this.countdownManager = CountdownManager.getInstance();
        this.main = main;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "countdown";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ARTYz";
    }

    @Override
    public @NotNull String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This expansion will be persistent and not need to be re-registered on reload
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String identifier) {
        main.getLogger().info("onPlaceholderRequest called with identifier: " + identifier);
        // %countdown_<name>%
        CountdownTask task = countdownManager.getCountdownTask(identifier);
        if (task != null) {
            if (identifier.equalsIgnoreCase(task.getName())) {
                main.getLogger().info("Found countdown task for: " + identifier + " with " + task.getCountdownSeconds() + " seconds remaining.");
                return String.valueOf(task.getCountdownSeconds());
            }
        }
        main.getLogger().info("No countdown found for: " + identifier);
        return "No countdown found";
    }

    public void addPlaceholder(String name, CountdownTask task) {
        placeholders.put(name, task);
    }
}



