package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.commands.EnclaveCommand;
import com.bradenn.dev.enclave.completers.EnclaveCompleter;
import com.bradenn.dev.enclave.events.InteractionEvents;
import com.bradenn.dev.enclave.events.PlayerEvents;
import com.bradenn.dev.enclave.events.WorldEvents;
import com.bradenn.dev.enclave.persistent.Database;
import com.mongodb.MongoSocketException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("enclave")).setExecutor(new EnclaveCommand());
        Objects.requireNonNull(getCommand("enclave")).setTabCompleter(new EnclaveCompleter());
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new InteractionEvents(), this);
        getServer().getPluginManager().registerEvents(new WorldEvents(), this);
        this.saveDefaultConfig();
        plugin = this;
        try {
            Database.connect();
        }catch(IllegalArgumentException | MongoSocketException e){
            getServer().getLogger().log(Level.SEVERE, "Database connection failed.");
        }
    }

}
