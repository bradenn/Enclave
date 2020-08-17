package com.bradenn.dev.enclave.messages;

import org.bukkit.entity.Player;

public class CommandHelp {

    public static void sendHelp(Player player){
        String stringBuilder = MessageUtils.getPrefix() +
                "Available Commands\n" +
                "/e create [name]";
        player.sendMessage(MessageUtils.format(stringBuilder));
    }

}
