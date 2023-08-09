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
public class ServerUpdatePacket implements Packet {

    private final String name;
    private final String version;
    private final long startedAt;
    private final double tps;
    private final boolean whitelisted;
    private final int maxPlayers;
    private final int onlinePlayers;
    private final long lastUpdate;
    private final long lastOnline;

}
