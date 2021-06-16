package com.bradenn.dev.enclave.renderers;

import com.bradenn.dev.enclave.models.RegionModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ChunkRenderer {

    private final Player target;
    private int radius;

    public ChunkRenderer(Player target, int radius) {
        this.target = target;
    }

    public void outlineChunk(Chunk chunk) {
        RegionModel rm = new RegionModel(chunk);
        if (rm.isClaimed()) {
            World world = chunk.getWorld();
            java.awt.Color c = ChatColor.of(rm.getEnclave().getColor()).getColor();
            Particle.DustOptions du = new Particle.DustOptions(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()), 1);
            for (double i = 0; i < 16; i += 0.5) {
                int cx = chunk.getX() * 16;
                int cz = chunk.getZ() * 16;
                double cy = Objects.requireNonNull(world).getHighestBlockYAt(cx, cz) + 1.5;
                world.spawnParticle(Particle.REDSTONE, cx + i, cy, cz, 0, 0, 0, 0, du);
                world.spawnParticle(Particle.REDSTONE, cx, cy, cz + i, 0, 0, 0, 0, du);
                world.spawnParticle(Particle.REDSTONE, cx + 16, cy, cz + i, 0, 0, 0, 0, du);
                world.spawnParticle(Particle.REDSTONE, cx + i, cy, cz + 16, 0, 0, 0, 0, du);
            }


        }
    }

    public void render() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                outlineChunk(target.getLocation().add(16 * i, 0, 16 * j).getChunk());
            }
        }

    }

}
