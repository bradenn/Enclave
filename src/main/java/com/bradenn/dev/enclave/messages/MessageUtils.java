package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    // ACCENT FE881E
    // GREEN 45d14f
    // RED eb5f55
    // Dark Grey 555555
    // Text 8C8C8C


    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getPrefix(){
        return format(ChatColor.of("#555555") + "[" + ChatColor.of("#FE881E") +  "Enclave" + ChatColor.of("#555555") +  "] " + ChatColor.of("#8C8C8C"));
    }

    public static void sendMessage(Player player, String message){
        player.sendMessage(getPrefix() + format(message));
    }

    public static void sendMessage(Player player, String message, boolean success){
        player.sendMessage(getPrefix() + format(message));
    }

    public static void sendMessageWithoutPrefix(Player player, String message){
        player.sendMessage(ChatColor.of("#8C8C8C") + format(message));
    }

}
