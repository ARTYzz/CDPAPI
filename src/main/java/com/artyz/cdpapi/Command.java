package com.artyz.cdpapi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class Command implements @Nullable CommandExecutor {

    private final CountdownManager countdownManager;

    public Command(CDPAPI main) {
        this.countdownManager = new CountdownManager(main);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String arg, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 2) {  // Changed from arg.length() to args.length
                sender.sendMessage("Usage: /countdown <create/start/stop> <name> [seconds]");
                return false;
            }

            String action = args[0];
            String name = args[1];

            if (action.equalsIgnoreCase("create")) {
                String path = "Countdown.";
                if (args.length < 3) {
                    sender.sendMessage("Usage: /countdown create <name> <seconds>");
                    return false;
                }
                int seconds;
                try {
                    seconds = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid number of seconds.");
                    return false;
                }

                int index = 1;
                while (Data.getConfig().contains(path + index)) {
                    index++;
                }

                ConfigurationSection section = Data.getConfig().getConfigurationSection("Countdown");
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        if (Data.getConfig().getString("Countdown." + key + ".Name").equalsIgnoreCase(name)) {
                            sender.sendMessage("This Name already exists.");
                            return false; // Return false if the name already exists
                        }
                    }
                }

                String newPath = path + index;
                Data.getConfig().set(newPath + ".Name", name);
                Data.getConfig().set(newPath + ".Seconds", seconds);
                countdownManager.createCountdown(name);
                countdownManager.sendPlaceholder(player,name);

                Data.save();
                sender.sendMessage("Countdown " + name + " created with duration " + seconds + " seconds.");
            } else if (action.equalsIgnoreCase("start")) {
                try {
                    if (countdownManager.isCountdownRunning(name)) {
                        Logger.getLogger("DEBUG").warning("Running state: true");
                        sender.sendMessage("Countdown with this name is already running.");
                    } else {
                        countdownManager.startCountdown(name);
                        player.sendMessage(PlaceholderAPI.setPlaceholders(player,"Countdown now is %countdown_"+name+"%"));
                        Logger.getLogger("DEBUG").warning("Running state: false");
                        sender.sendMessage("Countdown " + name + " started.");
                    }
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("No countdown with this name exists.");
                } catch (IllegalStateException e) {
                    sender.sendMessage("Countdown with this name is already running.");
                }
            } else if (action.equalsIgnoreCase("stop")) {
                sender.sendMessage("Countdown " + name + " stopped.");
                countdownManager.stopCountdown(name);
            } else if (action.equalsIgnoreCase("reload")) {
                Data.reload();
                sender.sendMessage(ChatColor.GOLD + "[Countdown]" + ChatColor.WHITE + "Config Reload !!");
            } else {
                sender.sendMessage("Unknown action. Use /countdown <create/start/stop> <name> [seconds]");
            }

            return true;
        }
        return false;
    }
}
