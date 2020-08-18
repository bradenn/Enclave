package com.bradenn.dev.enclave;

import com.bradenn.dev.enclave.commands.EnclaveCommand;
import com.bradenn.dev.enclave.completers.EnclaveCompleter;
import com.bradenn.dev.enclave.events.PlayerEvents;
import com.bradenn.dev.enclave.managers.PlayerManager;
import com.bradenn.dev.enclave.managers.EnclaveManager;
import com.bradenn.dev.enclave.persistent.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("enclave")).setExecutor(new EnclaveCommand());
        Objects.requireNonNull(getCommand("enclave")).setTabCompleter(new EnclaveCompleter());
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        Database.connect();
    }

}
