package com.bradenn.dev.enclave.hooks;

import com.bradenn.dev.enclave.Main;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.PlayerModel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderApiHook extends PlaceholderExpansion {

    private final Main plugin;

    public PlaceholderApiHook(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "enclave";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (player == null) return "";

        if (identifier.equals("name")) {
            PlayerModel playerModel = new PlayerModel(player.getUniqueId());
            try {
                EnclaveModel enclaveModel = playerModel.getEnclave();
                if (enclaveModel != null) {
                    return enclaveModel.getDisplayName();
                }
                return "";
            }catch (Exception e){
                return "";
            }
        }
        if (identifier.equals("name2")) {
            PlayerModel playerModel = new PlayerModel(player.getUniqueId());
            EnclaveModel enclaveModel = playerModel.getEnclave();
            if (enclaveModel != null) {
                return playerModel.getEnclave().getDisplayName() + " " + ChatColor.GRAY + ":";
            }
            return "";
        }

        return null;
    }
}