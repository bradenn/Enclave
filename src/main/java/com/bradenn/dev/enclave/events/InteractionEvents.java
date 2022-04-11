package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.models.Tag;
import com.bradenn.dev.enclave.renderers.ParticleUtils;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class InteractionEvents implements Listener {

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        e.setCancelled(EventUtility.blockInteractionShouldCancel(e.getPlayer(), e.getBlock()));
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        e.setCancelled(EventUtility.blockInteractionShouldCancel(e.getPlayer(), e.getBlock()));
    }

    @EventHandler
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

            float x = chunkTo.getX() - chunkFrom.getX();
            float z = chunkTo.getZ() - chunkFrom.getZ();

            RegionModel from = new RegionModel(chunkFrom);
            RegionModel to = new RegionModel(chunkTo);

            if (to.isClaimed()) {
                if (from.isClaimed()) {
                    if (!to.getEnclave().getUUID().toString().equals(from.getEnclave().getUUID().toString())) {
                        MessageUtils.sendAction(e.getPlayer(), String.format("&aNow entering %s&a.", to.getEnclave().getDisplayName()));
                    } else {
                        return;
                    }
                } else {
                    MessageUtils.sendAction(e.getPlayer(), String.format("&aNow entering %s&a.", to.getEnclave().getDisplayName()));
                }
                int face = 0;

                if (x == -1 && z == 0) {
                    face = 2; // West
                } else if (x == 0 && z == 1) {
                    face = 1; // South
                } else if (x == 0 && z == -1) {
                    face = 3; // North
                }
                if (EventUtility.chunkAttributeIsDisabled(e.getTo().getChunk(), Tag.ENTER)) {
                    if (to.getEnclave().hasMember(e.getPlayer().getUniqueId()) || e.getPlayer().hasPermission("enclave.admin")) {
                        ParticleUtils.chunkWall(e.getTo().getChunk(), e.getPlayer().getLocation(), face);
                        ParticleUtils.playerWall(e.getTo().getChunk(), e.getPlayer().getLocation(), face);
                    } else {
                        ParticleUtils.chunkWall(e.getTo().getChunk(), e.getPlayer().getLocation(), face);
                        Vector vector = new Vector(x, 0, z).multiply(-2).normalize();
                        e.getPlayer().setVelocity(vector);
                        e.setCancelled(true);
                    }
                }
            } else if (from.isClaimed()) {
                MessageUtils.sendAction(e.getPlayer(), "&aNow entering the wild.");
            }

        }
    }

}
