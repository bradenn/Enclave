package com.bradenn.dev.enclave.renderers;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleRenderer {

    public static void beamUp(Location location, java.awt.Color c) {
        for (double i = 0.0; i < (2*Math.PI)*8; i+=(2*Math.PI)/64) {
            double z = i/(Math.PI*8);
            Particle.DustOptions du = new Particle.DustOptions(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()), 1);
            location.getWorld().spawnParticle(Particle.REDSTONE, location.getX() + Math.cos(i)/2, location.getY() + z, location.getZ() + Math.sin(i)/2, 1, 0,0,0, du);
        }

    }

}
