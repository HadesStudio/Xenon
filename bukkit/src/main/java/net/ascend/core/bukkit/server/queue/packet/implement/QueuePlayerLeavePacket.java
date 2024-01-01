package net.ascend.core.bukkit.server.queue.packet.implement;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue.packets.implement
//   Date: Tuesday, September 12, 2023 - 6:36 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class QueuePlayerLeavePacket implements Packet {
    private final UUID uuid;
}
