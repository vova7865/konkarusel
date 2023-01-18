package com.vova7865.konkarusel.network;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;

public abstract class AbstractPacketWrapper {
    @Getter
    protected final PacketContainer packetContainer;

    protected AbstractPacketWrapper(PacketType type) {
        this.packetContainer = new PacketContainer(type);
        this.packetContainer.getModifier().writeDefaults();
    }
}