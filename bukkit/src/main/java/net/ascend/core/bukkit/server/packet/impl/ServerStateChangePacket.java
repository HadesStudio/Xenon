package net.ascend.core.bukkit.server.packet.impl;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.core.bukkit.server.ServerState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerStateChangePacket implements Packet {

    private final String name;
    private final boolean online;

}
