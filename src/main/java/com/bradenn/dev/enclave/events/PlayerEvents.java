package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.models.PlayerModel;
import org.bukkit.ChatColor;
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
        PlayerModel playerModel = new PlayerModel(e.getPlayer().getUniqueId());
        if (playerModel.getEnclave() != null) {
            e.setFormat(e.getFormat()
                .replace("{ENCLAVE_DISPLAYNAME}", playerModel.getEnclave().getDisplayName()));
            } else {
            e.setFormat(e.getFormat().replace("{ENCLAVE_DISPLAYNAME}", ""));
        }
    }


}
