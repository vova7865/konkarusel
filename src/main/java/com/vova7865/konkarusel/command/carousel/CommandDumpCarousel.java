package com.vova7865.konkarusel.command.carousel;

import com.vova7865.konkarusel.carousel.Carousel;
import com.vova7865.konkarusel.carousel.CarouselManager;
import com.vova7865.konkarusel.command.Subcommand;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class CommandDumpCarousel implements Subcommand {
    private final CarouselManager carouselManager;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String id = args[0];
        Carousel carousel = carouselManager.getCarousel(id);
        if (carousel == null) {
            sender.sendMessage("No carousel with id " + id);
            return true;
        }
        Location center = carousel.getCenter();
        sender.sendMessage(String.format("center: %d/%d/%d @%s", center.getBlockX(), center.getBlockY(), center.getBlockZ(), center.getWorld().getName()));
        sender.sendMessage("speed: " + carousel.getSpeed());
        sender.sendMessage("# horses: " + carousel.getHorseCount());
        sender.sendMessage("vertical stretch: " + carousel.getVerticalStretch());
        sender.sendMessage("radius: " + carousel.getRadius());
        sender.sendMessage("color: " + carousel.getHorseColor().name().toLowerCase());
        sender.sendMessage("style: " + carousel.getHorseStyle().name().toLowerCase());
        return true;
    }
}
