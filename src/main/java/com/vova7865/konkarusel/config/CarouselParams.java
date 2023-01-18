package com.vova7865.konkarusel.config;

import lombok.Data;

@Data
public class CarouselParams {
    private String centerWorldName;
    private int centerX;
    private int centerY;
    private int centerZ;
    private int speed;
    private int radius;
    private int verticalStretch;
    private int horseCount;
    private String horseColor;
    private String horseStyle;
}
