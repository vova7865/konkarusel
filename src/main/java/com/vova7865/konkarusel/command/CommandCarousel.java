package com.vova7865.konkarusel.command;

import com.vova7865.konkarusel.carousel.CarouselManager;
import com.vova7865.konkarusel.command.carousel.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandCarousel implements CommandExecutor {
    private final Map<String, Subcommand> subcommands = new HashMap<>();

    public CommandCarousel(CarouselManager carouselManager) {
        subcommands.put("add", new CommandAddCarousel(carouselManager));
        subcommands.put("edit", new CommandEditCarousel(carouselManager));
        subcommands.put("clear", new CommandClearCarousel(carouselManager));
        subcommands.put("remove", new CommandRemoveCarousel(carouselManager));
        subcommands.put("dump", new CommandDumpCarousel(carouselManager));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand == null) {
            return false;
        }

        String[] subcommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subcommandArgs, 0, subcommandArgs.length);
        return subcommand.execute(sender, subcommandArgs);
    }

}
