package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class CommandHelp {

    public static void sendHelp(Player player) {
        String stringBuilder = MessageUtils.getInfoPrefix() +
                "Available Commands\n" +
                formatLine("create [name]", "Create an enclave") +
                formatLine("color [color]|[#hex]", "Change your enclave color") +
                formatLine("map", "Get a map of nearby enclaves") +
                formatLine("invite [player]", "Invite a player to your enclave") +
                formatLine("claim", "Claim regions for your enclave") +
                formatLine("unclaim", "Unclaim regions for your enclave") +
                formatLine("disband", "Disband your enclave") +
                formatLine("info", "Get info about a region");

        player.sendMessage(MessageUtils.format(stringBuilder));
    }

    public static String formatLine(String cmd, String desc) {
        return String.format("  &7/e %s%s %s- %s\n", ChatColor.of("#22AFFF"), cmd, "&7", desc);
    }

    public static void sendNoobHelp(Player player) {
        String stringBuilder = MessageUtils.getInfoPrefix() +
                "Available Commands\n" +
                formatLine("create [name]", "Create an enclave") +
                formatLine("map", "Get a map of nearby enclaves") +
                formatLine("here", "Get info about a region");

        player.sendMessage(MessageUtils.format(stringBuilder));
    }
}
