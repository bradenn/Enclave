package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.models.EnclaveTag;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.utilities.InteractionUtility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        PlayerModel pm = new PlayerModel(e.getUniqueId());
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player){
            if(e.getDamager() instanceof Player){
                if(InteractionUtility.invalidEvent(e.getEntity().getLocation().getChunk(), EnclaveTag.PVP) || InteractionUtility.invalidEvent(e.getDamager().getLocation().getChunk(), EnclaveTag.PVP)){
                    e.setCancelled(true);
                }
            }else if(e.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow)e.getDamager();
                if(arrow.getShooter() instanceof Player){
                    if(InteractionUtility.invalidEvent(e.getEntity().getLocation().getChunk(), EnclaveTag.PVP) || InteractionUtility.invalidEvent(((Player) arrow.getShooter()).getPlayer().getLocation().getChunk(), EnclaveTag.PVP)){
                        arrow.remove();
                        e.setCancelled(true);
                    }
                }
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
