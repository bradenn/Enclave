package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.managers.PlayerManager;
import com.bradenn.dev.enclave.managers.RegionManager;
import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.models.RegionModel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Objects;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerModel pm = new PlayerModel(e.getPlayer().getUniqueId());
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
