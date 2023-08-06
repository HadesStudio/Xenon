package dev.hely.core.bukkit.server.packet.implement;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 06/08/2023
 * Protect Name Oxygen
 */
@Getter
@AllArgsConstructor
public class ServerAlertUpdatePacket implements Packet {

    private final String server;
    private final boolean online;

}

