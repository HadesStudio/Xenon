package dev.hely.core.bukkit.server;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.server.packets.ServerStateChangePacket;
import dev.hely.core.bukkit.server.packets.ServerUpdatePacket;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bukkit.utils.TPSUtil;
import dev.hely.lib.Tasks;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ServerManager implements ModuleLoader {
    private boolean enabled;
    private final List<Server> servers = new ArrayList<>();

    @Override
    public void enable() {
        this.startServerMonitor();
    }

    @Override
    public void disable() {
    }

    @Override
    public void reload() {

    }

    @Override
    public String getName() {
        return "Server";
    }

    public Server getOrSetupServer(String server_name) {
        Server data = this.getServer(server_name);

        if (data != null) {
            return data;
        }
        data = new Server(server_name);
        this.servers.add(data);
        return data;
    }

    public Server getServer(String name) {
        return this.getServerList().stream().filter(data -> data.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Server> getServerList() {
        return new ArrayList<>(this.servers);
    }

    public int getRealPlayerCount() {
        int i = 0;

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (OxygenBukkit.getInstance().getStaffManager().isVanished(online) || OxygenBukkit.getInstance().getStaffManager().isStaffModeActive(online)) {
                continue;
            }
            i++;
        }

        return i;
    }

    public void removeClosedServers() {
        this.servers.removeIf(next -> System.currentTimeMillis() - next.getLastHeartbeat() >= 15000L);
    }

    public List<Server> getAvailableServers() {
        List<Server> toReturn = new ArrayList<>();
        for (Server server : this.servers) {
            if (!server.isOnline()) {
                OxygenBukkit.getInstance().getLogger().info(server.getName() + " is currently offline.");
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
        OxygenBukkit.getInstance().getTasks().runAsyncTimer(() -> {
            getServers().stream().
                    filter(server -> System.currentTimeMillis() - server.getLastHeartbeat() > 15_000
                            && server.getState() != ServerState.HEARTBEAT_TIMEOUT
                            && server.getState() != ServerState.OFFLINE).forEach(server -> {
                        server.setState(ServerState.HEARTBEAT_TIMEOUT);
                        OxygenBukkit.getInstance().getRedisModule().sendPacket(new ServerStateChangePacket(server.getName(), ServerState.ONLINE));
                    });

            OxygenBukkit.getInstance().getServerData().setTps(TPSUtil.getRecentTps()[0]);
            OxygenBukkit.getInstance().getServerData().setLastHeartbeat(System.currentTimeMillis());
            OxygenBukkit.getInstance().getServerData().setState(Bukkit.hasWhitelist() ? ServerState.WHITELISTED : ServerState.ONLINE);
            OxygenBukkit.getInstance().getServerData().setOnlinePlayers(Bukkit.getOnlinePlayers().size());
            OxygenBukkit.getInstance().getServerData().setMaxPlayers(Bukkit.getMaxPlayers());
            OxygenBukkit.getInstance().getServerData().setQueueEnabled(OxygenBukkit.getInstance().getServerData().isQueueEnabled());
            OxygenBukkit.getInstance().getServerData().setQueuePaused(OxygenBukkit.getInstance().getServerData().isQueuePaused());
            OxygenBukkit.getInstance().getServerData().setQueueRate(OxygenBukkit.getInstance().getServerData().getQueueRate());
            OxygenBukkit.getInstance().getServerData().setPlayersInQueue(OxygenBukkit.getInstance().getQueue().getPlayers().size());

            OxygenBukkit.getInstance().getRedisModule().sendPacket(new ServerUpdatePacket(OxygenBukkit.getInstance().getServerData()));

        }, 20L, 20L);
    }

    public void updateServer(Server server) {
        OxygenBukkit.getInstance().getServerManager().getServers().add(server);
    }
}