package dev.hely.core.bukkit.server.packets;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.packets
//   Date: Tuesday, September 12, 2023 - 4:57 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import dev.hely.core.bukkit.server.Server;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerUpdatePacket implements Packet {
    private final Server server;
}
