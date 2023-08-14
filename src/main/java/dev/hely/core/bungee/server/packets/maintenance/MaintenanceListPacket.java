package dev.hely.core.bungee.server.packets.maintenance;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.packets.maintenance
//   Date: Monday, August 14, 2023 - 12:00 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MaintenanceListPacket implements Packet {
    private final Action action;
    private final UUID uuid;

    public enum Action {
        ADD,
        REMOVE
    }
}
