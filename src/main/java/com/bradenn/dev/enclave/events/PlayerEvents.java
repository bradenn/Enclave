package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.models.EnclaveTag;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.utilities.InteractionUtility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerModel pm = new PlayerModel(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            if(InteractionUtility.invalidEvent(e.getEntity().getLocation().getChunk(), EnclaveTag.PVP)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        PlayerModel pm = new PlayerModel(e.getPlayer().getUniqueId());
        if (pm.getEnclave().isValid()) {
            e.setFormat(pm.getEnclave().getDisplayName() + " §7%s§6:" + ChatColor.of("#EBF3FF") + " %s");
        } else {
            e.getPlayer().sendMessage("You have no enclave");
        }
    }


}
