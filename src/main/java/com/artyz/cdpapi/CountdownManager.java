package com.artyz.cdpapi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CountdownManager {

    private final CDPAPI main;
    private final Map<String, CountdownTask> countdowns;
    private static CountdownManager instance;
    private final Set<CountdownTask> runningCountdowns;

    public CountdownManager(CDPAPI main) {
        this.main = main;
        this.countdowns = new HashMap<>();
        this.runningCountdowns = new HashSet<>();
        instance = this;

        main.getLogger().info("CountdownManager instance created: " + this.hashCode());
    }

    public void createCountdown(String name) {
        main.getLogger().info("Creating countdown: " + name);
        if (countdowns.containsKey(name)) {
            main.getLogger().severe("A countdown with this name already exists: " + name);
            throw new IllegalArgumentException("A countdown with this name already exists.");
        }

        ConfigurationSection section = Data.getConfig().getConfigurationSection("Countdown");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                if (Data.getConfig().getString("Countdown." + key + ".Name").equalsIgnoreCase(name)) {
                    int seconds = Data.getConfig().getInt("Countdown." + key + ".Seconds");
                    CountdownTask countdownTask = new CountdownTask(main, name, seconds);
                    countdowns.put(name, countdownTask);
                    main.getLogger().info("Countdown " + name + " created with duration " + seconds + " seconds.");
                    main.getLogger().info("Current countdowns: " + countdowns);

                    CountdownTask task = countdowns.get(name);
                    main.getLogger().info("HashCode of existing task: " + (task != null ? task.hashCode() : "null"));
                    return;
                }
            }
        }
        main.getLogger().warning("No configuration found for countdown " + name);
    }

    public void startCountdown(String name) {
        CountdownTask task = countdowns.get(name);
        if (task == null) {
            throw new IllegalArgumentException("No countdown with this name exists.");
        }
        if (task.isRunning()) {
            throw new IllegalStateException("Countdown with this name is already running.");
        }

        // If the task is not running but was finished previously, create a new instance
        if (task.isFinished()) {
            main.getLogger().warning("Countdown " + name + " has finished previously. Creating a new instance.");
            ConfigurationSection section = Data.getConfig().getConfigurationSection("Countdown");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    if (Data.getConfig().getString("Countdown." + key + ".Name").equalsIgnoreCase(name)) {
                        int seconds = Data.getConfig().getInt("Countdown." + key + ".Seconds");
                        task = new CountdownTask(main, name, seconds);
                        countdowns.put(name, task);
                        main.getLogger().info("New instance of countdown " + name + " created with duration " + seconds + " seconds.");
                        main.getLogger().info("Add Task to list: " + countdowns);
                        main.getLogger().info("HashCode of new task: " + task.hashCode());
                        break;
                    }
                }
            }
        }

        task.start();
        main.getLogger().info("Countdown " + name + " started.");
    }

    public void finishCountdown(String name) {
        CountdownTask task = countdowns.get(name);
        if (task != null) {
            task.stop();
            runningCountdowns.remove(task);
            task.reset(); // Reset the countdown for reuse
            main.getLogger().info("Countdown " + name + " finished and reset.");
            main.getLogger().info("Running Countdown: " + runningCountdowns);
        }
    }

    public void stopCountdown(String name) {
        CountdownTask task = countdowns.get(name);
        if (task != null) {
            task.stop();
            runningCountdowns.remove(task);

        }
    }

    public void stopAllCountdowns() {
        for (CountdownTask task : runningCountdowns) {
            task.cancel();
        }
        runningCountdowns.clear();
    }

    public boolean isCountdownRunning(String name) {
        CountdownTask task = countdowns.get(name);
        return task != null && task.isRunning();
    }

    public CountdownTask getCountdownTask(String name) {
        main.getLogger().info("Getting countdown task for: " + name);
        main.getLogger().info("Countdown available: " + countdowns);
        CountdownTask task = countdowns.get(name);
        main.getLogger().info("HashCode of retrieved task: " + (task != null ? task.hashCode() : "null"));
        if (task == null) {
            main.getLogger().warning("Countdown task not found for: " + name);
        }
        return task;
    }
    public void sendPlaceholder(Player player, String name) {
        String placeholder = "%countdown_" + name + "%";
        player.sendMessage("Placeholder for " + name + " Countdown: " + placeholder);
    }

    public static CountdownManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CountdownManager instance is not initialized.");
        }
        return instance;
    }
}
