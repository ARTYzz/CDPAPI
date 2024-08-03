package com.artyz.cdpapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTab implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("create","start","stop"),new ArrayList<>());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("start")){
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("<countdown_name>"),new ArrayList<>());
            } else if (args[0].equalsIgnoreCase("stop")) {
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("<countdown_name>"),new ArrayList<>());
            } else if (args[0].equalsIgnoreCase("create")) {
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("<countdown_name>"),new ArrayList<>());
            }
        }  else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            return StringUtil.copyPartialMatches(args[2], Arrays.asList("<seconds>"),new ArrayList<>());
        }
        return new ArrayList<>();
    }
}
