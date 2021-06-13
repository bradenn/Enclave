package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.EnclaveTag;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.renderers.ParticleRenderer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
                MessageUtils.sendError(player, "Your enclave name must not include any special characters.");
            }
        } else {
            MessageUtils.sendError(player, "You must disband your current enclave, '" + enclave.getName() + "' before creating a new one.");
        }
    }

    public void disbandEnclave() {
        if (enclave.getOwner() != null) {
            if (enclave.isOwner(player.getUniqueId())) {
                MessageUtils.sendMessage(player, "Disbanded the enclave '" + enclave.getName() + "'.");
                enclave.disbandEnclave();
            } else {
                MessageUtils.sendError(player, "You must be the owner to disband an enclave; use '/e leave' to leave the enclave.");
            }
        } else {
            MessageUtils.sendError(player, "You must create an Enclave before you can disband it. You must be French.");
        }
    }

    public void claimRegion() {
        if (region.isClaimed()) {
            MessageUtils.sendError(player, "This region is already claimed.");
        } else {
            if (playerModel.hasEnclave()) {
                if (playerModel.getEnclave().isOwner(player.getUniqueId())) {
                    region.claimChunk(playerModel.getEnclave().getUUID());
                    MessageUtils.sendMessage(player, "Region claimed.");
                } else {
                    MessageUtils.sendError(player, "You must be the enclave owner to claim land.");
                }
            } else {
                MessageUtils.sendError(player, "You must create an Enclave before you can claim land; use '/e create [name]'");
            }
        }
    }

    public void unclaimRegion() {
        if (!region.isClaimed()) {
            MessageUtils.sendError(player, "This region is not claimed.");
        } else {
            if (playerModel.hasEnclave()) {
                if (playerModel.getEnclave().getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                    if (region.getEnclave().getUUID().toString().equalsIgnoreCase(playerModel.getEnclave().getUUID().toString())) {
                        region.unclaimChunk();
                        MessageUtils.sendMessage(player, "Region unclaimed.");
                    } else {
                        MessageUtils.sendError(player, "This region does not belong to your enclave.");
                    }
                } else {
                    MessageUtils.sendError(player, "You must be the enclave owner to unclaim land.");
                }
            } else {
                MessageUtils.sendError(player, "You must create an Enclave before you can unclaim land; use '/e create [name]'");
            }
        }
    }

    public void inviteMember(String target) {
        if (playerModel.hasEnclave()) {
            if (validatePlayer(target)) {
                Player targetPlayer = parsePlayer(target);
                PlayerModel targetPlayerModel = new PlayerModel(targetPlayer.getUniqueId());
                if (targetPlayerModel.hasEnclave()) {
                    MessageUtils.sendError(player, "This player is already in an Enclave. They must leave first.");
                } else {
                    targetPlayerModel.addInvite(enclave.getUUID());
                    MessageUtils.sendMessage(player, "An invite has been sent.");
                    MessageUtils.sendMessage(targetPlayer, "You have been invited to join '" + enclave.getName() + "'. Type /e join to accept and join.");
                }
            } else {
                MessageUtils.sendError(player, "That player does not exist.");
            }
        } else {
            MessageUtils.sendError(player, "You must create or join an enclave before you can invite someone to join.");
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

    public void setHome() {
        if (enclave.isOwner(playerModel.getPlayerUUID())) {
            if (region.isEnclave(enclave.getUUID())) {
                MessageUtils.sendMessage(player, "Your enclave home as been set to your current position.");
                enclave.setHome(player.getLocation());
            } else {
                MessageUtils.sendError(player, "An enclave home can only be set within your Enclave. Silly goose!");
            }
        } else {
            MessageUtils.sendError(player, "You must be the owner of the enclave to set a home.");
        }
    }

    public void goHome() {
        if (enclave.hasMember(playerModel.getPlayerUUID())) {
            Location loc = enclave.getHome();
            if (loc == null) {
                MessageUtils.sendError(player, "Your enclave does not have a home set.");
            } else {
                player.teleport(loc);
                MessageUtils.sendMessage(player, "You have teleported to your enclave's home.");
                ParticleRenderer.beamUp(player.getLocation().add(0, 0, 0), net.md_5.bungee.api.ChatColor.of(enclave.getColor()).getColor());
            }
        } else {
            MessageUtils.sendError(player, "Interesting, inform your local developer of this message. You may be entitled to zero compensation. Error Code #69420");
        }
    }

    public void joinEnclave() {
        UUID enclaveUUID = playerModel.getInvite();
        if (enclaveUUID != null) {
            if (!enclave.isValid()) {
                EnclaveModel em = new EnclaveModel(enclaveUUID);
                em.addMember(playerModel.getPlayerUUID());
                playerModel.setEnclave(enclaveUUID);
                MessageUtils.sendMessage(player, "You have joined the enclave " + em.getDisplayName());
            } else {
                MessageUtils.sendError(player, "You are already in an enclave. How tf did this even happen?");
            }
        } else {
            MessageUtils.sendError(player, "You have not been invited to an enclave.");
        }

    }

    public void leaveEnclave() {
        if (enclave.hasMember(playerModel.getPlayerUUID())) {
            if (!enclave.isOwner(playerModel.getPlayerUUID())) {
                enclave.removeMember(playerModel.getPlayerUUID());
                playerModel.setEnclave(null);
                MessageUtils.sendMessage(player, "You have left the enclave.");
            } else {
                MessageUtils.sendError(player, "You are the owner of this enclave. You must disband it before leaving.");

            }
        } else {
            MessageUtils.sendError(player, "You don't have an enclave to leave, how did you get here anyhow?");
        }
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
