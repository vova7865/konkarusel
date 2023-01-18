package com.vova7865.konkarusel.carousel;

import com.vova7865.konkarusel.Konkarusel;
import com.vova7865.konkarusel.config.CarouselParams;
import com.vova7865.konkarusel.config.Konfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CarouselManager {
    private final Konkarusel plugin;
    private final Konfig config;

    private final Map<String, Carousel> carousels = new HashMap<>();

    public CarouselManager(Konkarusel plugin, Konfig config) {
        this.plugin = plugin;
        this.config = config;

        loadCarouselsFromConfig();
    }

    public Carousel getCarousel(String id) {
        return this.carousels.get(id);
    }

    public void addCarouselAtPlayerLocation(String id, Player player) {
        Carousel carousel = new Carousel(plugin, player.getLocation());
        if (this.carousels.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        this.carousels.put(id, carousel);
        carousel.start();

        saveCarouselsToConfig();
    }

    public void removeCarousel(String id) {
        if (!this.carousels.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        Carousel carousel = this.carousels.remove(id);
        carousel.destroy();

        saveCarouselsToConfig();
    }

    public void removeAllCarousels() {
        this.destroyAllCarousels();
        this.carousels.clear();

        saveCarouselsToConfig();
    }

    public void destroyAllCarousels() {
        this.carousels.values().forEach(Carousel::destroy);
    }

    public boolean isHorseInCarousel(Horse horse) {
        return this.carousels.values().stream().anyMatch(c -> c.containsHorse(horse));
    }

    public void updateCarouselProperty(String id, String property, String value) {
        Carousel carousel = Objects.requireNonNull(this.carousels.get(id));

        setCarouselProperty(carousel, property, value);

        saveCarouselsToConfig();
    }

    private void loadCarouselsFromConfig() {
        try {
            this.config.load();
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load plugin config");
            e.printStackTrace();
            return;
        }

        this.carousels.clear();
        for (Map.Entry<String, CarouselParams> entry : this.config.getCarousels().entrySet()) {
            Carousel carousel = createCarouselFromParams(entry.getValue());
            this.carousels.put(entry.getKey(), carousel);
            carousel.start();
        }
    }

    private void saveCarouselsToConfig() {
        this.config.getCarousels().clear();
        for (Map.Entry<String, Carousel> entry : this.carousels.entrySet()) {
            CarouselParams params = saveCarouselToParams(entry.getValue());
            this.config.getCarousels().put(entry.getKey(), params);
        }

        try {
            config.save();
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save plugin config");
            e.printStackTrace();
        }
    }

    private Carousel createCarouselFromParams(CarouselParams params) {
        World world = Bukkit.getWorld(params.getCenterWorldName());
        Location center = new Location(world, params.getCenterX(), params.getCenterY(), params.getCenterZ());
        Carousel carousel = new Carousel(this.plugin, center);

        carousel.setSpeed(params.getSpeed());
        carousel.setRadius(params.getRadius());
        carousel.setHorseCount(params.getHorseCount());
        carousel.setVerticalStretch(params.getVerticalStretch());

        String color = params.getHorseColor().toUpperCase();
        carousel.setHorseColor(color.equals("RANDOM") ? null : Horse.Color.valueOf(color));

        String style = params.getHorseStyle().toUpperCase();
        carousel.setHorseStyle(style.equals("RANDOM") ? null : Horse.Style.valueOf(style));

        return carousel;
    }

    private CarouselParams saveCarouselToParams(Carousel carousel) {
        CarouselParams params = new CarouselParams();

        Location center = carousel.getCenter();
        params.setCenterWorldName(center.getWorld().getName());
        params.setCenterX(center.getBlockX());
        params.setCenterY(center.getBlockY());
        params.setCenterZ(center.getBlockZ());

        params.setSpeed(carousel.getSpeed());
        params.setRadius(carousel.getRadius());
        params.setHorseCount(carousel.getHorseCount());
        params.setVerticalStretch(carousel.getVerticalStretch());

        params.setHorseColor(carousel.getHorseColor() == null ? "RANDOM" : carousel.getHorseColor().name());
        params.setHorseStyle(carousel.getHorseStyle() == null ? "RANDOM" : carousel.getHorseStyle().name());

        return params;
    }

    private static void setCarouselProperty(Carousel carousel, String property, String val) {
        switch (property) {
            case "radius":
                carousel.setRadius(Integer.parseInt(val));
                break;
            case "verticalStretch":
                carousel.setVerticalStretch(Integer.parseInt(val));
                break;
            case "speed":
                carousel.setSpeed(Integer.parseInt(val));
                break;
            case "horseCount":
                carousel.setHorseCount(Integer.parseInt(val));
                break;
            case "horseColor":
                String color = val.toUpperCase();
                carousel.setHorseColor(color.equals("RANDOM") ? null : Horse.Color.valueOf(color));
                break;
            case "horseStyle":
                String style = val.toUpperCase();
                carousel.setHorseStyle(style.equals("RANDOM") ? null : Horse.Style.valueOf(style));
                break;
            default:
                throw new IllegalArgumentException("Property '" + property + "' doesn't exist");
        }
    }
}
