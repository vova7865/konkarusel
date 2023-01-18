package com.vova7865.konkarusel.carousel;

import com.vova7865.konkarusel.db.Ride;
import com.vova7865.konkarusel.db.RideRepository;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class HorseRideListener implements Listener {
    private final RideRepository rideRepository;
    private final CarouselManager manager;

    private final Map<UUID, Ride> unfinishedRides = new HashMap<>();

    @EventHandler
    public void handlePlayerMountHorse(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player)
                || !(event.getMount() instanceof Horse))
            return;

        Player player = (Player) event.getEntity();
        Horse horse = (Horse) event.getMount();

        if (!manager.isHorseInCarousel(horse))
            return;

        Ride ride = new Ride();
        ride.setPlayerUUID(player.getUniqueId());
        ride.setStartDate(new Date());
        ride.setHorseColor(horse.getColor());
        ride.setHorseStyle(horse.getStyle());
        unfinishedRides.put(player.getUniqueId(), ride);
    }

    @EventHandler
    public void handlePlayerDismountHorse(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player)
                || !(event.getDismounted() instanceof Horse))
            return;

        Player player = (Player) event.getEntity();
        Horse horse = (Horse) event.getDismounted();

        if (!manager.isHorseInCarousel(horse))
            return;

        Ride ride = unfinishedRides.get(player.getUniqueId());
        ride.setEndDate(new Date());
        rideRepository.addRide(ride);
    }
}
