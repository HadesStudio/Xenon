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
import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.server.packets.ProxyUpdatePacket;
import dev.hely.core.bungee.server.packets.ServerUpdatePacket;
import dev.hely.core.bungee.server.packets.maintenance.MaintenanceKickPacket;
import dev.hely.core.bungee.server.packets.maintenance.MaintenanceListPacket;
import dev.hely.core.bungee.server.packets.maintenance.MaintenanceModePacket;

import java.util.UUID;

public class BukkitServerListener implements PacketListener {

    @IncomingPacketHandler
    public void onProxyUpdate(ProxyUpdatePacket packet) {

        Proxy proxy = OxygenBukkit.getInstance().getServerManager().proxyFromName(packet.getJsonObject().get("name").getAsString());

        if (proxy == null) {
            proxy = new Proxy(packet.getJsonObject().get("name").getAsString(),packet.getJsonObject().get("port").getAsInt());
            OxygenBukkit.getInstance().getServerManager().getProxies().add(proxy);
        }

        proxy.refresh(packet.getJsonObject());
    }

    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        Server server = OxygenBukkit.getInstance().getServerManager().serverFromName(packet.getJsonObject().get("name").getAsString());

        if (server == null) {
            server = new Server(packet.getJsonObject().get("name").getAsString(),packet.getJsonObject().get("port").getAsInt());
            OxygenBukkit.getInstance().getServerManager().getServers().add(server);
        }

        server.refresh(packet.getJsonObject());
    }

    @IncomingPacketHandler
    public void onMaintenanceKick(MaintenanceKickPacket packet) {}

    @IncomingPacketHandler
    public void onMaintenanceMode(MaintenanceModePacket packet) {
        OxygenBukkit.getInstance().getServerManager().setMaintenanceMode(packet.isEnabled());
    }

    @IncomingPacketHandler
    public void onMaintennaceList(MaintenanceListPacket packet) {

        final UUID uuid = packet.getUuid();
        final MaintenanceListPacket.Action action = packet.getAction();

        if (action == MaintenanceListPacket.Action.ADD) {

            if (!OxygenBukkit.getInstance().getServerManager().getMaintenanceList().contains(uuid)) {
                OxygenBukkit.getInstance().getServerManager().getMaintenanceList().add(uuid);
            }

        } else if (action == MaintenanceListPacket.Action.REMOVE) {
            if (OxygenBukkit.getInstance().getServerManager().getMaintenanceList().contains(uuid)) {
                OxygenBukkit.getInstance().getServerManager().getMaintenanceList().remove(uuid);
            }

        }
    }

}
