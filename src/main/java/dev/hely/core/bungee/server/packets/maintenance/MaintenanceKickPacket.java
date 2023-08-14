package dev.hely.core.bungee.server.packets.maintenance;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.packets.maintenance
//   Date: Monday, August 14, 2023 - 11:59 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import dev.hely.core.bungee.server.data.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class MaintenanceKickPacket implements Packet {
    private final Player player;
    private final Server server;
}
