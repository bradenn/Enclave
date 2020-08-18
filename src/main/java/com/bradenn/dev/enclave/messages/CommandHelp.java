package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class CommandHelp {

    public static void sendHelp(Player player) {
        String stringBuilder = MessageUtils.getPrefix() +
                "Available Commands\n" +
                formatLine("/e create name", "Create an enclave") +
                formatLine("/e disband", "Disband your enclave") +
                formatLine("/e info", "Get info about a region") +
                formatLine("/e claim", "Claim regions for your enclave") +
                formatLine("/e unclaim", "Unclaim regions for your enclave");
        player.sendMessage(MessageUtils.format(stringBuilder));
    }

    public static String formatLine(String cmd, String desc){
        return String.format("%s%s %s- %s\n", ChatColor.of("#FE881E"), cmd, ChatColor.of("#8C8C8C"), desc);
    }

}
