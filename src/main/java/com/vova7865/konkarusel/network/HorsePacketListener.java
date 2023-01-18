package com.vova7865.konkarusel.network;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.vova7865.konkarusel.carousel.CarouselManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.plugin.Plugin;

public class HorsePacketListener extends PacketAdapter {
    private final CarouselManager carouselManager;

    public HorsePacketListener(Plugin plugin, CarouselManager carouselManager) {
        super(plugin, PacketType.Play.Client.VEHICLE_MOVE);
        this.carouselManager = carouselManager;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Entity vehicle = event.getPlayer().getVehicle();
        if (vehicle instanceof Horse && carouselManager.isHorseInCarousel((Horse) vehicle)) {
            event.setCancelled(true);
        }
    }
}
