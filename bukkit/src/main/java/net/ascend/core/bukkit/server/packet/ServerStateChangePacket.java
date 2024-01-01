package net.ascend.core.bukkit.server.packet;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.packets
//   Date: Tuesday, September 12, 2023 - 4:42 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.core.bukkit.server.ServerState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerStateChangePacket implements Packet {
    private final String name;
    private final ServerState state;
}
