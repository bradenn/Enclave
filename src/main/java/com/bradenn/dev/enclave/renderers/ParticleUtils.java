package com.bradenn.dev.enclave.renderers;

import com.bradenn.dev.enclave.Main;
import com.bradenn.dev.enclave.models.RegionModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class ParticleUtils {

    public static void chunkClaim(Chunk chunk, Location location) {
        ParticleDust xMarkerDust = new ParticleDust(ChatColor.of("#0a84ff"));
        ParticleDust yMarkerDust = new ParticleDust(ChatColor.of("#64d2ff"));

        int chunkX = chunk.getX() * 16;
        int chunkZ = chunk.getZ() * 16;

        new BukkitRunnable() {
            double scale = 0;

            @Override
            public void run() {
                for (double i = 0; i <= 16; i += 0.5) {
                    double circle = (Math.PI * 2)/16;
                    double x = Math.cos(circle * i) * scale;
                    double y = Math.sin(circle * i) * scale;
                    Location point = location.clone().add(x, 0.25, y);

                    if(point.getX() <= chunkX || point.getX() >= chunkX + 16) continue;
                    if(point.getZ() <= chunkZ || point.getZ() >= chunkZ + 16) continue;


                    if (scale % 4 == 0) {
                        xMarkerDust.spawn(point);
                    } else {
                        yMarkerDust.spawn(point);
                    }
                }
                scale += 1;
                if (scale >= 8) {
                    animateWall(chunk, location.getY(), 0);
                    animateWall(chunk, location.getY(), 1);
                    animateWall(chunk, location.getY(), 2);
                    animateWall(chunk, location.getY(), 3);
                    cancel();
                }
            }

        }.runTaskTimer(Main.plugin, 0, 1/2);

    }

    public static void animateWall(Chunk chunk, double y, int face) {
        Location location = new Location(chunk.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16);
        location.setY(chunk.getWorld().getHighestBlockYAt(chunk.getX() * 16, chunk.getZ() * 16));

        ParticleDust xMarkerDust = new ParticleDust(ChatColor.of("#0a84ff"));
        ParticleDust yMarkerDust = new ParticleDust(ChatColor.of("#64d2ff"));

        if(y >= location.getY()){
            location.setY(y - 3);
        }

        new BukkitRunnable() {
            double scanLine = 0;

            @Override
            public void run() {

                for (double i = 0; i < 16; i += 0.5) {
                    double y = scanLine + Math.abs(Math.sin((2 * Math.PI / 16) * i + i) / 4);

                    Location point = location;
                    switch (face) {
                        case 0: // East
                            point = location.clone().add(0, y, i);
                            break;
                        case 1: // South
                            point = location.clone().add(i, y, 0);
                            break;
                        case 2: // West
                            point = location.clone().add(16, y, i);
                            break;
                        case 3: // North
                            point = location.clone().add(-i + 16, y, 16);
                            break;
                    }

                    if (scanLine % 2 == 0) {
                        xMarkerDust.spawn(point);
                    } else {
                        yMarkerDust.spawn(point);
                    }
                }

                scanLine += 0.5;
                if (scanLine >= 8) {
                    scanLine = 0;
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);

    }
    public static void chunkWall(Chunk chunk, Location location, int face) {
        new BukkitRunnable() {
            int itr = 0;

            @Override
            public void run() {
                animateWall(chunk, location.getY(), face);

                itr++;
                if (itr >= 2) {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);
    }

    public static void playerWall(Chunk chunk, Location location, int face) {
        ParticleDust xMarkerDust = new ParticleDust(ChatColor.of("#0a84ff"));
        ParticleDust yMarkerDust = new ParticleDust(ChatColor.of("#64d2ff"));

        new BukkitRunnable() {
            double scale = 4;

            @Override
            public void run() {
                int chunkX = chunk.getX() * 16;
                int chunkZ = chunk.getZ() * 16;
                for (double i = 0; i <= 8; i += 0.5) {
                    for (double radius = 1.2; radius <= 2; radius += 0.4) {
                        double pi2 = Math.PI * 2;
                        double x = Math.cos((pi2 / 8) * i) * radius * 0.75;
                        double y = Math.sin((pi2 / 8) * i) * radius + 1;

                        double indent = Math.abs(Math.sin((pi2 / 8) * radius + Math.PI / 2)) / scale + 0.25;

                        Location point = location;
                        switch (face) {
                            case 0: // East
                                point = location.clone().add(indent, y, x);
                                break;
                            case 1: // South
                                point = location.clone().add(x, y, indent);
                                break;
                            case 2: // West
                                point = location.clone().add(-indent, y, -x);
                                break;
                            case 3: // North
                                point = location.clone().add(-x, y, -indent);
                                break;
                        }

                        if(!(point.getX() >= chunkX && point.getX() <= chunkX + 16) || !(point.getZ() >= chunkZ && point.getZ() <= chunkZ + 16)){
                            continue;
                        }
                        if (scale % 1 == 0) {
                            xMarkerDust.spawn(point);
                        } else {
                            yMarkerDust.spawn(point);
                        }

                    }
                }
                scale -= 0.4;
                if (scale <= 0.8) {
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);

    }

    public static void identifyChunk(Chunk chunk) {
        World world = chunk.getWorld();

        double cx = chunk.getX() * 16;
        double cz = chunk.getZ() * 16;
        double cy = Objects.requireNonNull(world).getHighestBlockYAt((int) cx, (int) cz);

        ParticleDust xMarkerDust = new ParticleDust(ChatColor.of("#ff453a"));
        ParticleDust yMarkerDust = new ParticleDust(ChatColor.of("#0a84ff"));
        ParticleDust zMarkerDust = new ParticleDust(ChatColor.of("#8fd158"));

        Location chunkTextLocation = new Location(world, cx + 1, cy + 1, cz + 1);
        String chunkString = String.format("[%d,%d]", chunk.getX(), chunk.getZ());
        ParticleUtils.drawText(chunkTextLocation, chunkString);

        Location northLocation = new Location(world, cx + 7.5, cy + 1, cz + 3);
        String northString = "N";
        ParticleUtils.drawText(northLocation, northString);

        Location southLocation = new Location(world, cx + 7.5, cy + 1, cz + 11);
        String southString = "S";
        ParticleUtils.drawText(southLocation, southString);

        Location westLocation = new Location(world, cx + 3.5, cy + 1, cz + 7);
        String westString = "W";
        ParticleUtils.drawText(westLocation, westString);

        Location eastLocation = new Location(world, cx + 11.5, cy + 1, cz + 7);
        String eastString = "E";
        ParticleUtils.drawText(eastLocation, eastString);

        for (double i = 0; i < 8; i += 1) {
            Location nexus = new Location(world, cx + 0.5, 0, cz + 0.5);
            xMarkerDust.spawn(nexus.clone().add(i, world.getHighestBlockYAt(nexus.getBlockX(), nexus.getBlockZ()) + 1, 0));
            yMarkerDust.spawn(nexus.clone().add(0, world.getHighestBlockYAt(nexus.getBlockX(), nexus.getBlockZ()) + 1 + i, 0));
            zMarkerDust.spawn(nexus.clone().add(0, world.getHighestBlockYAt(nexus.getBlockX(), nexus.getBlockZ()) + 1, i));
        }
    }

    public static void outlineChunk(Chunk chunk) {
        RegionModel rm = new RegionModel(chunk);
        World world = chunk.getWorld();
        if (rm.isClaimed()) {
            double cx = chunk.getX() * 16;
            double cz = chunk.getZ() * 16;

            ParticleDust particleDust = new ParticleDust(ChatColor.of(rm.getEnclave().getColor()));
            for (double i = 0; i < 16; i += 1) {
                double cy = Objects.requireNonNull(world).getHighestBlockYAt((int) cx, (int) cz) + 1.5;

                Location north = new Location(world, cx + i, cy, cz);
                north.setY(world.getHighestBlockYAt(north.getBlockX(), north.getBlockZ()) + 1);
                north.add(0.5, 0, 0.5);
                particleDust.spawn(north);

                Location south = new Location(world, cx + i, cy, cz + 15);
                south.add(0.5, 0, 0.5);
                south.setY(world.getHighestBlockYAt(south.getBlockX(), south.getBlockZ()) + 1);
                particleDust.spawn(south);

                Location west = new Location(world, cx, cy, cz + i);
                west.add(0.5, 0, 0.5);
                west.setY(world.getHighestBlockYAt(west.getBlockX(), west.getBlockZ()) + 1);
                particleDust.spawn(west);

                Location east = new Location(world, cx + 15, cy, cz + i);
                east.setY(world.getHighestBlockYAt(east.getBlockX(), east.getBlockZ()) + 1);
                east.add(0.5, 0, 0.5);
                particleDust.spawn(east);

            }
        }
    }

    public static void drawText(Location location, String text) {
        ParticleDust xMarkerDust = new ParticleDust(ChatColor.of("#2c2c2e"));
        if (MinecraftFont.Font.isValid(text)) {
            for (byte aByte : text.getBytes()) {
                MapFont.CharacterSprite sprite = MinecraftFont.Font.getChar((char) aByte);
                if (sprite == null) break;
                int width = sprite.getWidth();
                int height = sprite.getHeight();
                double scale = 0.25;
                for (double x = 0; x < width; x++) {
                    for (double y = 0; y < height; y++) {
                        if (sprite.get((int) y, (int) x)) {
                            xMarkerDust.spawn(location.clone().add(x * scale, 0, y * scale));
                        }
                    }
                }
                location.add(width * scale, 0, 0);
            }
        }
    }
}
