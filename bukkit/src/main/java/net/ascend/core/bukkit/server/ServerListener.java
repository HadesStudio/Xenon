package net.ascend.core.bukkit.server;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;

import net.ascend.core.bukkit.profile.packet.implement.StaffRawMessagePacket;
import net.ascend.core.bukkit.server.packet.ServerCommandExecutePacket;
import net.ascend.core.bukkit.server.packet.ServerStateChangePacket;
import net.ascend.core.bukkit.server.packet.ServerUpdatePacket;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class ServerListener implements PacketListener {
    @IncomingPacketHandler
    public void onServerUpdate(ServerStateChangePacket packet) {
        Server server = XenonBukkit.getInstance().getServerManager().getServer(packet.getName());
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


        String message = XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("server-manager.state-changed").replace("%server%", server.getName()).replace("%state%", state.getDisplayName());
        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.staff", server.getName(), message));
    }

    @IncomingPacketHandler
    public void onServerCommandExecute(ServerCommandExecutePacket packet) {
        Server server = XenonBukkit.getInstance().getServerManager().getServer(packet.getServer());
        String executor = packet.getExecutor();
        String command = packet.getCommand();

        if (server == null) {
            return;
        }
        if (!XenonBukkit.getInstance().getServer().getName().equalsIgnoreCase(server.getName())) {
            return;
        }
        if (server.getState() != ServerState.ONLINE) {
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);


        String message = XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("run-command.broadcast").replace("%server%", server.getName()).replace("%command%", command).replace("%user%", executor);
        XenonBukkit.getInstance().getRedisModule().sendPacket(new StaffRawMessagePacket("xenon.admin", server.getName(), message));
    }

    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        XenonBukkit.getInstance().getServerManager().updateServer(packet.getServer());
    }
}
