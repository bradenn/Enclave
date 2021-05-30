package com.bradenn.dev.enclave.commands;

import com.bradenn.dev.enclave.managers.EnclaveManager;
import com.bradenn.dev.enclave.managers.RegionManager;
import com.bradenn.dev.enclave.messages.CommandHelp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender commandSender, Command command, @NotNull String s, String[] args) {
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("admin")) {
            switch (args.length) {
                case 0:
                    CommandHelp.sendHelp(player);
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "disband":
                            EnclaveManager.disbandEnclave(player);
                        case "borders":
                            RegionManager.showBorders(player);
                        case "unclaim":
                            EnclaveManager.unclaimRegion(player);
                        case "claim":
                            EnclaveManager.claimRegion(player);
                        case "info":
                            RegionManager.showRegionInfo(player);
                        default:
                            CommandHelp.sendHelp(player);
                    }
                default:
                    CommandHelp.sendHelp(player);
            }
        }
        return true;
    }
}
