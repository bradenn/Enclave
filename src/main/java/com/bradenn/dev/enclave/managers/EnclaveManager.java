package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.EnclaveModel;
import com.bradenn.dev.enclave.models.PlayerModel;
import com.bradenn.dev.enclave.models.RegionModel;
import org.bukkit.entity.Player;

public class EnclaveManager {

    public static void createEnclave(Player player, String name) {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId());
        if (playerModel.getEnclave() == null) {
            if (validateName(name)) {
                MessageUtils.sendMessage(player, "Created the enclave '" + new EnclaveModel(player.getUniqueId(), name).getName() + "'.");
            } else {
                MessageUtils.sendMessage(player, "Your enclave name must not include any special characters.");
            }
        } else {
            MessageUtils.sendMessage(player, "You must disband your current enclave before creating a new one.");
        }
    }

    public static void disbandEnclave(Player player) {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId());
        EnclaveModel enclaveModel = playerModel.getEnclave();

        if (enclaveModel.getOwner() != null) {
            if (enclaveModel.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                MessageUtils.sendMessage(player, "Disbanded the enclave '" + enclaveModel.getName() + "'.");
                enclaveModel.disbandEnclave();
            } else {
                MessageUtils.sendMessage(player, "You must be the owner to disband an enclave; use '/e leave' to leave the enclave.");
            }
        } else {
            MessageUtils.sendMessage(player, "You must create an Enclave before you can disband it.");
        }
    }

    public static void claimRegion(Player player) {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId());
        RegionModel regionModel = new RegionModel(player.getLocation().getChunk(), player.getWorld());
        if (regionModel.isClaimed()) {
            MessageUtils.sendMessage(player, "This region is already claimed.");
        } else {
            if (playerModel.getEnclave() != null) {
                if (playerModel.getEnclave().getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                    regionModel.claimChunk(playerModel.getEnclave().getUUID());
                    MessageUtils.sendMessage(player, "Region claimed.");
                } else {
                    MessageUtils.sendMessage(player, "You must be the enclave owner to claim land.");
                }
            } else {
                MessageUtils.sendMessage(player, "You must create an Enclave before you can claim land; use '/e create [name]'");
            }
        }
    }

    public static void unclaimRegion(Player player) {
        PlayerModel playerModel = new PlayerModel(player.getUniqueId());
        RegionModel regionModel = new RegionModel(player.getLocation().getChunk(), player.getWorld());
        if (!regionModel.isClaimed()) {
            MessageUtils.sendMessage(player, "This region is not claimed.");
        } else {
            if (playerModel.getEnclave() != null) {
                if (playerModel.getEnclave().getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                    if (regionModel.getEnclave().getUUID().toString().equalsIgnoreCase(playerModel.getEnclave().getUUID().toString())) {
                        regionModel.unclaimChunk(playerModel.getEnclave().getUUID());
                        MessageUtils.sendMessage(player, "Region unclaimed.");
                    } else {
                        MessageUtils.sendMessage(player, "This region does not belong to your enclave.");
                    }
                } else {
                    MessageUtils.sendMessage(player, "You must be the enclave owner to unclaim land.");
                }
            } else {
                MessageUtils.sendMessage(player, "You must create an Enclave before you can unclaim land; use '/e create [name]'");
            }
        }
    }

    /**
     * Enclave Utils
     */
    private static boolean validateName(String name) {
        String pattern = "^[a-zA-Z0-9]*$";
        return name.matches(pattern);
    }
}
