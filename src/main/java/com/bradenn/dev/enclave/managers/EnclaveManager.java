package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.messages.Response;
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

    /**
     * If the user is not a member of an enclave, create a new one.
     * This function also performs name checks.
     *
     * @param player The player performing the commands.
     */
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

    /**
     * If the user is not a member of an enclave, create a new one.
     * This function also performs name checks.
     * @param name The name of the enclave
     */
    public void createEnclave(String name) {
        if (enclave == null) {
            if (validateName(name)) {
                EnclaveModel enclaveModel = new EnclaveModel(player.getUniqueId(), name);
                MessageUtils.send(player, Response.ENCLAVE_CREATED, enclaveModel.getName());
            } else {
                MessageUtils.send(player, Response.E_INVALID_ENCLAVE_NAME);
            }
        } else {
            MessageUtils.send(player, Response.E_ENCLAVE);
        }
    }

    /**
     * If the user is the owner of an enclave, delete it.
     */
    public void disbandEnclave() {
        if (enclave.isValid()) {
            if (enclave.isOwner(player.getUniqueId())) {
                MessageUtils.send(player, Response.ENCLAVE_DISBANDED, enclave.getName());
                enclave.disbandEnclave();
            } else {
                MessageUtils.send(player, Response.E_INSUFFICIENT_CLOUT, enclave.getName());
            }
        } else {
            MessageUtils.send(player, Response.E_NO_ENCLAVE);
        }
    }

    /**
     * If the user is the owner of an enclave, delete it.
     */
    public void claimRegion() {
        if (region.isClaimed()) {
            MessageUtils.send(player, Response.E_CHUNK_CLAIMED);
        } else {
            if (playerModel.hasEnclave()) {
                if (playerModel.getEnclave().isOwner(player.getUniqueId())) {
                    region.claimChunk(playerModel.getEnclave().getUUID());
                    MessageUtils.send(player, Response.CHUNK_CLAIMED);
                } else {
                    MessageUtils.send(player, Response.E_INSUFFICIENT_CLOUT);
                }
            } else {
                MessageUtils.send(player, Response.E_NO_ENCLAVE);
            }
        }
    }

    public void unclaimRegion() {
        if (!region.isClaimed()) {
            MessageUtils.send(player, Response.E_NONMEMBER_CHUNK);
        } else {
            if (playerModel.hasEnclave()) {
                if (playerModel.getEnclave().getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                    if (region.getEnclave().getUUID().toString().equalsIgnoreCase(playerModel.getEnclave().getUUID().toString())) {
                        region.unclaimChunk();
                        MessageUtils.send(player, Response.CHUNK_UNCLAIMED);
                    } else {
                        MessageUtils.send(player, Response.E_NONMEMBER_CHUNK);
                    }
                } else {
                    MessageUtils.send(player, Response.E_INSUFFICIENT_CLOUT);
                }
            } else {
                MessageUtils.send(player, Response.E_NO_ENCLAVE);
            }
        }
    }

    public void inviteMember(String target) {
        if (playerModel.hasEnclave()) {
            if (validatePlayer(target)) {
                Player targetPlayer = parsePlayer(target);
                PlayerModel targetPlayerModel = new PlayerModel(targetPlayer.getUniqueId());
                if (targetPlayerModel.hasEnclave()) {
                    MessageUtils.send(player, Response.E_ALREADY_IN_ENCLAVE);
                } else {
                    targetPlayerModel.addInvite(enclave.getUUID());
                    MessageUtils.send(player, Response.INVITE_SENT, targetPlayer.getName());
                    MessageUtils.send(player, Response.INVITE_PLAYER, enclave.getName());
                }
            } else {
                MessageUtils.send(player, Response.E_INVALID_PLAYER);
            }
        } else {
            MessageUtils.send(player, Response.E_NO_ENCLAVE);
        }
    }

    public void setColor(String target) {

        Pattern p = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
        List<String> colors = new ArrayList<>();
        new ArrayList<>(Arrays.asList(ChatColor.values())).forEach(chatColor -> {
            if (chatColor != ChatColor.MAGIC)
                colors.add(chatColor.name());
        });

        if (p.matcher(target).matches() || colors.contains(target.toUpperCase())) {
            enclave.setColor(target.toUpperCase());
            MessageUtils.send(player, Response.COLOR_CHANGED, target);
        } else {
            MessageUtils.send(player, Response.E_INVALID_COLOR);
        }
    }

    public void toggleTag(String arg) {
        if (enclave.isOwner(playerModel.getPlayerUUID())) {
            if (enclave.toggleTag(EnclaveTag.valueOf(arg))) {
                MessageUtils.send(player, Response.TAG_DISABLED, arg);
            } else {
                MessageUtils.send(player, Response.TAG_ENABLED, arg);
            }
        } else {
            MessageUtils.send(player, Response.E_INSUFFICIENT_CLOUT);
        }
    }

    public void getTags() {
        List<String> diff = new ArrayList<>();
        for (EnclaveTag value : EnclaveTag.values()) {
            if (!enclave.getTags().contains(value.name())) diff.add(value.name());
        }

        MessageUtils.send(player, Response.TAG_LIST_ENABLED, diff.toString());
        MessageUtils.send(player, Response.TAG_LIST_DISABLED,  enclave.getTags().toString());

    }

    public void setHome() {
        if (enclave.isOwner(playerModel.getPlayerUUID())) {
            if (region.isEnclave(enclave.getUUID())) {
                MessageUtils.send(player, Response.HOME_SET);
                enclave.setHome(player.getLocation());
            } else {
                MessageUtils.send(player, Response.E_NONMEMBER_CHUNK);
            }
        } else {
            MessageUtils.send(player, Response.E_INSUFFICIENT_CLOUT);
        }
    }

    public void goHome() {
        if (enclave.hasMember(playerModel.getPlayerUUID())) {
            Location loc = enclave.getHome();
            if (loc == null) {
                MessageUtils.send(player, Response.E_NO_HOME);
            } else {
                player.teleport(loc);
                MessageUtils.send(player, Response.WELCOME_HOME);
                ParticleRenderer.beamUp(player.getLocation().add(0, 0, 0), net.md_5.bungee.api.ChatColor.of(enclave.getColor()).getColor());
            }
        } else {
            MessageUtils.send(player, Response.E_NO_ENCLAVE);
        }
    }

    public void joinEnclave() {
        UUID enclaveUUID = playerModel.getInvite();
        if (enclaveUUID != null) {
            if (!enclave.isValid()) {
                EnclaveModel em = new EnclaveModel(enclaveUUID);
                em.addMember(playerModel.getPlayerUUID());
                playerModel.setEnclave(enclaveUUID);
                MessageUtils.send(player, Response.ENCLAVE_JOINED, em.getDisplayName());
            } else {
                MessageUtils.send(player, Response.E_ALREADY_IN_ENCLAVE);
            }
        } else {
            MessageUtils.send(player, Response.E_NO_PENDING);
        }

    }

    public void leaveEnclave() {
        if (enclave.hasMember(playerModel.getPlayerUUID())) {
            if (!enclave.isOwner(playerModel.getPlayerUUID())) {
                enclave.removeMember(playerModel.getPlayerUUID());
                playerModel.clearEnclave();
                MessageUtils.send(player, Response.ENCLAVE_LEFT);
            } else {
                MessageUtils.send(player, Response.ENCLAVE_CANNOT_LEAVE);
            }
        } else {
            MessageUtils.send(player, Response.E_NO_ENCLAVE);
        }
    }

    /**
     * Verification Utils
     */
    private boolean validateName(String name) {
        String pattern = "^[a-zA-Z0-9]*$";
        return name.matches(pattern);
    }

    /**
     * Validate Player
     */
    private boolean validatePlayer(String name) {
        return parsePlayer(name) != null;
    }

    /**
     * Get Player from server.
     */
    private Player parsePlayer(String name) {
        return Bukkit.getPlayer(name);
    }


}
