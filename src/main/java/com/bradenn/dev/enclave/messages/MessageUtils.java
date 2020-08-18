package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getPrefix(){
        return format(ChatColor.of("#555555") + "[" + ChatColor.of("#FE881E") +  "Enclave" + ChatColor.of("#555555") +  "] " + ChatColor.of("#8C8C8C"));
    }

    public static void sendMessage(Player player, String message){
        player.sendMessage(getPrefix() + format(message));
    }

}
