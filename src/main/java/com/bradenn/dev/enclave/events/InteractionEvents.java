package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.EnclaveTag;
import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.utilities.InteractionUtility;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class InteractionEvents implements Listener {

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        if (InteractionUtility.invalidInteraction(e.getPlayer(), e.getBlock().getChunk())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        if (InteractionUtility.invalidInteraction(e.getPlayer(), e.getBlock().getChunk())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if (InteractionUtility.invalidInteraction(e.getPlayer(), e.getClickedBlock().getChunk())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        if (e.getTo() == null) return;

        Chunk chunkFrom = e.getFrom().getChunk();
        Chunk chunkTo = e.getTo().getChunk();

        // Only process movements if the player has moved between chunks.
        if (chunkTo != chunkFrom) {
            RegionModel from = new RegionModel(chunkFrom);
            RegionModel to = new RegionModel(chunkTo);

            if (to.isClaimed()) {
                if(InteractionUtility.invalidEvent(e.getTo().getChunk(), EnclaveTag.ENTER)){
                    e.setCancelled(true);
                }
                if(from.isClaimed()){
                    if(!to.getEnclave().getUUID().toString().equals(from.getEnclave().getUUID().toString())){

                        MessageUtils.sendAction(e.getPlayer(), String.format("&aNow entering %s&a.", to.getEnclave().getDisplayName()));
                    }
                }else{
                    MessageUtils.sendAction(e.getPlayer(), String.format("&aNow entering %s&a.", to.getEnclave().getDisplayName()));
                }
            } else if (from.isClaimed()) {
                MessageUtils.sendAction(e.getPlayer(), "&aNow entering the wild.");
            }
        }
    }


}
