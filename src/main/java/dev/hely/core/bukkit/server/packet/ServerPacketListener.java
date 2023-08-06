package dev.hely.core.bukkit.server.packet;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bukkit.server.ServerManager;
import dev.hely.core.bukkit.server.packet.implement.ServerAlertUpdatePacket;
import dev.hely.core.bukkit.server.packet.implement.ServerCommandPacket;
import dev.hely.core.bukkit.server.packet.implement.ServerUpdatePacket;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

/**
 * Created By LeandroSSJ
 * Created on 06/08/2023
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class ServerPacketListener implements PacketListener {

    private ServerManager serverManager;

    @IncomingPacketHandler
    public void onServerAlertUpdate(ServerAlertUpdatePacket packet) {
        String server = packet.getServer();
        boolean online = packet.isOnline();

        if (online) {
            // START ALERT
        } else {
            // OFFLINE ALERT
        }
    }

    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        Server server = serverManager.getOrCreateServer(packet.getName());
        if (server == null) return;

        server.setStartedAt(packet.getStartedAt());
        server.setTps(packet.getTps());
        server.setWhitelisted(packet.isWhitelisted());
        server.setOnlinePlayers(packet.getOnlinePlayers());
        server.setMaxPlayers(packet.getMaxPlayers());
    }

    @IncomingPacketHandler
    public void onServerCommand(ServerCommandPacket packet) {
        String sender = packet.getSender();
        String server = packet.getServer();

        String command = packet.getCommand();
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        String finalCommand = command;

        if (server.equalsIgnoreCase("global")) {
            OxygenBukkit.getInstance().getTasks().run(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
        } else if (serverManager.getServerName().equalsIgnoreCase(server)) {
            OxygenBukkit.getInstance().getTasks().run(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
        }
    }
}
