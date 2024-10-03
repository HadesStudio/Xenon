package net.ascend.core.bukkit.server.packet.impl;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.core.bukkit.server.Server;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerUpdatePacket implements Packet {

    private final Server server;

}
