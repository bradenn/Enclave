package com.bradenn.dev.enclave.completers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class EnclaveCompleter implements TabCompleter {

    Map<String, List<String>> children = new HashMap<>();

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        children.put("claim", new ArrayList<>());
        children.put("unclaim", new ArrayList<>());
        children.put("invite", null);
        children.put("info", new ArrayList<>());
        children.put("disband", new ArrayList<>());
        children.put("create", null);

        List<String> colors = new ArrayList<>();
        new ArrayList<>(Arrays.asList(ChatColor.values())).forEach(chatColor -> {
            colors.add(chatColor.name());
        });
        children.put("color", colors);

        switch (args.length) {
            case 1:
                return new ArrayList<>(children.keySet());
            case 2:
                return children.get(args[0]);
            default:
                return new ArrayList<>();
        }

    }

}
