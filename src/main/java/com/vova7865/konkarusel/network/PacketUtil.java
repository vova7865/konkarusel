package com.vova7865.konkarusel.network;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

import java.lang.reflect.InvocationTargetException;

public class PacketUtil {
    private static final ProtocolManager MANAGER = ProtocolLibrary.getProtocolManager();

    public static void attachLeashToEntity(Entity from, Entity to) {
        AttachEntityPackerWrapper packet = new AttachEntityPackerWrapper();
        packet.setEntityID(from.getEntityId());
        packet.setVehicleID(to.getEntityId());
        MANAGER.broadcastServerPacket(packet.getPacketContainer(), to, false);
    }

    public static void sendVehicleRotationToPassenger(Vehicle vehicle, float yaw, float pitch) {
        Entity passenger = vehicle.getPassenger();
        if (!(passenger instanceof Player)) {
            return;
        }
        Player player = (Player) passenger;
        VehicleMovePacketWrapper movePacket = new VehicleMovePacketWrapper();
        Location vehicleLocation = vehicle.getLocation();
        movePacket.setX(vehicleLocation.getX());
        movePacket.setY(vehicleLocation.getY());
        movePacket.setZ(vehicleLocation.getZ());
        movePacket.setYaw(yaw);
        movePacket.setPitch(pitch);
        try {
            MANAGER.sendServerPacket(player, movePacket.getPacketContainer());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void broadcastEntityRotationPacket(Entity entity, float yaw, float pitch) {
        EntityLookPacketWrapper lookPacket = new EntityLookPacketWrapper();
        lookPacket.setEntityID(entity.getEntityId());
        lookPacket.setYaw(yaw);
        lookPacket.setPitch(pitch);
        MANAGER.broadcastServerPacket(lookPacket.getPacketContainer(), entity, false);
    }
}
