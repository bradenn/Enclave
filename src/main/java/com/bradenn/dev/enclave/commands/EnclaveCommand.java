package com.bradenn.dev.enclave.commands;

import com.bradenn.dev.enclave.managers.EnclaveManager;
import com.bradenn.dev.enclave.models.Tag;
import org.bukkit.ChatColor;
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

public class EnclaveCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Enclave commands are only available for players.");
            return true;
        }
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("enclave")) {
            EnclaveManager enclaveManager = new EnclaveManager(player);
            if (!enclaveManager.isValid()) {
                switch (args.length) {
                    case 1:
                        switch (args[0].toLowerCase()) {
                            case "here":
                                enclaveManager.getInfo();
                                break;
                            case "join":
                                enclaveManager.joinEnclave();
                                break;
                            case "borders":
                                enclaveManager.showBorders();
                                break;
                            default:
                                enclaveManager.sendNoobHelp();
                                break;
                        }
                        break;
                    case 2:
                        switch (args[0].toLowerCase()) {
                            case "create":
                                enclaveManager.createEnclave(args[1]);
                                break;
                            default:
                                enclaveManager.sendNoobHelp();
                                break;
                        }
                        break;
                    default:
                        enclaveManager.sendNoobHelp();
                        break;
                }
            } else {
                switch (args.length) {
                    case 1:
                        switch (args[0].toLowerCase()) {
                            case "disband":
                                enclaveManager.disbandEnclave();
                                break;
                            case "sethome":
                                enclaveManager.setHome();
                                break;
                            case "home":
                                enclaveManager.goHome();
                                break;
                            case "unclaim":
                                enclaveManager.unclaimRegion();
                                break;
                            case "claim":
                                enclaveManager.claimRegion();
                                break;
                            case "leave":
                                enclaveManager.leaveEnclave();
                                break;
                            case "here":
                                enclaveManager.getInfo();
                                break;
                            case "tags":
                                enclaveManager.getTags();
                                break;
                            default:
                                enclaveManager.sendHelp();
                                break;
                        }
                        break;
                    case 2:
                        switch (args[0].toLowerCase()) {
                            case "create":
                                enclaveManager.createEnclave(args[1]);
                                break;
                            case "rename":
                                enclaveManager.renameEnclave(args[1]);
                                break;
                            case "invite":
                                enclaveManager.inviteMember(args[1]);
                                break;
                            case "color":
                                enclaveManager.setColor(args[1]);
                                break;
                            case "tag":
                                enclaveManager.toggleTag(args[1]);
                                break;
                            default:
                                enclaveManager.sendHelp();
                                break;
                        }
                        break;
                    default:
                        enclaveManager.sendHelp();
                        break;
                }
            }
        }
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                      @NotNull String s, String[] args) {
        
        Map<String, List<String>> commands = new HashMap<>();

        commands.put("claim", new ArrayList<>());
        commands.put("unclaim", new ArrayList<>());
        commands.put("invite", null);
        commands.put("rename", new ArrayList<>());
        commands.put("home", new ArrayList<>());
        commands.put("sethome", new ArrayList<>());
        commands.put("here", new ArrayList<>());
        commands.put("disband", new ArrayList<>());
        commands.put("create", null);
        commands.put("tags", null);

        commands.put("color", enumList(ChatColor.values()));
        commands.put("tag", enumList(Tag.values()));

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
