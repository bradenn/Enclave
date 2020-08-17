package com.bradenn.dev.enclave.commands;

import com.bradenn.dev.enclave.messages.CommandHelp;
import com.bradenn.dev.enclave.models.EnclaveModel;
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
        if(command.getName().equalsIgnoreCase("enclave")){
            if(args.length == 0){
                CommandHelp.sendHelp(player);
            }else if(args.length == 1){
                //
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("create")){
                    new EnclaveModel(player.getUniqueId(), args[1]);
                    player.sendMessage("You've made the new enclave!!!!");
                }
            }
        }
        return true;
    }



}
