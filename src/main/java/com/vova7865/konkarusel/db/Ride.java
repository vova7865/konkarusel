package com.vova7865.konkarusel.db;

import lombok.Data;
import org.bukkit.entity.Horse;

import java.util.Date;
import java.util.UUID;

@Data
public class Ride {
    public UUID playerUUID;
    public Horse.Color horseColor;
    public Horse.Style horseStyle;
    public Date startDate, endDate;
}
