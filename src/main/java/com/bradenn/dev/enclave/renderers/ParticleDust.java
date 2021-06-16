package com.bradenn.dev.enclave.renderers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleDust {

    private final Particle.DustOptions dustOptions;

    public ParticleDust(ChatColor chatColor) {
        java.awt.Color c = chatColor.getColor();
        this.dustOptions = new Particle.DustOptions(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()), 1);
    }

    public void spawn(Location location) {
        World world = location.getWorld();
        if(world == null) return;
        world.spawnParticle(Particle.REDSTONE, location, 0, dustOptions);
    }

}
