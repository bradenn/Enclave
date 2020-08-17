package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;

public class MessageUtils {

    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getPrefix(){
        return format(ChatColor.of("#555555") + "[" + ChatColor.of("#FE881E") +  "Enclave" + ChatColor.of("#555555") +  "] " + ChatColor.of("#8C8C8C"));

    }

}
