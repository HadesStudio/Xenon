package dev.hely.core.bukkit.server.queue.packets;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue.packets
//   Date: Tuesday, September 12, 2023 - 6:51 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

@Getter
@AllArgsConstructor
public class QueuePositionNotificationPacket implements Packet {
    private final UUID player;
    private final List<String> message;
    private final int position;
    private final int queueSize;
}
