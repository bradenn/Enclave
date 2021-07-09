package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.messages.Response;
import com.bradenn.dev.enclave.models.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationManager {

    private final Player player;

    public ValidationManager(Player player) {
        this.player = player;
    }

    /**
     * Validate a color input.
     */
    public boolean colorIsValid(String color) {
        Pattern p = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
        List<String> colors = new ArrayList<>();

        new ArrayList<>(Arrays.asList(ChatColor.values())).forEach(chatColor -> {
            if (chatColor != ChatColor.MAGIC)
                colors.add(chatColor.name());
        });

        if (p.matcher(color).matches() || colors.contains(color.toUpperCase())) {
            return true;
        } else {
            MessageUtils.send(player, Response.E_INVALID_COLOR);
            return false;
        }
    }

    /**
     * Validate Player
     * @param name The player username
     */
    public boolean playerIsValid(String name) {
        if (parsePlayer(name) != null) {
            return true;
        } else {
            MessageUtils.send(player, Response.E_INVALID_PLAYER);
            return false;
        }
    }

    /**
     * Validate player invite
     */
    public boolean inviteIsValid() {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId());
        if (playerModel.getInvite() != null) {
            return true;
        } else {
            MessageUtils.send(player, Response.E_NO_PENDING);
            return false;
        }
    }

    /**
     * Validate Player enclave
     * @param name The player username
     */
    public boolean playerHasEnclave(String name) {
        Player target = parsePlayer(name);
        if (target != null) {
            PlayerModel playerModel = new PlayerModel(target.getUniqueId());
            if (playerModel.hasEnclave()) {
                return true;
            } else {
                MessageUtils.send(player, Response.E_PLAYER_NO_ENCLAVE);
                return false;
            }
        } else {
            MessageUtils.send(player, Response.E_INVALID_PLAYER);
            return false;
        }
    }

    /**
     * Get Player from server.
     */
    private Player parsePlayer(String name) {
        return Bukkit.getPlayer(name);
    }


    public <T extends Enum<T>> List<String> enumList(T[] object) {
        List<String> items = new ArrayList<>();
        for (T t : object) items.add(t.name());
        return items;
    }

}
