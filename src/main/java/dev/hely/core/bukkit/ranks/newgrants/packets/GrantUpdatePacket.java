package dev.hely.core.bukkit.ranks.newgrants.packets;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GrantUpdatePacket implements Packet {
    private final UUID profile;
}

