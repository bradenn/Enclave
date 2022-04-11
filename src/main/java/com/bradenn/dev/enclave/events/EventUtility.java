package com.bradenn.dev.enclave.events;

import com.bradenn.dev.enclave.models.RegionModel;
import com.bradenn.dev.enclave.models.Tag;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventUtility {

    /**
     * @param player The player interacting.
     * @param block The block in question.
     * @return true if the attribute is disabled and may be cancelled.
     */
    public static boolean blockInteractionShouldCancel(Player player, Block block) {
        RegionModel rm = new RegionModel(block.getChunk());
        if(player.hasPermission("enclave.admin.interact") || player.isOp())   return false;
        if (rm.isClaimed()) return !rm.getEnclave().hasMember(player.getUniqueId());
        return false;
    }

    /**
     * @param chunk The chunk in question.
     * @param tag The tag to check withing the chunk.
     * @return true if the attribute is disabled and may be cancelled.
     */
    public static boolean chunkAttributeIsDisabled(Chunk chunk, Tag tag) {
        RegionModel rm = new RegionModel(chunk);
        if (rm.isClaimed()) {
            return rm.getEnclave().checkTag(tag);
        }
        return false;
    }

    /**
     * @param target The player being damaged.
     * @param source The player damaging the target.
     * @return true if one or both of the players are in a chunk where pvp is disabled.
     */
    public static boolean pvpIsDisabled(Entity target, Entity source) {
        Chunk targetChunk = target.getLocation().getChunk();
        Chunk sourceChunk = source.getLocation().getChunk();
        return chunkAttributeIsDisabled(targetChunk, Tag.PVP) || chunkAttributeIsDisabled(sourceChunk, Tag.PVP);
    }

    /**
     * @param e EntityDamageByEntityEvent
     * @return true if the event should be disabled.
     */
    public static boolean pvpShouldCancel(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Player) {
                return pvpIsDisabled(e.getEntity(), e.getDamager());
            } else if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    return pvpIsDisabled(e.getEntity(), e.getDamager());
                }
            }
        }
        return false;
    }

}
