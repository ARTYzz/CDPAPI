package com.artyz.cdpapi;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CDPAPI extends JavaPlugin {

    private CountdownManager countdownManager;
    private CountdownPlaceHolder placeholderExpansion;

    @Override
    public void onEnable() {

        countdownManager = new CountdownManager(this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CountdownPlaceHolder(this).register();
        }

        getCommand("countdown").setExecutor(new Command(this));
        getCommand("countdownscore").setExecutor(new ScoreboardCommand(this));
        getCommand("countdown").setTabCompleter(new CommandTab());

        new Data(this);

        Data.reloadConfigdata();
        getLogger().info("Plugin Enabled");
    }

    @Override
    public void onDisable() {
        CountdownManager.getInstance().stopAllCountdowns();
    }

    public CountdownManager getCountdownManager() {
        return countdownManager;
    }

    public CountdownPlaceHolder  getPlaceholderManager() {
        return placeholderExpansion;
    }


}
