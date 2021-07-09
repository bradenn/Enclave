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
    private final ValidationManager validationManager;

    public AdminManager(Player player) {
        this.player = player;
        this.chunk = player.getLocation().getChunk();
        this.region = new RegionModel(this.chunk);
        this.validationManager = new ValidationManager(player);
    }

    public void sendHelp() {
        MessageBlock adminHelp = new MessageBlock("Admin Commands");
        adminHelp.addLine("disband", "[player] Disband a user's enclave.");
        adminHelp.addLine("kick", "[player] Kick a player from their enclave.");
        adminHelp.addLine("wipe", "Remove region data for current chunk.");
        adminHelp.addLine("cleanup", "Reset all particle effects.");
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
        if (Main.runtime.togglePlayerTask(player, Runtime.PlayerTask.SHOW_PARTICLE_BORDER)) {
            MessageUtils.send(player, Response.SHOW_BORDER_ENABLED);
        } else {
            MessageUtils.send(player, Response.SHOW_BORDER_DISABLED);
        }
    }

    public void showIdentifier() {
        if (Main.runtime.togglePlayerTask(player, Runtime.PlayerTask.SHOW_PARTICLE_IDENTIFIER)) {
            MessageUtils.send(player, Response.SHOW_BORDER_ENABLED);
        } else {
            MessageUtils.send(player, Response.SHOW_BORDER_DISABLED);
        }
    }

    public void showVersion() {
        MessageUtils.send(player, Response.SEND_VERSION, Main.plugin.getDescription().getVersion());
    }

    public void cleanupRuntime() {
        int total = Main.runtime.clear();
        MessageUtils.send(player, Response.SEND_CLEANUP, total);
    }

    /**
     * Set the color of an enclave.
     *
     * @param playerName The player's username
     * @param color      The new color.
     */
    public void setColor(String playerName, String color) {
        if (validationManager.playerHasEnclave(playerName)) {
            if (validationManager.colorIsValid(color)) {
                Player target = parsePlayer(playerName);
                PlayerModel playerModel = new PlayerModel(target.getUniqueId());
                EnclaveModel enclaveModel = playerModel.getEnclave();
                enclaveModel.setColor(color.toUpperCase());
            }
        }
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
     *
     * @param playerName The player's username
     */
    public void disbandEnclave(String playerName) {
        if (validationManager.playerHasEnclave(playerName)) {
            Player target = parsePlayer(playerName);
            PlayerModel playerModel = new PlayerModel(target.getUniqueId());
            EnclaveModel enclaveModel = playerModel.getEnclave();
            MessageUtils.send(player, Response.ENCLAVE_DISBANDED, enclaveModel.getName());
            enclaveModel.disbandEnclave();
        }
    }

    /**
     * Kick a player from their enclave.
     *
     * @param playerName The player's username
     */
    public void kickPlayer(String playerName) {
        if (validationManager.playerHasEnclave(playerName)) {
            Player target = parsePlayer(playerName);
            UUID playerUUID = target.getUniqueId();

            PlayerModel playerModel = new PlayerModel(playerUUID);
            EnclaveModel enclaveModel = playerModel.getEnclave();

            if (!enclaveModel.isOwner(playerUUID)) {
                enclaveModel.removeMember(playerUUID);
                playerModel.clearEnclave();
                MessageUtils.send(player, Response.PLAYER_KICKED, target.getName(), enclaveModel.getName());
            } else {
                MessageUtils.send(player, Response.E_KICK_OWNER);
            }
        }
    }

    /**
     * Get Player from server.
     */
    private Player parsePlayer(String name) {
        return Bukkit.getPlayer(name);
    }

}
