package net.ascend.core.bukkit.rank.grant.packet.impl;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GrantUpdatePacket implements Packet {

    private final UUID profile;

}

