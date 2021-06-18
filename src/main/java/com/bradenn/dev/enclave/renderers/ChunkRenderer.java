package com.bradenn.dev.enclave.renderers;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ChunkRenderer {

    public enum ChunkEffect {
        OUTLINE,
        IDENTIFIER
    }

    private final Player target;
    private final ChunkEffect effect;

    public ChunkRenderer(Player target, ChunkEffect effect) {
        this.target = target;
        this.effect = effect;
    }

    public void render() {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                Chunk chunk = target.getLocation().add(16 * i, 0, 16 * j).getChunk();
                switch (effect) {
                    case OUTLINE:
                        ParticleUtils.outlineChunk(chunk);
                        break;
                    case IDENTIFIER:
                        ParticleUtils.identifyChunk(chunk);
                        break;
                    default:
                        break;
                }
            }
        }

    }

}
