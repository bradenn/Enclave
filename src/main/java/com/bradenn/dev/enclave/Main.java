package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.commands.AdminCommand;
import com.bradenn.dev.enclave.commands.EnclaveCommand;
import com.bradenn.dev.enclave.events.InteractionEvents;
import com.bradenn.dev.enclave.events.PlayerEvents;
import com.bradenn.dev.enclave.events.WorldEvents;
import com.bradenn.dev.enclave.hooks.PlaceholderAPI;
import com.mongodb.MongoSocketException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        init();
        Runtime.run();
    }

    private void init() {
        plugin = this;

        registerListeners();

        registerCommand("enclave", new EnclaveCommand());
        registerCommand("enclaveadmin", new AdminCommand());

        this.saveDefaultConfig();

        connectDatabase();
        loadHooks();
    }

    private void registerListeners() {
        List<Listener> listeners = new ArrayList<>();
        listeners.add(new PlayerEvents());
        listeners.add(new InteractionEvents());
        listeners.add(new WorldEvents());
        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommand(String cmd, Object object) {
        PluginCommand command = getCommand(cmd);
        if (command != null) {
            command.setExecutor((CommandExecutor) object);
            command.setTabCompleter((TabCompleter) object);
        }
    }

    private void loadHooks() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getServer().getLogger().log(Level.WARNING, "Could not find PlaceholderAPI.");
        }
    }

    private void connectDatabase() {
        try {
            Database.connect();
        } catch (IllegalArgumentException | MongoSocketException e) {
            getServer().getLogger().log(Level.SEVERE, "Database connection failed.");
        }
    }

    @Override
    public void onDisable() {
        Database.mongoClient.close();
    }

}
