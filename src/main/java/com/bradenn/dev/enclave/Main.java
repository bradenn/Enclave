package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.commands.EnclaveCommand;
import com.bradenn.dev.enclave.events.InteractionEvents;
import com.bradenn.dev.enclave.events.PlayerEvents;
import com.bradenn.dev.enclave.events.WorldEvents;
import com.mongodb.MongoSocketException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Objects.requireNonNull(getCommand("enclave")).setExecutor(new EnclaveCommand());
        Objects.requireNonNull(getCommand("enclave")).setTabCompleter(new EnclaveCommand());

        List<Listener> listeners = new ArrayList<>();
        listeners.add(new PlayerEvents());
        listeners.add(new InteractionEvents());
        listeners.add(new WorldEvents());
        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        this.saveDefaultConfig();


        try {
            Database.connect();
        }catch(IllegalArgumentException | MongoSocketException e){
            getServer().getLogger().log(Level.SEVERE, "Database connection failed.");
        }
    }

    @Override
    public void onDisable() {
        Database.mongoClient.close();
    }

}
