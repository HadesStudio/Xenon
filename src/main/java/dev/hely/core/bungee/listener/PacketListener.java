package dev.hely.core.bungee.listener;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.listener
//   Date: Monday, August 14, 2023 - 12:09 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.server.packets.ProxyUpdatePacket;
import dev.hely.core.bungee.server.packets.ServerUpdatePacket;
import dev.hely.core.bungee.server.packets.maintenance.MaintenanceKickPacket;
import dev.hely.core.bungee.server.packets.maintenance.MaintenanceListPacket;
import dev.hely.core.bungee.server.packets.maintenance.MaintenanceModePacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;

import java.util.UUID;

public class PacketListener implements Listener {
    @IncomingPacketHandler
    public void onProxyUpdate(ProxyUpdatePacket packet) {
        Proxy proxy = OxygenBungee.getInstance().getServerManager().proxyFromName(packet.getProxyName());

        if (proxy == null) {
            proxy = new Proxy(packet.getProxyName(), packet.getProxyPort());
            OxygenBungee.getInstance().getServerManager().getProxies().add(proxy);
        }

        proxy.refresh(packet.getJsonObject());
    }

    @IncomingPacketHandler
    public void onServerUpdate(ServerUpdatePacket packet) {
        Server server = OxygenBungee.getInstance().getServerManager().serverFromName(packet.getServerName());

        if (server == null) {
            server = new Server(packet.getServerName(), packet.getServerPort());
            OxygenBungee.getInstance().getServerManager().getServers().add(server);
        }

        server.refresh(packet.getJsonObject());
    }

    @IncomingPacketHandler
    public void onMaintenanceKick(MaintenanceKickPacket packet) {
        for (ProxiedPlayer player : OxygenBungee.getInstance().getProxy().getPlayers()) {
            player.disconnect(ChatColor.RED + "The server has went into maintenance mode.");
        }
    }

    @IncomingPacketHandler
    public void onMaintenanceMode(MaintenanceModePacket packet) {
        OxygenBungee.getInstance().getServerManager().setMaintenanceMode(packet.isEnabled());
    }

    @IncomingPacketHandler
    public void onMaintennaceList(MaintenanceListPacket packet) {
        UUID uuid = UUID.fromString(packet.getUuid().toString());
        MaintenanceListPacket.Action action = MaintenanceListPacket.Action.valueOf(packet.getAction().toString());

        if (action == MaintenanceListPacket.Action.ADD) {
            if (!OxygenBungee.getInstance().getServerManager().getMaintenanceList().contains(uuid)) {
                OxygenBungee.getInstance().getServerManager().getMaintenanceList().add(uuid);
            }
        } else if (action == MaintenanceListPacket.Action.REMOVE) {
            if (OxygenBungee.getInstance().getServerManager().getMaintenanceList().contains(uuid)) {
                OxygenBungee.getInstance().getServerManager().getMaintenanceList().remove(uuid);
            }
        }
    }

}
