package com.vova7865.konkarusel.network;

import com.comphenix.protocol.PacketType;

public class AttachEntityPackerWrapper extends AbstractPacketWrapper {
    public AttachEntityPackerWrapper() {
        super(PacketType.Play.Server.ATTACH_ENTITY);
    }


    public void setVehicleID(int vehicleID) {
        packetContainer.getIntegers().write(0, vehicleID);
    }

    public void setEntityID(int entityID) {
        packetContainer.getIntegers().write(1, entityID);
    }
}
