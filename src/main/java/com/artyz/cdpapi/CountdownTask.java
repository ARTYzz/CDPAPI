package com.artyz.cdpapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class CountdownTask extends BukkitRunnable {

    private final CDPAPI main;
    private final String name;
    private int countdownSeconds;
    private final int initialSeconds;
    private boolean isRunning;
    private boolean finished;

    public CountdownTask(CDPAPI main, String name, int countdownSeconds) {
        this.main = main;
        this.name = name;
        this.countdownSeconds = countdownSeconds;
        this.initialSeconds = countdownSeconds;
        this.isRunning = false;
        this.finished = false;
    }

    @Override
    public void run() {
        if (countdownSeconds <= 0) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "Countdown " + name + " finished!");
            CountdownManager.getInstance().finishCountdown(name);
            this.isRunning = false;
            this.finished = true;
            this.cancel();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(String.valueOf(countdownSeconds));
        }

        countdownSeconds--;
    }

    public void start() {
        if (isRunning) {
            throw new IllegalStateException("Countdown already running.");
        }
        Logger.getLogger("DEBUG").warning("Attempting to start countdown: " + name);
        runTaskTimer(main, 0, 20); // Run every second (20 ticks)
        isRunning = true; // Update state to running
        Logger.getLogger("DEBUG").warning("Countdown " + name + " timer started.");
    }

    public void stop() {
        if (isRunning) {
            this.cancel();
            isRunning = false;
            main.getLogger().info(name + " countdown has been stopped.");
        } else {
            main.getLogger().info(name + " countdown was not running.");
        }
    }

    public String getName() {
        return name;
    }

    public int getCountdownSeconds() {
        return countdownSeconds;
    }

    public void reset() {
        if (!isRunning) { // Ensure it is only reset if not running
            this.countdownSeconds = initialSeconds; // Reset to initial seconds
            this.finished = false;
            main.getLogger().info(name + " countdown has been reset.");
        } else {
            main.getLogger().warning(name + " countdown is still running and cannot be reset.");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
    public boolean isFinished() {
        return finished;
    }
}
