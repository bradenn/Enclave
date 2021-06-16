package com.bradenn.dev.enclave.messages;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.map.MinecraftFont;

public class MessageBlock {

    private final StringBuilder message;
    private final int longestKey = 0;

    private static final ChatColor
            info = ChatColor.of("#0a84ff"),
            compliment = ChatColor.of("#ff9f0a"),
            secondary = ChatColor.of("#64d2ff"),
            danger = ChatColor.of("#ff453a"),
            mono0 = ChatColor.of("#7C7C80"),
            mono1 = ChatColor.of("#8e8e93"),
            mono2 = ChatColor.of("#aeaeb2"),
            mono3 = ChatColor.of("#c7c7cc"),
            mono4 = ChatColor.of("#D1D1D6");

    public static class Line {
        private final String key, value;
        Line(String key, String value) {
            this.key = key;
            this.value = value;
        }
        public String getString() {
            int font = MinecraftFont.Font.getWidth(this.key);
            String filler = StringUtils.repeat("·", (64 - font)/2);
            String formattedValue = this.value.replace("[", compliment+"[").replace("]", "]" + mono2);
            return String.format("    %s%s %s%s %s%s\n", mono3, this.key, mono1, filler, mono2, formattedValue);
        }
    }

    public MessageBlock(String title) {
        this.message = new StringBuilder(getTitle(title));
    }

    public void addLine(String key, String value) {
        this.message.append(new Line(key, value).getString());
    }

    public String getMessage() {
        return message.toString();
    }

    private String getTitle(String title) {
        return String.format("%s→ %s%s\n", info, mono4, title);
    }

}
