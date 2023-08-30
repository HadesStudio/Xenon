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

import dev.hely.core.bukkit.server.packets.ServerUpdatePacket;
import dev.hely.core.bukkit.utils.TPSUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class ServerListener implements PacketListener {
    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        Server server = OxygenBukkit.getInstance().getServerManager().getOrSetupServer(packet.getName());

        if (server == null) return;

        server.setStatus(packet.getStatus());
        server.setLastTps(TPSUtil.getRecentTps()[0]);
        server.setOnlinePlayers(Bukkit.getOnlinePlayers().size());
        server.setMaximumPlayers(Bukkit.getMaxPlayers());
        server.setUseQueue(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getBoolean("use-queue-system"));
        server.setLastHeartBeat(System.currentTimeMillis());
        server.setStartupTime(packet.getStartupTime());
        server.cache(OxygenBukkit.getInstance().getRedisModule().getJedisPool());
    }
}
