package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.models.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class WorldEvents implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onExplode(EntityExplodeEvent e) {
        e.setCancelled(EventUtility.chunkAttributeIsDisabled(e.getLocation().getChunk(), Tag.EXPLOSIONS));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpread(BlockIgniteEvent e) {
        if (e.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            e.setCancelled(EventUtility.chunkAttributeIsDisabled(e.getBlock().getChunk(), Tag.FIRE_SPREAD));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBurn(BlockBurnEvent e) {
        e.setCancelled(EventUtility.chunkAttributeIsDisabled(e.getBlock().getChunk(), Tag.FIRE_SPREAD));
    }

}
