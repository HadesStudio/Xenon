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
import dev.hely.core.bukkit.utils.TPSUtil;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class ServerManager implements ModuleLoader {
    private boolean enabled;
    private final Map<String, Server> servers = new HashMap<>();

    @Override
    public void enable() {
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getServerCollection().find()) {
            Server server = new Server(document.getString("name"));
            server.setState(ServerState.valueOf(document.getString("state")));
            server.setMaxPlayers(document.getInteger("maxPlayers"));
            server.setQueueRate(document.getInteger("queueRate"));
            server.setQueuePaused(document.getBoolean("queuePaused"));
            server.setQueueEnabled(document.getBoolean("queueEnabled"));

            OxygenBukkit.getInstance().debug_message("------------------------------------------");
            OxygenBukkit.getInstance().debug_message("Registering a Existing Server:");
            OxygenBukkit.getInstance().debug_message(" - Server Name: " + server.getName());
            OxygenBukkit.getInstance().debug_message(" - Server State: " + server.getState().getDisplayName());
            OxygenBukkit.getInstance().debug_message(" - Max Players: " + server.getMaxPlayers());
            OxygenBukkit.getInstance().debug_message(" - Queue Enabled: " + server.isQueueEnabled());
            OxygenBukkit.getInstance().debug_message(" - Queue Paused: " + server.isQueuePaused());
            OxygenBukkit.getInstance().debug_message(" - Queue Rate: " + server.getQueueRate());
            OxygenBukkit.getInstance().debug_message("------------------------------------------");

            servers.put(server.getName(), server);
        }

        this.startServerMonitor();
        this.removeClosedServers();
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


    public List<Server> getServersList() {
        return (new ArrayList<>(servers.values()));
    }

    public Server getServer(String name) {
        return this.getServersList().stream().filter(data -> data.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
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
        getServersList().removeIf(next -> System.currentTimeMillis() - next.getLastHeartbeat() >= 15000L);
    }

    public List<Server> getAvailableServers() {
        List<Server> toReturn = new ArrayList<>();
        for (Server server : getServersList()) {
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
        // We use this as a replacement for the HeartBeatRunnable:
        OxygenBukkit.getInstance().getTasks().runAsyncTimer(() -> {
            for (Server server : getServersList()) {
                if (System.currentTimeMillis() - server.getLastHeartbeat() > 15_000) {
                    if (server.getState() != ServerState.HEARTBEAT_TIMEOUT && server.getState() != ServerState.OFFLINE) {
                        if (server.getState() == ServerState.HEARTBEAT_TIMEOUT) {
                            continue;
                        }
                        server.setState(ServerState.HEARTBEAT_TIMEOUT);
                        OxygenBukkit.getInstance().getRedisModule().sendPacket(new ServerStateChangePacket(server.getName(), server.getState()));
                    }
                }
            }

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
        }, 40L, 40L);
    }

    public void updateServer(Server server) {
        getServers().remove(server.getName(), server);
        getServers().put(server.getName(), server);
    }
}