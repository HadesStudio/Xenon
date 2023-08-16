package dev.hely.core.bukkit.server.packets;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.packets
//   Date: Wednesday, August 16, 2023 - 12:10 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.packets.ProxyUpdatePacket;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class ServerPacketListener implements PacketListener {
    @IncomingPacketHandler
    public void onServerShutdown(ShutdownPacket packet) {
        String server = packet.getServer();

        if (server.equalsIgnoreCase("all")) {
            OxygenBukkit.getInstance().getTasks().runAsync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "end"));
            OxygenBukkit.getInstance().getTasks().runAsync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop"));
        } else if (server.equalsIgnoreCase(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"))) {
            OxygenBukkit.getInstance().getTasks().runAsync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop"));
        }
    }
}