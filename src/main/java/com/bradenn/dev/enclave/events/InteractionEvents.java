package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.models.Tag;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class InteractionEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void blockBreakEvent(BlockBreakEvent e) {
        e.setCancelled(EventUtility.blockInteractionShouldCancel(e.getPlayer(), e.getBlock()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void blockPlaceEvent(BlockPlaceEvent e) {
        e.setCancelled(EventUtility.blockInteractionShouldCancel(e.getPlayer(), e.getBlock()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        e.setCancelled(EventUtility.blockInteractionShouldCancel(e.getPlayer(), e.getClickedBlock()));
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
                e.setCancelled(EventUtility.chunkAttributeIsDisabled(e.getTo().getChunk(), Tag.ENTER));
                if (from.isClaimed()) {
                    if (!to.getEnclave().getUUID().toString().equals(from.getEnclave().getUUID().toString())) {
                        MessageUtils.sendAction(e.getPlayer(), String.format("&aNow entering %s&a.", to.getEnclave().getDisplayName()));
                    }
                } else {
                    MessageUtils.sendAction(e.getPlayer(), String.format("&aNow entering %s&a.", to.getEnclave().getDisplayName()));
                }
            } else if (from.isClaimed()) {
                MessageUtils.sendAction(e.getPlayer(), "&aNow entering the wild.");
            }
        }
    }


}
