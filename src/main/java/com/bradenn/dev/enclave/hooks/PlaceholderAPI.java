package com.bradenn.dev.enclave.hooks;

import com.bradenn.dev.enclave.Main;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.PlayerModel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final Main plugin;

    public PlaceholderAPI(Main plugin) {
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
            EnclaveModel enclaveModel = playerModel.getEnclave();
            if (enclaveModel != null) {
                return enclaveModel.getDisplayName();
            }
            return "";
        }

        return null;
    }
}
