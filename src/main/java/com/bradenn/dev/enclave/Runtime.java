package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.renderers.ChunkRenderer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Runtime {

    public static List<Player> chunkRenderers = new ArrayList<>();

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                chunkRenderers.forEach(player -> new ChunkRenderer(player, 3).render());
            }
        }.runTaskTimer(Main.plugin, 0, 2);
    }

}
