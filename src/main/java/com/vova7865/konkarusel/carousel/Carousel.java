package com.vova7865.konkarusel.carousel;

import com.vova7865.konkarusel.Konkarusel;
import com.vova7865.konkarusel.network.PacketUtil;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;

public class Carousel {
    private final Konkarusel plugin;
    @Getter
    private final Location center;

    @Getter
    private int horseCount = 2;
    @Getter
    private int radius = 10;
    @Getter
    private int verticalStretch = 0;
    @Getter
    private int speed = 0;
    @Getter
    private Horse.Color horseColor;
    @Getter
    private Horse.Style horseStyle;

    private final List<Horse> horses = new LinkedList<>();
    private final World world;
    private LeashHitch leashHitch;
    private BukkitTask progressHorsesTask;
    private long ticks = 0;
    private boolean active = false;

    public Carousel(Konkarusel plugin, Location center) {
        this.plugin = plugin;
        this.center = center.toCenterLocation();
        this.world = center.getWorld();
    }

    public void start() {
        if (this.active) {
            return;
        }

        active = true;

        Location leashLocation = center.clone().add(0, 4, 0).toBlockLocation();
        leashLocation.setYaw(0);
        leashLocation.setPitch(0);
        leashLocation.getBlock().setType(Material.FENCE);
        leashHitch = world.spawn(leashLocation, LeashHitch.class);
        leashHitch.setInvulnerable(true);

        initHorses();

        progressHorsesTask = Bukkit.getServer().getScheduler().runTaskTimer(plugin, this::progressHorses, 0, 1);
    }

    public void destroy() {
        if (!this.active) {
            return;
        }

        active = false;

        progressHorsesTask.cancel();
        horses.forEach(h -> {
            h.remove();
            world.spawnParticle(Particle.EXPLOSION_LARGE, h.getLocation(), 1);
        });
        leashHitch.getLocation().getBlock().setType(Material.AIR);
        leashHitch.remove();
    }

    public boolean containsHorse(Horse horse) {
        return this.horses.contains(horse);
    }

    private void progressHorses() {
        if (!world.isChunkLoaded(center.getChunk()))
            return;

        double effectiveSpeed = (2 * Math.PI * radius) / speed;
        double baseAngle = ticks / effectiveSpeed;
        for (int i = 0; i < horseCount; i++) {
            Horse horse = horses.get(i);
            double angle = baseAngle + (2 * Math.PI / horseCount * i);
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle + 2 * Math.PI / horseCount * i) * verticalStretch;
            double z = Math.sin(angle) * radius;
            Location nextCheckpoint = center.clone().add(x, y, z);
            Location delta = nextCheckpoint.subtract(horse.getLocation());
            horse.setVelocity(delta.toVector());

            float yaw = (float) (angle / Math.PI * 180) % 360;
            if (speed < 0) {
                yaw = 180 - yaw;
            }
            float pitch = 0;

            PacketUtil.attachLeashToEntity(leashHitch, horse);

            if (horse.getPassenger() instanceof Player) {
                PacketUtil.sendVehicleRotationToPassenger(horse, yaw, pitch);
                PacketUtil.broadcastEntityRotationPacket(horse, yaw, pitch);
            } else {
                Location cur = horse.getLocation();
                cur.setYaw(yaw);
                horse.teleport(cur);
            }
        }
        ticks++;
    }

    private void reinitHorses() {
        if (this.active)
            initHorses();
    }

    private void initHorses() {
        this.horses.forEach(Entity::remove);
        this.horses.clear();

        for (int i = 0; i < this.horseCount; i++) {
            double angle = 2 * Math.PI / horseCount * i;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            Horse horse = world.spawn(center.clone().add(x, 0, z), Horse.class);

            if (this.horseColor != null)
                horse.setColor(this.horseColor);
            if (this.horseStyle != null)
                horse.setStyle(this.horseStyle);

            horse.setCollidable(false);
            horse.setInvulnerable(true);
            horse.setGravity(false);
            horse.setAdult();
            horse.setTamed(true);

            this.horses.add(horse);
        }
    }

    public void setRadius(int radius) {
        if (radius < 1 || radius > 10) {
            throw new IllegalArgumentException("1 <= radius <= 10");
        }
        this.radius = radius;
    }

    public void setVerticalStretch(int verticalStretch) {
        if (verticalStretch < 0 || verticalStretch > 10) {
            throw new IllegalArgumentException("0 <= verticalStretch <= 10");
        }
        this.verticalStretch = verticalStretch;
    }

    public void setSpeed(int speed) {
        if (speed < -10 || speed > 30) {
            throw new IllegalArgumentException("-10 <= speed <= 30");
        }
        this.speed = speed;
    }

    public void setHorseCount(int horseCount) {
        if (horseCount < 1 || horseCount > 16) {
            throw new IllegalArgumentException("1 <= horseCount <= 16");
        }
        this.horseCount = horseCount;
        reinitHorses();
    }

    public void setHorseColor(Horse.Color horseColor) {
        this.horseColor = horseColor;
        reinitHorses();
    }

    public void setHorseStyle(Horse.Style horseStyle) {
        this.horseStyle = horseStyle;
        reinitHorses();
    }
}
