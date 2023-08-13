package dev.hely.core.bungee.server.listeners;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.listeners
//   Date: Sunday, August 13, 2023 - 2:42 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.server.events.ServerCreateEvent;
import dev.hely.core.bungee.server.events.ServerDeleteEvent;
import dev.hely.core.bungee.utils.BungeeCC;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;

public class ServerListener implements Listener {
    @EventHandler
    public void onServerCreate(ServerCreateEvent serverCreateEvent) {
        OxygenBungee.getInstance().getServerManager().loadServer(serverCreateEvent.getServerData());
    }

    @EventHandler
    public void onServerDelete(ServerDeleteEvent serverDeleteEvent) {
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverDeleteEvent.getServerName());

        if (serverInfo != null) {
            for (ProxiedPlayer proxiedPlayer : serverInfo.getPlayers()) {
                if (serverInfo.getName().toLowerCase().contains(OxygenBungee.getInstance().getBungeeConfig().getConfiguration().getString("hub-name")))
                    proxiedPlayer.disconnect(BungeeCC.translate("&cThe server you where on has went offline."));
                else
                    proxiedPlayer.connect(OxygenBungee.getInstance().getServerManager().getRandomHub(), ServerConnectEvent.Reason.SERVER_DOWN_REDIRECT);
            }
            OxygenBungee.getInstance().getServerManager().removeLoadedServer(serverDeleteEvent.getServerName());
        }
    }

    @EventHandler
    public void onServerKick(ServerKickEvent serverKickEvent) {
        serverKickEvent.getPlayer().resetTabHeader();
        if (!serverKickEvent.getKickedFrom().getName().toLowerCase().contains("-lobby-")) {
            serverKickEvent.setCancelled(true);
            serverKickEvent.getPlayer().sendMessage(BungeeCC.translate("&cYou were kicked from the server: &6" + Arrays.toString(serverKickEvent.getKickReasonComponent())));
            serverKickEvent.setCancelServer(OxygenBungee.getInstance().getServerManager().getRandomHub());
        } else {
            serverKickEvent.getPlayer().disconnect(serverKickEvent.getKickReasonComponent());
        }
    }
}
