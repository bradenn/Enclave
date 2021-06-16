package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.Runtime;
import com.bradenn.dev.enclave.messages.MessageBlock;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.RegionModel;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

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
        adminHelp.addLine("disband", "[player] Disband enclave using a member's username.");
        adminHelp.addLine("kick", "[player] Kick a player from their enclave.");
        adminHelp.addLine("wipe", "Remove region data for current chunk.");
        adminHelp.addLine("info", "Get info about a region");
        player.sendMessage(adminHelp.getMessage());
    }

    public void wipeChunk() {
        this.region.unclaimChunk();
    }

    public void showBorder() {
        if (Runtime.chunkRenderers.contains(player)) {
            Runtime.chunkRenderers.remove(player);
        } else {
            Runtime.chunkRenderers.add(player);
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
}
