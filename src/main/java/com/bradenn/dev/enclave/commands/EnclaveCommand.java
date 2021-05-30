package com.bradenn.dev.enclave.commands;

import com.bradenn.dev.enclave.managers.EnclaveManager;
import com.bradenn.dev.enclave.managers.RegionManager;
import com.bradenn.dev.enclave.messages.CommandHelp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnclaveCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("enclave")) {
            if (args.length == 0) {
                CommandHelp.sendHelp(player);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("disband")) {
                    EnclaveManager.disbandEnclave(player);
                } else if (args[0].equalsIgnoreCase("borders")) {
                    RegionManager.showBorders(player);
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    EnclaveManager.unclaimRegion(player);
                } else if (args[0].equalsIgnoreCase("claim")) {
                    EnclaveManager.claimRegion(player);
                } else if (args[0].equalsIgnoreCase("info")) {
                    RegionManager.showRegionInfo(player);
                } else if (args[0].equalsIgnoreCase("here")) {
                    RegionManager.showRegionInfo(player);
                } else {
                    CommandHelp.sendHelp(player);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    EnclaveManager.createEnclave(player, args[1]);
                } else if (args[0].equalsIgnoreCase("invite")) {
                    EnclaveManager.inviteMember(player, args[1]);
                }else if (args[0].equalsIgnoreCase("color")) {
                    EnclaveManager.setColor(player, args[1]);
                } else {
                    CommandHelp.sendHelp(player);
                }
            } else {
                CommandHelp.sendHelp(player);
            }
        }
        return true;
    }


}
