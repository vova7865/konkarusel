package com.vova7865.konkarusel;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.vova7865.konkarusel.carousel.CarouselManager;
import com.vova7865.konkarusel.carousel.HorseRideListener;
import com.vova7865.konkarusel.command.CommandCarousel;
import com.vova7865.konkarusel.command.CommandRides;
import com.vova7865.konkarusel.config.Konfig;
import com.vova7865.konkarusel.db.RideRepository;
import com.vova7865.konkarusel.network.HorsePacketListener;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Konkarusel extends JavaPlugin {
    private MongoClient mongoClient;

    @Getter
    private CarouselManager carouselManager;

    private HorseRideListener horseRideListener;
    private HorsePacketListener packetListener;

    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        Konfig config;

        try {
            config = new Konfig(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().severe("Failed to load plugin config");
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        try {
            CodecProvider provider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(provider)
            );
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(config.getDbConnectionString()))
                    .codecRegistry(codecRegistry)
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .build();
            mongoClient = MongoClients.create(settings);
        } catch (Exception e) {
            getLogger().severe("Failed to connect to MongoDB");
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        RideRepository rideRepository = new RideRepository(mongoClient.getDatabase("konkarusel"));

        carouselManager = new CarouselManager(this, config);

        horseRideListener = new HorseRideListener(rideRepository, carouselManager);
        Bukkit.getServer().getPluginManager().registerEvents(this.horseRideListener, this);

        packetListener = new HorsePacketListener(this, carouselManager);
        protocolManager.addPacketListener(packetListener);

        getCommand("carousel").setExecutor(new CommandCarousel(carouselManager));
        getCommand("rides").setExecutor(new CommandRides(rideRepository));
    }

    @Override
    public void onDisable() {
        if (horseRideListener != null)
            HandlerList.unregisterAll(horseRideListener);
        if (packetListener != null)
            protocolManager.removePacketListener(packetListener);
        if (carouselManager != null)
            carouselManager.destroyAllCarousels();

        if (mongoClient != null)
            mongoClient.close();
    }
}
