package net.ascend.core.bukkit.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server
//   Date: Sunday, August 13, 2023 - 1:05 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.server.packet.ServerStateChangePacket;
import net.ascend.core.bukkit.server.packet.ServerUpdatePacket;
import net.ascend.core.bukkit.util.TPSUtil;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class ServerManager implements ModuleLoader {

    private final Map<String, Server> servers = new HashMap<>();

    @Override
    public void onEnable() {
        for (Document document : XenonBukkit.getInstance().getMongoModule().getServerCollection().find()) {
            Server server = new Server(document.getString("name"));
            server.setState(ServerState.valueOf(document.getString("state")));
            server.setMaxPlayers(document.getInteger("maxPlayers"));
            server.setQueueRate(document.getInteger("queueRate"));
            server.setQueuePaused(document.getBoolean("queuePaused"));
            server.setQueueEnabled(document.getBoolean("queueEnabled"));

            servers.put(server.getName(), server);
        }

        this.startServerMonitor();
        this.removeClosedServers();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onReload() {

    }

    public List<Server> getServersList() {
        return (new ArrayList<>(servers.values()));
    }

    public Server getServer(String name) {
        return this.getServersList().stream().filter(data -> data.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public int getRealPlayerCount() {
        int i = 0;

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (XenonBukkit.getInstance().getStaffManager().isVanished(online) || XenonBukkit.getInstance().getStaffManager().isStaffModeActive(online)) {
                continue;
            }
            i++;
        }

        return i;
    }

    public void removeClosedServers() {
        getServersList().removeIf(next -> System.currentTimeMillis() - next.getLastHeartbeat() >= 15000L);
    }

    public List<Server> getAvailableServers() {
        List<Server> toReturn = new ArrayList<>();
        for (Server server : getServersList()) {
            if (!server.isOnline()) {
                XenonBukkit.getInstance().getLogger().info(server.getName() + " is currently offline.");
                continue;
            }
            if (server.getOnlinePlayers() >= server.getMaxPlayers()) {
                continue;
            }

            toReturn.add(server);
        }

        return toReturn;
    }

    public void startServerMonitor() {
        // We use this as a replacement for the HeartBeatRunnable:
        XenonBukkit.getInstance().getTaskUtil().runAsyncTimer(() -> {
            for (Server server : getServersList()) {
                if (System.currentTimeMillis() - server.getLastHeartbeat() > 15_000) {
                    if (server.getState() != ServerState.HEARTBEAT_TIMEOUT && server.getState() != ServerState.OFFLINE) {
                        if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                            continue;
                        }
                        server.setState(ServerState.HEARTBEAT_TIMEOUT);
                        XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerStateChangePacket(server.getName(), server.getState()));
                    }
                }
            }

            XenonBukkit.getInstance().getServerData().setTps(TPSUtil.getRecentTps()[0]);
            XenonBukkit.getInstance().getServerData().setLastHeartbeat(System.currentTimeMillis());
            XenonBukkit.getInstance().getServerData().setState(Bukkit.hasWhitelist() ? ServerState.WHITELISTED : ServerState.ONLINE);
            XenonBukkit.getInstance().getServerData().setOnlinePlayers(Bukkit.getOnlinePlayers().size());
            XenonBukkit.getInstance().getServerData().setMaxPlayers(Bukkit.getMaxPlayers());
            XenonBukkit.getInstance().getServerData().setQueueEnabled(XenonBukkit.getInstance().getServerData().isQueueEnabled());
            XenonBukkit.getInstance().getServerData().setQueuePaused(XenonBukkit.getInstance().getServerData().isQueuePaused());
            XenonBukkit.getInstance().getServerData().setQueueRate(XenonBukkit.getInstance().getServerData().getQueueRate());
            XenonBukkit.getInstance().getServerData().setPlayersInQueue(XenonBukkit.getInstance().getQueue().getPlayers().size());

            XenonBukkit.getInstance().getRedisModule().sendPacket(new ServerUpdatePacket(XenonBukkit.getInstance().getServerData()));
        }, 40L, 40L);
    }

    public void updateServer(Server server) {
        getServers().remove(server.getName(), server);
        getServers().put(server.getName(), server);
    }
}