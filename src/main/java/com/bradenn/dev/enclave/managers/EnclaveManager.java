package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.EnclaveTag;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.models.RegionModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class EnclaveManager {

    private final EnclaveModel enclave;
    private final RegionModel region;
    private final PlayerModel playerModel;
    private final Player player;

    public EnclaveManager(Player player) {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId());
        if (playerModel.hasEnclave()) {
            this.enclave = playerModel.getEnclave();
        } else {
            this.enclave = null;
        }
        this.region = new RegionModel(player.getLocation().getChunk());
        this.playerModel = new PlayerModel(player.getUniqueId());
        this.player = player;
    }

    public boolean isValid() {
        if (enclave != null) {
            return enclave.isValid();
        } else {
            return false;
        }
    }

    public void createEnclave(String name) {
        if (enclave == null) {
            if (validateName(name)) {
                EnclaveModel enclaveModel = new EnclaveModel(player.getUniqueId(), name);
                MessageUtils.sendMessage(player, "Created the enclave '" + enclaveModel.getName() + "'.");
            } else {
                MessageUtils.sendMessage(player, "Your enclave name must not include any special characters.");
            }
        } else {
            MessageUtils.sendMessage(player, "You must disband your current enclave, '" + enclave.getName() + "' before creating a new one.");
        }
    }

    public void disbandEnclave() {
        if (enclave.getOwner() != null) {
            if (enclave.isOwner(player.getUniqueId())) {
                MessageUtils.sendMessage(player, "Disbanded the enclave '" + enclave.getName() + "'.");
                enclave.disbandEnclave();
            } else {
                MessageUtils.sendMessage(player, "You must be the owner to disband an enclave; use '/e leave' to leave the enclave.");
            }
        } else {
            MessageUtils.sendMessage(player, "You must create an Enclave before you can disband it.");
        }
    }

    public void claimRegion() {
        if (region.isClaimed()) {
            MessageUtils.sendMessage(player, "This region is already claimed.");
        } else {
            if (playerModel.hasEnclave()) {
                if (playerModel.getEnclave().isOwner(player.getUniqueId())) {
                    region.claimChunk(playerModel.getEnclave().getUUID());
                    MessageUtils.sendMessage(player, "Region claimed.");
                } else {
                    MessageUtils.sendMessage(player, "You must be the enclave owner to claim land.");
                }
            } else {
                MessageUtils.sendMessage(player, "You must create an Enclave before you can claim land; use '/e create [name]'");
            }
        }
    }

    public void unclaimRegion() {
        if (!region.isClaimed()) {
            MessageUtils.sendMessage(player, "This region is not claimed.");
        } else {
            if (playerModel.hasEnclave()) {
                if (playerModel.getEnclave().getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                    if (region.getEnclave().getUUID().toString().equalsIgnoreCase(playerModel.getEnclave().getUUID().toString())) {
                        region.unclaimChunk(playerModel.getEnclave().getUUID());
                        MessageUtils.sendMessage(player, "Region unclaimed.");
                    } else {
                        MessageUtils.sendMessage(player, "This region does not belong to your enclave.");
                    }
                } else {
                    MessageUtils.sendMessage(player, "You must be the enclave owner to unclaim land.");
                }
            } else {
                MessageUtils.sendMessage(player, "You must create an Enclave before you can unclaim land; use '/e create [name]'");
            }
        }
    }

    public void inviteMember(String target) {
        if (playerModel.hasEnclave()) {
            if (validatePlayer(target)) {
                Player targetPlayer = parsePlayer(target);
                PlayerModel targetPlayerModel = new PlayerModel(targetPlayer.getUniqueId());
                if (targetPlayerModel.hasEnclave()) {
                    MessageUtils.sendMessage(player, "This player is already in an Enclave. They must leave first.");
                } else {
                    targetPlayerModel.addInvite(enclave.getUUID());
                    MessageUtils.sendMessage(targetPlayer, "You have been invited to join '" + enclave.getName() + "'. Type /e accept " + enclave.getName() + " to accept and join.");
                }
            } else {
                MessageUtils.sendMessage(player, "That player does not exist.");
            }
        } else {
            MessageUtils.sendMessage(player, "You must create or join an enclave before you can invite someone to join.");
        }
    }

    public void setColor(String target) {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId());
        EnclaveModel enclaveModel = playerModel.getEnclave();
        Pattern p = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
        List<String> colors = new ArrayList<>();
        new ArrayList<>(Arrays.asList(ChatColor.values())).forEach(chatColor -> {
            if (chatColor != ChatColor.MAGIC)
                colors.add(chatColor.name());
        });
        if (p.matcher(target).matches()) {
            enclaveModel.setColor(target);
            MessageUtils.sendMessage(player, String.format("Your enclave color has been changed to %s.", target));
        } else if (colors.contains(target.toUpperCase())) {
            enclaveModel.setColor(target.toUpperCase());
            MessageUtils.sendMessage(player, String.format("Your enclave color has been changed to %s.", target));
        } else {
            MessageUtils.sendError(player, "That is an invalid color.");
        }
    }

    public void toggleTag(String arg) {
        if (enclave.toggleTag(EnclaveTag.valueOf(arg))) {
            MessageUtils.sendMessage(player, "The attribute '" + arg + "' is now disabled.");
        } else {
            MessageUtils.sendMessage(player, "The attribute '" + arg + "' is now enabled.");
        }
    }

    public void getTags() {
        List<String> diff = new ArrayList<>();
        for (EnclaveTag value : EnclaveTag.values()) {
            if (!enclave.getTags().contains(value.name())) diff.add(value.name());
        }
        MessageUtils.sendMessage(player, "Enabled: " + diff);
        MessageUtils.sendMessage(player, "Disabled: " + enclave.getTags().toString());
    }

    /**
     * Verification Utils
     */
    private boolean validateName(String name) {
        String pattern = "^[a-zA-Z0-9]*$";
        return name.matches(pattern);
    }

    private boolean validatePlayer(String name) {
        return parsePlayer(name) != null;
    }

    /**
     * Parsing Utils
     */
    private Player parsePlayer(String name) {
        return Bukkit.getPlayer(name);
    }

}
