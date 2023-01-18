package com.vova7865.konkarusel.command.carousel;

import com.vova7865.konkarusel.carousel.CarouselManager;
import com.vova7865.konkarusel.command.Subcommand;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CommandAddCarousel implements Subcommand {
    private final CarouselManager carouselManager;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("players only");
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        Player player = (Player) sender;
        String id = args[0];
        try {
            carouselManager.addCarouselAtPlayerLocation(id, player);
            sender.sendMessage("Carousel added");
        } catch (IllegalArgumentException e) {
            sender.sendMessage("Carousel with id " + id + " already exists");
        }
        return true;
    }
}
