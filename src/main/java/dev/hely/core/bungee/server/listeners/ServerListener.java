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
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.utils.BungeeCC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ServerListener implements Listener {
    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing proxy = event.getResponse();
        ServerPing.Players players = proxy.getPlayers();

        int playerCount = 0;

        for (Server server : OxygenBungee.getInstance().getServerManager().getServers()) {
            playerCount += server.getOnlinePlayers().get();
        }

        proxy.setPlayers(new ServerPing.Players(players.getMax(), playerCount, players.getSample()));

        event.setResponse(proxy);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPostLogin(ServerConnectEvent event) {
        if (event.getPlayer().getServer() != null) {
            return;
        }

        List<Server> availableHubs = OxygenBungee.getInstance().getServerManager().getAvailableServers(event.getPlayer().getUniqueId(), OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("lobby-group"));
        if (availableHubs.isEmpty()) {
            event.getPlayer().disconnect(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-system.unable-suitable-hub") + " &c(Error #1)"));
            event.setCancelled(true);
            return;
        }

        ServerInfo serverInfo = OxygenBungee.getInstance().getProxy().getServerInfo(availableHubs.get(ThreadLocalRandom.current().nextInt(availableHubs.size())).getName());
        if (serverInfo == null) {
            event.getPlayer().disconnect(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-system.unable-suitable-hub") + " &c(Error #2)"));
            event.setCancelled(true);
            return;
        }

        event.getPlayer().sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-system.connected").replace("%server%", serverInfo.getName())));
        event.setTarget(serverInfo);
    }

    @EventHandler
    public void onKick(ServerKickEvent event) {
        if (Arrays.stream(event.getKickReasonComponent()).map(it -> ChatColor.stripColor(it.toPlainText())).noneMatch(it -> it.equalsIgnoreCase("Server closed") || it.contains("Server closed"))) {
            return;
        }

        List<Server> availableHubs = OxygenBungee.getInstance().getServerManager().getAvailableServers(event.getPlayer().getUniqueId(), OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("lobby-group"));
        if (availableHubs.isEmpty()) {
            event.getPlayer().disconnect(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-system.unable-suitable-hub") + " &c(Error #3)"));
            event.setCancelled(true);
            return;
        }

        ServerInfo serverInfo = OxygenBungee.getInstance().getProxy().getServerInfo(availableHubs.get(ThreadLocalRandom.current().nextInt(availableHubs.size())).getName());
        if (serverInfo == null) {
            event.getPlayer().disconnect(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-system.unable-suitable-hub") + " &c(Error #4)"));
            event.setCancelled(true);
            return;
        }

        event.getPlayer().sendMessage(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-system.connected").replace("%server%", serverInfo.getName())));
        event.setCancelled(true);
        event.setCancelServer(serverInfo);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPostLogin(PostLoginEvent event) {
        if (!OxygenBungee.getInstance().getServerManager().isMaintenanceMode()) {
            return;
        }

        if (OxygenBungee.getInstance().getServerManager().getMaintenanceList().contains(event.getPlayer().getUniqueId())) {
            return;
        }

        event.getPlayer().disconnect(BungeeCC.translate(OxygenBungee.getInstance().getLanguageConfig().getConfiguration().getString("server-system.maintenance")));
    }
}
