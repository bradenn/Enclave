package com.bradenn.dev.enclave.completers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnclaveCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> tab = new ArrayList<>();
            tab.add("claim");
            tab.add("unclaim");
            tab.add("info");
            tab.add("disband");
            tab.add("create");
            tab.add("color");
            return tab;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                return null;
            } else if (args[0].equalsIgnoreCase("color")) {
                List<String> tab = new ArrayList<>();
                for(ChatColor c : ChatColor.values()){
                    tab.add(c.name());
                }
                return tab;
            }
        }
        return null;
    }

}
