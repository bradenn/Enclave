package com.bradenn.dev.enclave.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AdminCommand extends Command {

    public AdminCommand(@NotNull String name) {
        super(name);
        this.description = "Admin tools for enclaves.";
        this.usageMessage = "/enclaveadmin";
        this.setPermission("enclaves.admin");
        this.setAliases(Arrays.asList("ea"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return false;
    }
}
