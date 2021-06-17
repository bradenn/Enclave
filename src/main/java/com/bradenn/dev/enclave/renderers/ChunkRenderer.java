package com.bradenn.dev.enclave.renderers;

import com.bradenn.dev.enclave.models.RegionModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;

import java.util.Objects;

public class ChunkRenderer {

    private final Player target;
    private int radius;

    public ChunkRenderer(Player target, int radius) {
        this.target = target;
    }

    public void drawText(Location location, String text) {
        ParticleDust xMarkerDust = new ParticleDust(ChatColor.of("#2c2c2e"));

        for (byte aByte : text.getBytes()) {

            MapFont.CharacterSprite sprite = MinecraftFont.Font.getChar((char) aByte);

            int width = sprite.getWidth();
            int height = sprite.getHeight();
            double scale = 0.25;

            for (double x = 0; x < width; x++) {
                for (double y = 0; y < height; y++) {

                    if (sprite.get((int)y, (int)x)) {
                        xMarkerDust.spawn(location.clone().add(x * scale, 0, y * scale));
                    }

                }
            }

            location.add(width * scale, 0, 0);
        }
    }

    public void outlineChunk(Chunk chunk) {
        RegionModel rm = new RegionModel(chunk);
        World world = chunk.getWorld();
        if (rm.isClaimed()) {
            double cx = chunk.getX() * 16;
            double cz = chunk.getZ() * 16;

            ParticleDust xMarkerDust = new ParticleDust(ChatColor.of("#ff453a"));
            ParticleDust yMarkerDust = new ParticleDust(ChatColor.of("#0a84ff"));
            ParticleDust zMarkerDust = new ParticleDust(ChatColor.of("#8fd158"));

            drawText(new Location(world, cx + 1, Objects.requireNonNull(world).getHighestBlockYAt((int) cx, (int) cz) + 1, cz + 1), String.format("[%d,%d]", chunk.getX(), chunk.getZ()));
            ParticleDust particleDust = new ParticleDust(ChatColor.of(rm.getEnclave().getColor()));
            for (double i = 0; i < 16; i += 1) {

                double cy = Objects.requireNonNull(world).getHighestBlockYAt((int) cx, (int) cz) + 1.5;

                Location nexus = new Location(world, cx, 0, cz);

                xMarkerDust.spawn(nexus.clone().add(i / 2, world.getHighestBlockYAt(nexus.getBlockX(), nexus.getBlockZ()) + 1, 0));
                yMarkerDust.spawn(nexus.clone().add(0, world.getHighestBlockYAt(nexus.getBlockX(), nexus.getBlockZ()) + 1 + i / 2, 0));
                zMarkerDust.spawn(nexus.clone().add(0, world.getHighestBlockYAt(nexus.getBlockX(), nexus.getBlockZ()) + 1, i / 2));

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
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                outlineChunk(target.getLocation().add(16 * i, 0, 16 * j).getChunk());
            }
        }

    }

}
