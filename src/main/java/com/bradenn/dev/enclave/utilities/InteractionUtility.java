package com.bradenn.dev.enclave.utilities;

import com.bradenn.dev.enclave.messages.MessageUtils;
import com.bradenn.dev.enclave.models.EnclaveTag;
import com.bradenn.dev.enclave.models.RegionModel;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class InteractionUtility {

    public static boolean invalidEvent(Chunk chunk, EnclaveTag tag) {
        RegionModel rm = new RegionModel(chunk, chunk.getWorld());
        if (rm.isClaimed()) {
            return rm.getEnclave().checkTag(tag);
        } else {
            return false;
        }
    }

    public static boolean invalidInteraction(Player player, Chunk chunk) {
        RegionModel rm = new RegionModel(chunk, chunk.getWorld());
        if (rm.isClaimed()) {
            if (rm.getEnclave().hasMember(player.getUniqueId())) {
                return false;
            } else if(player.hasPermission("enclaves.override")){
                return false;
            }else{
                MessageUtils.sendError(player, "This region is claimed by " + rm.getEnclave().getName() + ".");
                return true;
            }
        } else {
            return false;
        }
    }

}
