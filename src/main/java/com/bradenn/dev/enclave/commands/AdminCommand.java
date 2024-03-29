package com.bradenn.dev.enclave.commands;

import com.bradenn.dev.enclave.managers.AdminManager;
import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.messages.Response;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String s, String[] args) {
        Player player = (Player) commandSender;
        if(!player.hasPermission("enclave.admin")){
            MessageUtils.send(player, Response.E_INSUFFICIENT_PERMISSION);
        }
        if (command.getName().equalsIgnoreCase("enclaveadmin")) {
            AdminManager adminManager = new AdminManager(player);
            switch (args.length) {
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "help":
                            adminManager.sendHelp();
                            break;
                        case "wipe":
                            adminManager.wipeChunk();
                            break;
                        case "info":
                            adminManager.getInfo();
                            break;
                        case "border":
                            adminManager.showBorder();
                            break;
                        case "identifier":
                            adminManager.showIdentifier();
                            break;
                        case "version":
                            adminManager.showVersion();
                            break;
                        case "cleanup":
                            adminManager.cleanupRuntime();
                            break;
                        default:
                            adminManager.sendUnknown();
                            break;
                    }
                    break;
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "disband":
                            adminManager.disbandEnclave(args[1]);
                            break;
                        case "kick":
                            adminManager.kickPlayer(args[1]);
                            break;
                        default:
                            adminManager.sendUnknown();
                            break;
                    }
                    break;
                case 3:
                    switch (args[0].toLowerCase()) {
                        case "merge":
                            // /ea merge [primary] [toMerge]
                            // Merge two enclaves
                            break;
                        case "color":
                            adminManager.setColor(args[1], args[2]);
                            // /ea color [player] [color]
                            // Change the color of an enclave
                            break;
                        default:
                            adminManager.sendUnknown();
                            break;
                    }
                    break;
                default:
                    adminManager.sendUnknown();
                    break;
            }
        }
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                      @NotNull String s, String[] args) {

        Map<String, List<String>> commands = new HashMap<>();

        commands.put("help", new ArrayList<>());
        commands.put("wipe", new ArrayList<>());
        commands.put("info", new ArrayList<>());
        commands.put("border", new ArrayList<>());
        commands.put("identifier", new ArrayList<>());
        commands.put("version", new ArrayList<>());
        commands.put("cleanup", new ArrayList<>());

        commands.put("disband", null);
        commands.put("kick", null);

        commands.put("merge", new ArrayList<>());
        commands.put("color", new ArrayList<>());

        switch (args.length) {
            case 1:
                return new ArrayList<>(commands.keySet());
            case 2:
                return commands.get(args[0]);
            default:
                return new ArrayList<>();
        }
    }


    public <T extends Enum<T>> List<String> enumList(T[] object) {
        List<String> items = new ArrayList<>();
        for (T t : object) items.add(t.name());
        return items;
    }
}
