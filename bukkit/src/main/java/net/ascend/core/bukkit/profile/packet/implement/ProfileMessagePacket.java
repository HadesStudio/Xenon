package net.ascend.core.bukkit.profile.packet.implement;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.packet.implement
//   Date: Monday, September 04, 2023 - 8:22 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class ProfileMessagePacket implements Packet {
    private final Player player;
    private final String message;
}
