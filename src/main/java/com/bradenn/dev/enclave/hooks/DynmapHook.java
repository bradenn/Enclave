package com.bradenn.dev.enclave.hooks;

import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.io.FileFilter;
import java.util.List;

public class DynmapHook {

    private final DynmapAPI api;
    private MarkerSet set;

    /*
     * Connect to dynmap plugin
     */
    public DynmapHook() {
        PluginManager pm = Bukkit.getServer().getPluginManager();

        Plugin dynmap = pm.getPlugin("dynmap");
        if (dynmap == null) {
            Bukkit.broadcastMessage("Cannot find dynmap!");
        }
        api = (DynmapAPI) dynmap;
        loadMarkerApi();
    }

    /*
     * Create Marker instance
     */
    private void loadMarkerApi() {
        MarkerAPI markerAPI = api.getMarkerAPI();
        if (markerAPI == null) {
            Bukkit.broadcastMessage("Error loading dynmap marker API!");
        }
        set = markerAPI.getMarkerSet("claims");
        if (set == null)
            set = markerAPI.createMarkerSet("claims", "Enclaves", null, false);
        else
            set.setMarkerSetLabel("Enclaves");
    }

    /*
     *   Get all chunks belonging to guilds, then render them with markers
     */
    public void reloadAllChunks() {
        List<RegionModel> regionModels = new RegionModel().getRegionModels();
        for (RegionModel model : regionModels) {
            reloadChunk(model.getChunk());
        }
    }

    private String buildEnclaveMarkup(EnclaveModel enclaveModel) {
        return "<b>" + enclaveModel.getName() + "</b>" +
                "<br>Members: " + enclaveModel.getMemberCount();
    }


    /*
     * Reload on chunk's marker on dynmap
     */
    public void reloadChunk(Chunk chunk) {
        if (Bukkit.getPluginManager().getPlugin("dynmap") != null) {
            RegionModel region = new RegionModel(chunk);
            double bitX = chunk.getX() * 16;
            double bitZ = chunk.getZ() * 16;
            double[] xArray = {bitX, bitX + 16};
            double[] zArray = {bitZ, bitZ + 16};
            if (region.isClaimed()) {
                EnclaveModel enclaveModel = region.getEnclave();

                int color = ColorUtils.parseColor(enclaveModel.getColor());
                String markup = buildEnclaveMarkup(enclaveModel);
                String selector = "enclave.claim." + bitX + ":" + bitZ;

                set.createAreaMarker(selector, markup, true, chunk.getWorld().getName(), xArray, zArray, false);
                set.findAreaMarker(selector).setLineStyle(2, 1, color);
                set.findAreaMarker(selector).setFillStyle(0.3, color);
            }
        }
    }
}
