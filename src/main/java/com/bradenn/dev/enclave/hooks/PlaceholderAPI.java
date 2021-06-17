package com.bradenn.dev.enclave.hooks;

import com.bradenn.dev.enclave.Main;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.PlayerModel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

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
  public String getAuthor() {
    return plugin.getDescription().getAuthors().toString();
  }

  @Override
  public String getIdentifier() {
    return "enclave";
  }

  @Override
  public String getVersion() {
    return plugin.getDescription().getVersion();
  }

  @Override
  public String onPlaceholderRequest(Player player, String identifier) {

    if (player == null) {
      return "";
    }
    if (identifier.equals("name")) {
      PlayerModel playerModel = new PlayerModel(player.getUniqueId());
      if (playerModel != null) {
        EnclaveModel enclaveModel = playerModel.getEnclave();
        return ChatColor.of(enclaveModel.getColor()) + enclaveModel.getName();
      }
      return "";
    }
    return null;
  }
}
