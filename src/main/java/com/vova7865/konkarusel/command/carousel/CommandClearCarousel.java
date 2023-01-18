package com.vova7865.konkarusel.command.carousel;

import com.vova7865.konkarusel.carousel.CarouselManager;
import com.vova7865.konkarusel.command.Subcommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class CommandClearCarousel implements Subcommand {
    private final CarouselManager carouselManager;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        carouselManager.removeAllCarousels();
        sender.sendMessage("Cleared");
        return true;
    }
}
