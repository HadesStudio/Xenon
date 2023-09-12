package dev.hely.core.bukkit.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server
//   Date: Monday, August 14, 2023 - 1:00 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;

import dev.hely.core.bukkit.profile.packet.implement.StaffRawMessagePacket;
import dev.hely.core.bukkit.server.packets.ServerStateChangePacket;
import dev.hely.core.bukkit.server.packets.ServerUpdatePacket;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerListener implements PacketListener {
    @IncomingPacketHandler
    public void onServerUpdate(ServerStateChangePacket packet) {
        Server server = OxygenBukkit.getInstance().getServerManager().getOrSetupServer(packet.getName());
        ServerState state = packet.getState();

        if (server != null) {
            server.setState(state);
            server.setLastHeartbeat(System.currentTimeMillis());
        }

        String message = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("server-manager.state-changed").replace("%server%", server.getName()).replace("%state%", state.getDisplayName());
        OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.staff", server.getName(), message));
    }

    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        OxygenBukkit.getInstance().getServerManager().updateServer(packet.getServer());
    }
}
