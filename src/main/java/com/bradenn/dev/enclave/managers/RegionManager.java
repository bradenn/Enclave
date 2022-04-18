package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.renderers.EnclaveMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public class RegionManager {

    public static void showBorders(Player player) {
        ItemStack newMap = new ItemStack(Material.FILLED_MAP);
        MapMeta mm = (MapMeta) newMap.getItemMeta();

        MapView mv = player.getServer().createMap(player.getWorld());

        assert mm != null;
        mm.setMapView(mv);
        ChatColor cc = ChatColor.of("#0a84ff");
        mm.setDisplayName("§aEnclave Map");
        newMap.setItemMeta(mm);

        mv.addRenderer(new EnclaveMap());
        player.getInventory().addItem(newMap);
        player.sendMap(mv);
    }

    public static void showRegionInfo(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        RegionModel regionModel = new RegionModel(player.getLocation().getChunk(), player.getWorld());

        String stringBuilder = MessageUtils.getInfoPrefix() +
                "Region Information\n" +
                formatLine("Chunk", String.format("(%d, %d)", chunk.getX(), chunk.getZ())) +
                formatLine("Status", regionModel.isClaimed() ? "Claimed" : "Unclaimed") +
                formatLine("Enclave", regionModel.isClaimed() ? regionModel.getEnclave().getDisplayName() : "N/A");

        player.sendMessage(MessageUtils.format(stringBuilder));

    }

    public static String formatLine(String key, String value) {
        return String.format("   &7%s - %s\n", key, value);
    }

}
