package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.renderers.ChunkRenderer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runtime {

    private enum TaskFrequency {
        TICK(2),
        SECOND(20),
        ;

        long period;

        TaskFrequency(long period) {
            this.period = period;
        }

        public long getPeriod() {
            return period;
        }
    }

    public enum PlayerTask {
        SHOW_PARTICLE_BORDER(TaskFrequency.TICK),
        SHOW_PARTICLE_IDENTIFIER(TaskFrequency.TICK),
        ;

        TaskFrequency taskFrequency;

        PlayerTask(TaskFrequency taskFrequency) {
            this.taskFrequency = taskFrequency;
        }

        public TaskFrequency getFrequency() {
            return taskFrequency;
        }
    }

    private final Map<PlayerTask, List<Player>> playerTasks = new HashMap<>();
    private final Plugin plugin;

    public Runtime(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Clear all playerTasks.
     */
    public int clear() {
        int total = 0;
        for (PlayerTask playerTask : PlayerTask.values()) {
            total += playerTasks.get(playerTask).size();
            playerTasks.put(playerTask, new ArrayList<>());
        }
        return total;
    }

    /**
     * Toggle the operation of a task for a player.
     * @param player The player in question
     * @param playerTask The task to be run
     */
    public boolean togglePlayerTask(Player player, PlayerTask playerTask) {
        List<Player> taskList = playerTasks.get(playerTask);
        if (taskList == null) return false;
        if (taskList.contains(player)) {
            taskList.remove(player);
            playerTasks.put(playerTask, taskList);
            return false;
        } else {
            taskList.add(player);
            playerTasks.put(playerTask, taskList);
            return true;
        }
    }

    /**
     * Run a specific task, for a given player.
     * @param player The player in question
     * @param playerTask The task to be run
     */
    private void runPlayerTask(Player player, PlayerTask playerTask) {
        switch (playerTask) {
            case SHOW_PARTICLE_BORDER:
                new ChunkRenderer(player, ChunkRenderer.ChunkEffect.OUTLINE).render();
                break;
            case SHOW_PARTICLE_IDENTIFIER:
                new ChunkRenderer(player, ChunkRenderer.ChunkEffect.BORDER).render();
                break;
            default:
                break;
        }
    }

    /**
     * Start timers for each of the the player tasks.
     */
    public void run() {
        for (PlayerTask playerTask : PlayerTask.values()) {
            playerTasks.put(playerTask, new ArrayList<>());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (playerTasks.get(playerTask) != null) {
                        playerTasks.get(playerTask).forEach(player -> runPlayerTask(player, playerTask));
                    }
                }
            }.runTaskTimer(plugin, 0, playerTask.getFrequency().getPeriod());
        }

    }

}
