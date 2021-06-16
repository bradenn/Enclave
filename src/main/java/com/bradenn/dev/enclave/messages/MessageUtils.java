package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private static final ChatColor info = ChatColor.of("#0a84ff"),
            danger = ChatColor.of("#ff453a"), mono1 = ChatColor.of("#8e8e93"), mono2 = ChatColor.of("#aeaeb2"),
            mono3 = ChatColor.of("#c7c7cc"), secondary = ChatColor.of("#64d2ff");

    public static String getInfoPrefix() {
        return format(info + "→ " + mono2);
    }

    public static String getErrorPrefix() {
        return format(danger + "→ " + mono2);
    }

    public static void sendAction(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(MessageUtils.format(message)));
    }

    public static void send(Player player, Response response, String... args) {
        String prepared = String.format(response.getMessage(), args);
        String prefix = response.isError() ? getErrorPrefix() : getInfoPrefix();
        player.sendMessage(prefix + format(prepared));
    }

}
