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
import dev.hely.core.bukkit.server.packets.ServerCommandExecutePacket;
import dev.hely.core.bukkit.server.packets.ServerStateChangePacket;
import dev.hely.core.bukkit.server.packets.ServerUpdatePacket;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class ServerListener implements PacketListener {
    @IncomingPacketHandler
    public void onServerUpdate(ServerStateChangePacket packet) {
        Server server = OxygenBukkit.getInstance().getServerManager().getServer(packet.getName());
        ServerState state = packet.getState();

        if (server == null) {
            return;
        }
        if (server.getState() == packet.getState()) { // We don't want to spam messages.. simple fix xd
            return;
        }

        server.setState(state);
        server.setLastHeartbeat(System.currentTimeMillis());
        server.save();


        String message = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("server-manager.state-changed").replace("%server%", server.getName()).replace("%state%", state.getDisplayName());
        OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.staff", server.getName(), message));
    }

    @IncomingPacketHandler
    public void onServerCommandExecute(ServerCommandExecutePacket packet) {
        Server server = OxygenBukkit.getInstance().getServerManager().getServer(packet.getServer());
        String executor = packet.getExecutor();
        String command = packet.getCommand();

        if (server == null) {
            return;
        }
        if (!OxygenBukkit.getInstance().getServer().getName().equalsIgnoreCase(server.getName())) {
            return;
        }
        if (server.getState() != ServerState.ONLINE) {
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);


        String message = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.broadcast").replace("%server%", server.getName()).replace("%command%", command).replace("%user%", executor);
        OxygenBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("oxygen.admin", server.getName(), message));
    }

    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        OxygenBukkit.getInstance().getServerManager().updateServer(packet.getServer());
    }
}
