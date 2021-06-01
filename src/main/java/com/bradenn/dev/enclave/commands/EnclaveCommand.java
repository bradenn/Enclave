package com.bradenn.dev.enclave.commands;

import com.bradenn.dev.enclave.managers.EnclaveManager;
import com.bradenn.dev.enclave.managers.RegionManager;
import com.bradenn.dev.enclave.messages.CommandHelp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnclaveCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender commandSender, Command command, String s, String[] args) {
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
                            case "map":
                                RegionManager.showBorders(player);
                                break;
                            case "here":
                                RegionManager.showRegionInfo(player);
                                break;
                            default:
                                CommandHelp.sendNoobHelp(player);
                                break;
                        }
                        break;
                    case 2:
                        switch (args[0].toLowerCase()) {
                            case "create":
                                enclaveManager.createEnclave(args[1]);
                                break;
                            default:
                                CommandHelp.sendNoobHelp(player);
                                break;
                        }
                        break;
                    default:
                        CommandHelp.sendNoobHelp(player);
                        break;
                }
            } else {
                switch (args.length) {
                    case 1:
                        switch (args[0].toLowerCase()) {
                            case "disband":
                                enclaveManager.disbandEnclave();
                                break;
                            case "map":
                                RegionManager.showBorders(player);
                                break;
                            case "unclaim":
                                enclaveManager.unclaimRegion();
                                break;
                            case "claim":
                                enclaveManager.claimRegion();
                                break;
                            case "here":
                                RegionManager.showRegionInfo(player);
                                break;
                            case "tags":
                                enclaveManager.getTags();
                                break;
                            default:
                                CommandHelp.sendHelp(player);
                                break;
                        }
                        break;
                    case 2:
                        switch (args[0].toLowerCase()) {
                            case "create":
                                enclaveManager.createEnclave(args[1]);
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
                                CommandHelp.sendHelp(player);
                                break;
                        }
                        break;
                    default:
                        CommandHelp.sendHelp(player);
                        break;
                }
            }
        }
        return true;
    }
}
