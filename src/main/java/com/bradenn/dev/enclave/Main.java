package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.commands.AdminCommand;
import com.bradenn.dev.enclave.commands.EnclaveCommand;
import com.bradenn.dev.enclave.events.InteractionEvents;
import com.bradenn.dev.enclave.events.PlayerEvents;
import com.bradenn.dev.enclave.events.WorldEvents;
import com.bradenn.dev.enclave.hooks.PlaceholderAPI;
import com.mongodb.MongoSocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Objects.requireNonNull(getCommand("enclave")).setExecutor(new EnclaveCommand());
        Objects.requireNonNull(getCommand("enclave")).setTabCompleter(new EnclaveCommand());

        Objects.requireNonNull(getCommand("enclaveadmin")).setExecutor(new AdminCommand());
        Objects.requireNonNull(getCommand("enclaveadmin")).setTabCompleter(new AdminCommand());

        List<Listener> listeners = new ArrayList<>();
        listeners.add(new PlayerEvents());
        listeners.add(new InteractionEvents());
        listeners.add(new WorldEvents());
        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        this.saveDefaultConfig();

        try {
            Database.connect();
        } catch (IllegalArgumentException | MongoSocketException e) {
            getServer().getLogger().log(Level.SEVERE, "Database connection failed.");
        }

        Runtime.run();
        loadHooks();
    }

    private void loadHooks() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getServer().getLogger().log(Level.WARNING, "Could not find PlaceholderAPI.");
        }
    }

    @Override
    public void onDisable() {
        Database.mongoClient.close();
    }

}
