package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.models.PlayerModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        new PlayerModel(e.getUniqueId());
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        e.setCancelled(EventUtility.pvpShouldCancel(e));
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
