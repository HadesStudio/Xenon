package dev.hely.core.bungee.server.packets;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.packets
//   Date: Monday, August 14, 2023 - 11:59 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonObject;
import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProxyUpdatePacket implements Packet {
    private final String proxyName;
    private final int proxyPort;
    private final JsonObject jsonObject;

}
