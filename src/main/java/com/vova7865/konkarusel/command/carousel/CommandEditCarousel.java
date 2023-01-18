package com.vova7865.konkarusel.command.carousel;

import com.vova7865.konkarusel.carousel.CarouselManager;
import com.vova7865.konkarusel.command.Subcommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class CommandEditCarousel implements Subcommand {
    private final CarouselManager carouselManager;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            return false;
        }
        String id = args[0];
        try {
            carouselManager.updateCarouselProperty(id, args[1], args[2]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("Invalid parameter: ");
            sender.sendMessage(e.getMessage());
        }
        return true;
    }
}
