package com.bradenn.dev.enclave.renderers;

import com.bradenn.dev.enclave.models.RegionModel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EnclaveMap extends MapRenderer {

    static public final float map(float value,
                                  float istart,
                                  float istop,
                                  float ostart,
                                  float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        try {


        int absCenterX = mapView.getCenterX();
        int absCenterZ = mapView.getCenterZ();
        MapCursorCollection mcc = new MapCursorCollection();
        mapView.setTrackingPosition(true);


        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
                Chunk chunk = player.getWorld().getChunkAt(player.getLocation().add(i * 16, 0,j * 16));
                RegionModel rm = new RegionModel(chunk);


                int translatedX = (int) map(chunk.getX() * 16, absCenterX - 64, absCenterX + 64, 0, 128);
                int translatedZ = (int) map(chunk.getZ() * 16, absCenterZ - 64, absCenterZ + 64, 0, 128);

                int translatedSX = (int) map(chunk.getX() * 16 + 8, absCenterX - 64, absCenterX + 64, -128, 128);
                int translatedSZ = (int) map(chunk.getZ() * 16 + 8, absCenterZ - 64, absCenterZ + 64, -128, 128);

                if (rm.isClaimed()) {
                    if(translatedSX <= 128 && translatedSX >= -128 && translatedSZ <= 128 && translatedSZ >= -128){
                        MapCursor mc = new MapCursor((byte) translatedSX, (byte)translatedSZ, (byte)0, MapCursor.Type.SMALL_WHITE_CIRCLE, true);
                        mc.setCaption(rm.getEnclave().getDisplayName());
                        mcc.addCursor(mc);
                    }


                    highLabelChunk(mapCanvas, translatedX, translatedZ, ChatColor.of(rm.getEnclave().getColor()).getColor());
                } else {
                    clearChunk(mapCanvas, translatedX, translatedZ);
                }

            }
        }

        mapCanvas.setCursors(mcc);

        mapView.setScale(MapView.Scale.CLOSEST);
        mapView.setCenterX(player.getLocation().getBlockX());
        mapView.setCenterZ(player.getLocation().getBlockZ());
        }catch(java.lang.IllegalStateException ignored){
            
        }
    }


    public void highLabelChunk(MapCanvas mapCanvas, int x, int z, Color color) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (i == 0 || j == 0 || i == 15 || j == 15) {
                    mapCanvas.setPixel(x + i, z + j, MapPalette.matchColor(color));
                } else {
                    mapCanvas.setPixel(x + i, z + j, mapCanvas.getBasePixel(x + i, z + j));
                }
            }
        }
    }

    public void clearChunk(MapCanvas mapCanvas, int x, int z) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapCanvas.setPixel(x + i, z + j, mapCanvas.getBasePixel(x + i, z + j));
            }
        }
    }
}
