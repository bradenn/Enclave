package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.renderers.ChunkRenderer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Runtime {

    public static List<Player> chunkBorders = new ArrayList<>();
    public static List<Player> chunkIdentifiers = new ArrayList<>();

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                chunkBorders.forEach(player -> new ChunkRenderer(player, ChunkRenderer.ChunkEffect.OUTLINE).render());
                chunkIdentifiers.forEach(player -> new ChunkRenderer(player, ChunkRenderer.ChunkEffect.IDENTIFIER).render());
            }
        }.runTaskTimer(Main.plugin, 0, 2);
    }

}
