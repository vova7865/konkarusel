package com.vova7865.konkarusel.network;

import com.comphenix.protocol.PacketType;

public class EntityLookPacketWrapper extends AbstractPacketWrapper {
    public EntityLookPacketWrapper() {
        super(PacketType.Play.Server.ENTITY_LOOK);
    }


    public void setEntityID(int entityID) {
        packetContainer.getIntegers().write(0, entityID);
    }

    public void setYaw(float yaw) {
        packetContainer.getBytes().write(0, (byte) (yaw * 256.0F / 360.0F));
    }

    public void setPitch(float pitch) {
        packetContainer.getBytes().write(1, (byte) (pitch * 256.0F / 360.0F));
    }

    public void setOnGround(boolean onGround) {
        packetContainer.getBooleans().write(0, onGround);
    }
}