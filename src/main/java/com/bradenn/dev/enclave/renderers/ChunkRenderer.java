package com.bradenn.dev.enclave.renderers;

import com.bradenn.dev.enclave.models.RegionModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
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
        World world = chunk.getWorld();
        if (rm.isClaimed()) {
            ParticleDust particleDust = new ParticleDust(ChatColor.of(rm.getEnclave().getColor()));
            for (double i = 0; i < 16; i += 1) {
                double cx = chunk.getX() * 16;
                double cz = chunk.getZ() * 16;
                double cy = Objects.requireNonNull(world).getHighestBlockYAt((int) cx, (int) cz) + 1.5;

                Location north = new Location(world, cx + i, cy, cz);
                north.setY(world.getHighestBlockYAt(north.getBlockX(), north.getBlockZ()) + 1.5);
                north.add(0.5, 0, 0.5);
                particleDust.spawn(north);

                Location south = new Location(world, cx + i, cy, cz + 15);
                south.add(0.5, 0, 0.5);
                south.setY(world.getHighestBlockYAt(south.getBlockX(), south.getBlockZ()) + 1.5);
                particleDust.spawn(south);

                Location west = new Location(world, cx, cy, cz + i);
                west.add(0.5, 0, 0.5);
                west.setY(world.getHighestBlockYAt(west.getBlockX(), west.getBlockZ()) + 1.5);
                particleDust.spawn(west);

                Location east = new Location(world, cx + 15, cy, cz + i);
                east.setY(world.getHighestBlockYAt(east.getBlockX(), east.getBlockZ()) + 1.5);
                east.add(0.5, 0, 0.5);
                particleDust.spawn(east);

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
