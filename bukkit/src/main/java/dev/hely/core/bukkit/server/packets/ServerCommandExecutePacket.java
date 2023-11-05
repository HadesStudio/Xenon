package dev.hely.core.bukkit.server.packets;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerCommandExecutePacket implements Packet {
    private final String server;
    private final String executor;
    private final String command;
}