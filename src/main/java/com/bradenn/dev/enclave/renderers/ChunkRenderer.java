package com.bradenn.dev.enclave.renderers;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ChunkRenderer {

    public enum ChunkEffect {
        OUTLINE,
        BORDER
    }

    private final Player target;
    private final ChunkEffect effect;

    public ChunkRenderer(Player target, ChunkEffect effect) {
        this.target = target;
        this.effect = effect;
    }

    public void render() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Chunk chunk = target.getLocation().add(i << 4, 0, j << 4).getChunk();
                switch (effect) {
                    case OUTLINE:
                        ParticleUtils.outlineChunk(chunk);
                        break;
                    case BORDER:
                        ParticleUtils.identifyChunk(chunk);
                        break;
                    default:
                        break;
                }
            }
        }

    }

}
