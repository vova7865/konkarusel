package com.vova7865.konkarusel.command.carousel;

import com.vova7865.konkarusel.carousel.CarouselManager;
import com.vova7865.konkarusel.command.Subcommand;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class CommandRemoveCarousel implements Subcommand {
    private final CarouselManager carouselManager;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String id = args[0];
        try {
            carouselManager.removeCarousel(args[0]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("No carousel with id " + id);
            return true;
        }
        sender.sendMessage("Removed " + id);
        return true;
    }
}
