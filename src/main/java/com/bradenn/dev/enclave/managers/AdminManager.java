package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.Main;
import com.bradenn.dev.enclave.Runtime;
import com.bradenn.dev.enclave.messages.MessageBlock;
import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.messages.Response;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.models.RegionModel;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AdminManager {

    private final Player player;
    private final Chunk chunk;
    private final RegionModel region;

    public AdminManager(Player player) {
        this.player = player;
        this.chunk = player.getLocation().getChunk();
        this.region = new RegionModel(this.chunk);
    }

    public void sendHelp() {
        MessageBlock adminHelp = new MessageBlock("Admin Commands");
        adminHelp.addLine("disband", "[player] Disband a user's enclave.");
        adminHelp.addLine("kick", "[player] Kick a player from their enclave.");
        adminHelp.addLine("wipe", "Remove region data for current chunk.");
        adminHelp.addLine("border", "Toggle showing particle borders.");
        adminHelp.addLine("identifier", "Toggle showing particle identifier.");
        adminHelp.addLine("version", "Get the plugin version.");
        adminHelp.addLine("info", "Get info about a region");
        adminHelp.addLine("help", "Show this help page");
        player.sendMessage(adminHelp.getMessage());
    }

    public void wipeChunk() {
        this.region.unclaimChunk();
    }

    public void showBorder() {
        if (Runtime.chunkBorders.contains(player)) {
            Runtime.chunkBorders.remove(player);
            MessageUtils.send(player, Response.SHOW_BORDER_DISABLED);
        } else {
            Runtime.chunkBorders.add(player);
            MessageUtils.send(player, Response.SHOW_BORDER_ENABLED);
        }
    }

    public void showIdentifier() {
        if (Runtime.chunkIdentifiers.contains(player)) {
            Runtime.chunkIdentifiers.remove(player);
            MessageUtils.send(player, Response.SHOW_BORDER_DISABLED);
        } else {
            Runtime.chunkIdentifiers.add(player);
            MessageUtils.send(player, Response.SHOW_BORDER_ENABLED);
        }
    }

    public void showVersion() {
        MessageUtils.send(player, Response.SEND_VERSION, Main.plugin.getDescription().getVersion());
    }

    public void getInfo() {
        EnclaveModel em = region.getEnclave();

        MessageBlock regionInfo = new MessageBlock("Region Info");
        regionInfo.addLine("chunk", String.format("(%d, %d)", chunk.getX(), chunk.getZ()));
        regionInfo.addLine("status", region.isClaimed() ? "Claimed" : "Unclaimed");

        if (!region.isClaimed()) {
            player.sendMessage(regionInfo.getMessage());
            return;
        }

        MessageBlock enclaveInfo = new MessageBlock("Enclave Info");
        enclaveInfo.addLine("enclave", em.getDisplayName());
        enclaveInfo.addLine("owner", Bukkit.getOfflinePlayer(em.getOwner()).getName());
        enclaveInfo.addLine("members", em.listMembers().toString());
        String homeValue = em.getHome() != null ? "(" + em.getHome().getBlockX() + ", " + em.getHome().getBlockY() + ")" : "Unset";
        enclaveInfo.addLine("home", homeValue);
        enclaveInfo.addLine("since", em.originDate());
        enclaveInfo.addLine("tags", em.getTags().toString());

        player.sendMessage(regionInfo.getMessage() + enclaveInfo.getMessage());
    }

    /**
     * Disband the enclave belonging to a user.
     * @param playerName The player's username
     */
    public void disbandEnclave(String playerName) {
        if (validatePlayer(playerName)) {
            Player target = parsePlayer(playerName);
            PlayerModel playerModel = new PlayerModel(target.getUniqueId());
            EnclaveModel enclaveModel = playerModel.getEnclave();
            if (enclaveModel != null) {
                MessageUtils.send(player, Response.ENCLAVE_DISBANDED, enclaveModel.getName());
                enclaveModel.disbandEnclave();
            } else {
                MessageUtils.send(player, Response.E_PLAYER_NO_ENCLAVE);
            }
        } else {
            MessageUtils.send(player, Response.E_INVALID_PLAYER);
        }
    }

    /**
     * Kick a player from their enclave.
     * @param playerName The player's username
     */
    public void kickPlayer(String playerName) {
        if (validatePlayer(playerName)) {
            Player target = parsePlayer(playerName);
            UUID playerUUID = target.getUniqueId();
            PlayerModel playerModel = new PlayerModel(target.getUniqueId());
            EnclaveModel enclaveModel = playerModel.getEnclave();
            if (enclaveModel != null) {
                if (!enclaveModel.isOwner(playerUUID)) {
                    enclaveModel.removeMember(playerUUID);
                    playerModel.clearEnclave();
                    MessageUtils.send(player, Response.PLAYER_KICKED, target.getName(), enclaveModel.getName());
                } else {
                    MessageUtils.send(player, Response.E_KICK_OWNER);
                }
            } else {
                MessageUtils.send(player, Response.E_PLAYER_NO_ENCLAVE);
            }
        } else {
            MessageUtils.send(player, Response.E_INVALID_PLAYER);
        }
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
