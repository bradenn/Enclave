package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.models.EnclaveTag;
import com.bradenn.dev.enclave.utilities.InteractionUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class WorldEvents implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (InteractionUtility.invalidEvent(e.getLocation().getChunk(), EnclaveTag.EXPLOSIONS)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpread(BlockIgniteEvent e) {
        if (e.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            if (InteractionUtility.invalidEvent(e.getBlock().getChunk(), EnclaveTag.FIRE_SPREAD)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBurn(BlockBurnEvent e) {
        if (InteractionUtility.invalidEvent(e.getBlock().getChunk(), EnclaveTag.FIRE_SPREAD)) {
            e.setCancelled(true);
        }
    }

}
