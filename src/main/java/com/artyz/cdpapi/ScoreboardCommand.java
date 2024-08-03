package com.artyz.cdpapi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ScoreboardCommand implements CommandExecutor {

    private final CDPAPI main;

    public ScoreboardCommand(CDPAPI main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usage: /countdownscore <name>");
            return true;
        }

        String countdownName = args[0];
        String placeholder = "%countdown_" + countdownName + "%";
        main.getLogger().info("Using placeholder: " + placeholder);

        // Parse the placeholder to get the countdown value
        String countdownValue = PlaceholderAPI.setPlaceholders(player, "%countdown%");
        main.getLogger().info("Parsed placeholder value: " + countdownValue);

        try {
            int seconds = Integer.parseInt(countdownValue);
            player.sendMessage("Countdown " + countdownName + " is at " + seconds + " seconds.");
        } catch (NumberFormatException e) {
            player.sendMessage("Error parsing countdown value: " + countdownValue);
            main.getLogger().severe("Error parsing countdown value: " + countdownValue);
        }

        return true;
    }
}
