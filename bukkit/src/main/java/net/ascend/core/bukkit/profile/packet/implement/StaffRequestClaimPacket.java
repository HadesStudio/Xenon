package net.ascend.core.bukkit.profile.packet.implement;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.profile.packet.implement
//   Date: Sunday, July 30, 2023 - 4:57 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StaffRequestClaimPacket implements Packet {

    private final String playerName;
    private final String creatorName;
}
