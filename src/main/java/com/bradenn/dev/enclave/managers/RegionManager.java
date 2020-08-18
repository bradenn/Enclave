package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.RegionModel;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

public class RegionManager {

    public static void showBorders(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        int locX = chunk.getX() << 4;
        int locZ = chunk.getZ() << 4;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (x == 0 && z == 0 || x == 15 && z == 15 || x == 15 && z == 0 || x == 0 && z == 15) {
                    Location loc = player.getWorld().getHighestBlockAt(locX + x, locZ + z).getLocation().add(0, 1, 0);
                    BlockData bd = Material.SOUL_TORCH.createBlockData();
                    player.sendBlockChange(loc, bd);
                }
            }
        }
    }

    public static void showRegionInfo(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        RegionModel regionModel = new RegionModel(player.getLocation().getChunk(), player.getWorld());
        MessageUtils.sendMessage(player, "Region Information");
        MessageUtils.sendMessageWithoutPrefix(player, "Region: CX: " + chunk.getX() + " CZ: " + chunk.getZ());
        MessageUtils.sendMessageWithoutPrefix(player, "Claimed: " + regionModel.isClaimed());
        if (regionModel.isClaimed()) {
            MessageUtils.sendMessageWithoutPrefix(player, "Enclave: " + regionModel.getEnclave().getName());
        } else {
            MessageUtils.sendMessageWithoutPrefix(player, "Enclave: &oUnclaimed");
        }
    }

}
