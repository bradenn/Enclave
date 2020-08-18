package com.bradenn.dev.enclave.commands;

import com.bradenn.dev.enclave.managers.EnclaveManager;
import com.bradenn.dev.enclave.messages.CommandHelp;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EnclaveCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("enclave")) {
            if (args.length == 0) {
                CommandHelp.sendHelp(player);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("disband")) {
                    EnclaveManager.disbandEnclave(player);
                } else if (args[0].equalsIgnoreCase("claim")) {

                } else if (args[0].equalsIgnoreCase("info")) {
                    RegionModel regionModel = new RegionModel(player.getLocation().getChunk(), player.getWorld());
                    PlayerModel playerModel = new PlayerModel(player.getUniqueId());
                    regionModel.claimChunk(playerModel.getEnclave().getUUID());
                    player.sendMessage("Chunk Claimed");
                } else {
                    CommandHelp.sendHelp(player);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    EnclaveManager.createEnclave(player, args[1]);
                }
            }
        }
        return true;
    }


}
