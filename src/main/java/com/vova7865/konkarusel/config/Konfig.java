package com.vova7865.konkarusel.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Konfig {
    private final File file;
    private YamlConfiguration config;

    @Getter
    private String dbConnectionString;

    @Getter
    private Map<String, CarouselParams> carousels;

    public Konfig(File file) throws IOException {
        this.file = file;
        load();
        save();
    }

    public void load() throws IOException {
        this.config = YamlConfiguration.loadConfiguration(file);
        URL defaultConfig = getClass().getClassLoader().getResource(file.getName());
        if (defaultConfig != null) {
            InputStreamReader reader = new InputStreamReader(defaultConfig.openStream());
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
            this.config.options().copyDefaults(true);
            this.config.setDefaults(defaults);
        }

        this.dbConnectionString = config.getString("database.connectionString");

        this.carousels = new HashMap<>();
        ConfigurationSection carouselsSection = config.getConfigurationSection("carousels");
        Set<String> carouselIds = carouselsSection.getKeys(false);
        for (String carouselId : carouselIds) {
            ConfigurationSection carouselSection = carouselsSection.getConfigurationSection(carouselId);

            CarouselParams params = loadCarousel(carouselSection);

            this.carousels.put(carouselId, params);
        }
    }

    public void save() throws IOException {
        config.set("database.connectionString", this.dbConnectionString);

        ConfigurationSection carouselsSection = config.createSection("carousels");
        for (Map.Entry<String, CarouselParams> entry : carousels.entrySet()) {
            ConfigurationSection carouselSection = carouselsSection.createSection(entry.getKey());
            CarouselParams params = entry.getValue();

            saveCarousel(carouselSection, params);
        }

        this.config.save(this.file);
    }

    private CarouselParams loadCarousel(ConfigurationSection carouselSection) {
        CarouselParams params = new CarouselParams();

        params.setCenterWorldName(carouselSection.getString("location.world"));
        params.setCenterX(carouselSection.getInt("location.x"));
        params.setCenterY(carouselSection.getInt("location.y"));
        params.setCenterZ(carouselSection.getInt("location.z"));

        params.setSpeed(carouselSection.getInt("speed"));
        params.setRadius(carouselSection.getInt("radius"));
        params.setVerticalStretch(carouselSection.getInt("verticalStretch"));
        params.setHorseCount(carouselSection.getInt("horseCount"));
        params.setHorseColor(carouselSection.getString("horseColor"));
        params.setHorseStyle(carouselSection.getString("horseStyle"));

        return params;
    }

    private void saveCarousel(ConfigurationSection carouselSection, CarouselParams params) {
        carouselSection.set("location.world", params.getCenterWorldName());
        carouselSection.set("location.x", params.getCenterX());
        carouselSection.set("location.y", params.getCenterY());
        carouselSection.set("location.z", params.getCenterZ());

        carouselSection.set("speed", params.getSpeed());
        carouselSection.set("radius", params.getRadius());
        carouselSection.set("verticalStretch", params.getVerticalStretch());
        carouselSection.set("horseCount", params.getHorseCount());
        carouselSection.set("horseColor", params.getHorseColor());
        carouselSection.set("horseStyle", params.getHorseStyle());
    }
}
