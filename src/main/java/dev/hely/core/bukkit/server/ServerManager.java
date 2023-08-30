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
import dev.hely.core.bukkit.server.queue.Queue;
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
    private List<Server> servers;
    public static String SERVER_CACHE_KEY = "oxygen:server-cache:";
    public static String SERVER_LOOKUP_KEY = "oxygen:server-lookup:";
    public static int REDIS_KEY = 15;

    @Override
    public void enable() {
        this.servers = new ArrayList<>();

        this.loadServers();
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
        this.servers.removeIf(next -> System.currentTimeMillis() - next.getLastHeartBeat() >= 15000L);
    }

    public List<Server> getAvailableServers() {
        List<Server> toReturn = new ArrayList<>();
        for (Server server : this.servers) {
            if (server.getStatus().equalsIgnoreCase("Offline")) {
                OxygenBukkit.getInstance().getLogger().info(server.getName() + " is currently offline.");
                continue;
            }
            if (server.getOnlinePlayers() >= server.getMaximumPlayers()) {
                continue;
            }

            toReturn.add(server);
        }

        return toReturn;
    }




    public void loadServers() {
        try (Jedis jedis = OxygenBukkit.getInstance().getRedisModule().getJedisPool().getResource()) {
            for (String path : jedis.keys("oxygen:server-cache:*")) {
                OxygenBukkit.getInstance().debug_message("Loading server cache:");
                Map<String, String> map = jedis.hgetAll(path);

                if (map == null || map.isEmpty()) {
                    continue;
                }
                Server server = this.getOrSetupServer(map.get("name"));
                if (server == null) {
                    this.servers.add(server = new Server(map.get("name")));
                }

                if (map.get("lastTps") == null) {
                    server.setLastTps(0.0);
                } else {
                    server.setLastTps(Double.parseDouble(map.get("lastTps")));
                }
                server.setStatus(map.get("status"));
                server.setOnlinePlayers(Integer.parseInt(map.get("onlinePlayers")));
                server.setMaximumPlayers(Integer.parseInt(map.get("maximumPlayers")));
                server.setLastHeartBeat(Long.parseLong(map.get("lastHeartBeat")));
                server.setUseQueue(Boolean.parseBoolean(map.get("queue")));
                server.setLastHeartBeat(Long.parseLong(map.get("lastHeartBeat")));
                server.setStartupTime(System.currentTimeMillis());

                Queue queue = Queue.getByName(server.getName());
                if (queue == null && server.isUseQueue()) {
                    queue = new Queue(server.getName(), server.getName());
                    queue.setEnabled(true);
                }

                if (!this.servers.contains(server)) {
                    this.servers.add(server);
                }
            }
        }
    }
}