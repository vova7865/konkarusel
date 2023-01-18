package com.vova7865.konkarusel.command;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    public boolean execute(CommandSender sender, String[] args);
}
