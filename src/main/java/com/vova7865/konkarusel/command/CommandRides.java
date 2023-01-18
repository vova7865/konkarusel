package com.vova7865.konkarusel.command;

import com.vova7865.konkarusel.db.Ride;
import com.vova7865.konkarusel.db.RideRepository;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class CommandRides implements CommandExecutor {
    private final RideRepository rideRepository;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<Ride> rides;
        if (args.length < 1) {
            rides = rideRepository.getRides();
        } else {
            String name = args[0];
            OfflinePlayer player = Bukkit.getOfflinePlayer(name);
            if (!player.hasPlayedBefore()) {
                sender.sendMessage("Player " + name + " not found");
                return true;
            }
            rides = rideRepository.getRidesForPlayer(player.getUniqueId());
        }
        for (Ride ride : rides) {
            sender.sendMessage(formatRide(ride).split("\n"));
            sender.sendMessage("\n");
        }
        return true;
    }

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");

    private String formatRide(Ride ride) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(ride.getPlayerUUID());
        Date startDate = ride.getStartDate();
        Date endDate = ride.getEndDate();

        return String.format(
                "Player : %s\n" +
                        "Horse : %s, %s\n" +
                        "Duration : %s - %s (%ds)",
                player.getName(),
                ride.getHorseColor().name().toLowerCase(), ride.getHorseStyle().name().toLowerCase(),
                FORMATTER.format(startDate), FORMATTER.format(endDate),
                ChronoUnit.SECONDS.between(startDate.toInstant(), endDate.toInstant())
        );
    }
}
