package com.vova7865.konkarusel.network;

import com.comphenix.protocol.PacketType;

public class VehicleMovePacketWrapper extends AbstractPacketWrapper {
    public VehicleMovePacketWrapper() {
        super(PacketType.Play.Server.VEHICLE_MOVE);
    }


    public void setX(double x) {
        packetContainer.getDoubles().write(0, x);
    }

    public void setY(double y) {
        packetContainer.getDoubles().write(1, y);
    }

    public void setZ(double z) {
        packetContainer.getDoubles().write(2, z);
    }

    public void setYaw(float yaw) {
        packetContainer.getFloat().write(0, yaw);
    }

    public void setPitch(float pitch) {
        packetContainer.getFloat().write(1, pitch);
    }
}