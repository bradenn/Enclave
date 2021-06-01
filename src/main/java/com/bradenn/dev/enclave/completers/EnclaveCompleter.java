package com.bradenn.dev.enclave.completers;

import com.bradenn.dev.enclave.models.EnclaveTag;
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
        children.put("map", new ArrayList<>());
        children.put("here", new ArrayList<>());
        children.put("disband", new ArrayList<>());
        children.put("create", null);
        children.put("tags", null);

        List<String> colors = new ArrayList<>();
        new ArrayList<>(Arrays.asList(ChatColor.values())).forEach(chatColor -> {
            colors.add(chatColor.name());
        });
        children.put("color", colors);

        List<String> tags = new ArrayList<>();
        new ArrayList<>(Arrays.asList(EnclaveTag.values())).forEach(tag -> {
            tags.add(tag.name());
        });
        children.put("tag", tags);

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
