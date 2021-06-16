package com.bradenn.dev.enclave.renderers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;

import java.util.Objects;

public class ParticleRenderer {

    public static void draw(Location location, ChatColor color) {
        java.awt.Color c = ChatColor.of(color.getColor()).getColor();

        Particle.DustOptions du = new Particle.DustOptions(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()), 1);
        World world = location.getWorld();
        assert world != null;
        world.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0, du);
    }

    public static void beamUp(Location location, java.awt.Color c) {
        for (double i = 0.0; i < (2*Math.PI)*8; i+=(2*Math.PI)/64) {
            double z = i/(Math.PI*8);
            Particle.DustOptions du = new Particle.DustOptions(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()), 1);
            location.getWorld().spawnParticle(Particle.REDSTONE, location.getX() + Math.cos(i)/2, location.getY() + z, location.getZ() + Math.sin(i)/2, 1, 0,0,0, du);
        }
    }

    public static void highlightChunk(Chunk chunk, java.awt.Color c) {
        for (int i = 0; i < 16; i++) {
            Particle.DustOptions du = new Particle.DustOptions(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()), 1);
            World world = chunk.getWorld();
            int cx = chunk.getX() * 16;
            int cz = chunk.getZ() * 16;
            int cy = Objects.requireNonNull(world).getHighestBlockYAt(cx, cz) + 2;
            world.spawnParticle(Particle.REDSTONE, cx + i, cy, cz, 1, 0,0,0, du);
            world.spawnParticle(Particle.REDSTONE, cx, cy, cz + i, 1, 0,0,0, du);
            world.spawnParticle(Particle.REDSTONE, cx + 16, cy, cz + i, 1, 0,0,0, du);
            world.spawnParticle(Particle.REDSTONE, cx + i, cy, cz + 16, 1, 0,0,0, du);
        }
    }

}
