package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MessageUtils {

    // ACCENT FE881E
    // GREEN 45d14f
    // RED eb5f55
    // Dark Grey 555555
    // Text 8C8C8C


    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getPrefix() {
        return format(ChatColor.of("#555555") + "[" + ChatColor.of("#FE881E") + "Enclave" + ChatColor.of("#555555") + "] " + ChatColor.of("#8C8C8C"));
    }

    public static String getInfoPrefix() {
        return format(ChatColor.of("#22AFFF")+"→ &7");
    }

    public static String getErrorPrefix() {
        return format("&c→ &7");
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(getInfoPrefix() + format(message));
    }

    public static void sendAction(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(MessageUtils.format(message)));
    }

    public static void sendSuccess(Player player, String message) {
        player.sendMessage(getPrefix() + format(message));
    }

    public static void sendError(Player player, String message) {
        player.sendMessage(getErrorPrefix() + format(message));
    }

    public static void sendError(Player player, Response response) {
        player.sendMessage(getErrorPrefix() + format(response.getMessage()));
    }

    public static void send(Player player, Response response, String... args) {
        String prepared = String.format(response.getMessage(), (Object) args);
        String prefix = response.isError()?getErrorPrefix():getInfoPrefix();
        player.sendMessage(prefix + format(prepared));
    }

    public static void sendMessage(Player player, String message, boolean success) {
        player.sendMessage(getPrefix() + format(message));
    }

    public static void sendMessageWithoutPrefix(Player player, String message) {
        player.sendMessage(ChatColor.of("#8C8C8C") + format(message));
    }

}
