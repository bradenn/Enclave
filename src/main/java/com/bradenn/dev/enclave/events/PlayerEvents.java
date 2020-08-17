package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.managers.PlayerManager;
import com.bradenn.dev.enclave.models.PlayerModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerModel pm = new PlayerModel(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        PlayerModel pm = new PlayerModel(e.getPlayer().getUniqueId());
        if (pm.getEnclave() != null) {
            e.getPlayer().sendMessage(pm.getEnclave().getName());
        }else{
            e.getPlayer().sendMessage("You have no enclave bb");
        }
    }

}
