package dev.hely.core.bukkit.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server
//   Date: Monday, August 14, 2023 - 1:15 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.data.proxy.comparator.ProxyPlayersData;
import dev.hely.core.bungee.server.data.proxy.comparator.ProxyPortData;
import dev.hely.core.bungee.server.data.proxy.comparator.ProxyRegionData;
import dev.hely.core.bungee.server.data.proxy.comparator.ProxyStatusData;
import dev.hely.core.bungee.server.data.proxy.data.ProxyServerRegion;
import dev.hely.core.bungee.server.data.proxy.data.ProxyServerStatus;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.server.data.server.comparator.ServerPlayersData;
import dev.hely.core.bungee.server.data.server.comparator.ServerPortData;
import dev.hely.core.bungee.server.data.server.comparator.ServerStatusData;
import dev.hely.core.bungee.server.data.server.data.StatusData;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class BukkitServerManager implements ModuleLoader {

    private boolean enabled;


    private List<Proxy> proxies;
    private List<Server> servers;
    private List<String> serverGroups;
    @Getter @Setter private boolean maintenanceMode;
    @Getter private List<UUID> maintenanceList;

    public static String PROXY_CACHE_KEY = "oxygen:proxy-cache:";
    public static String SERVER_CACHE_KEY = "oxygen:server-cache:";

    public static String PROXY_LOOKUP_KEY = "oxygen:proxy-lookup:";
    public static String SERVER_LOOKUP_KEY = "oxygen:server-lookup:";

    public static int REDIS_KEY = 15;

    @Override
    public void enable() {
        this.proxies = new ArrayList<>();
        this.servers = new ArrayList<>();
        this.serverGroups = new ArrayList<>();

        setupGroupList();
        this.load();
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Server";
    }

    public void load() {
        try (Jedis jedis = OxygenBukkit.getInstance().getRedisModule().getJedisPool().getResource()) {
            if (jedis.get("oxygen:maintenanceMode") != null) {
                this.maintenanceMode = Boolean.parseBoolean(jedis.get("oxygen:maintenanceMode"));
            }
            if (jedis.get("oxygen:maintenanceList") != null) {
                this.maintenanceList = OxygenBukkit.PLAIN_GSON.<List<String>>fromJson(jedis.get("oxygen:maintenanceList"),ArrayList.class).stream().map(UUID::fromString).collect(Collectors.toList());
            } else {
                this.maintenanceList = new ArrayList<>();
            }

            for (String path : jedis.keys("oxygen:proxy-cache:*")) {
                String[] split = path.split(":");
                int port = Integer.parseInt(split[2]);

                Map<String,String> map = jedis.hgetAll(path);

                if (map == null || map.isEmpty()) {
                    continue;
                }

                Proxy proxy = this.proxyFromName(map.get("name"));

                if (proxy == null) {
                    this.proxies.add(proxy = new Proxy(map.get("name"),port));
                }

                proxy.setStatus(ProxyServerStatus.valueOf(map.get("status")));
                proxy.setRegion(ProxyServerRegion.valueOf(map.get("region")));

                proxy.setOnlinePlayers(new AtomicInteger(Integer.parseInt(map.get("onlinePlayers"))));
                proxy.setMaximumPlayers(new AtomicInteger(Integer.parseInt(map.get("maximumPlayers"))));

                if (!this.proxies.contains(proxy)) {
                    this.proxies.add(proxy);
                }

            }

            for (String path : jedis.keys("oxygen:server-cache:*")) {
                OxygenBukkit.getInstance().debug_message("Loading server cache:");
                Map<String, String> map = jedis.hgetAll(path);

                String[] split = path.split(":");
                int port = Integer.parseInt(split[2]);

                if (map == null || map.isEmpty()) {
                    continue;
                }

                Server server = this.serverFromName(map.get("name"));

                if (server == null) {
                    this.servers.add(server = new Server(map.get("name"),port, System.currentTimeMillis()));
                }

                server.setStatus(StatusData.valueOf(map.get("status")));
                server.setGroup(map.get("group"));
                server.setOnlinePlayers(new AtomicInteger(Integer.parseInt(map.get("onlinePlayers"))));
                server.setMaximumPlayers(new AtomicInteger(Integer.parseInt(map.get("maximumPlayers"))));
                server.setLastHeartBeat(Long.parseLong(map.get("lastHeartBeat")));
                server.setOppedPlayers(OxygenBukkit.PLAIN_GSON.<List<String>>fromJson(map.get("oppedPlayers"),ArrayList.class).stream().map(UUID::fromString).collect(Collectors.toList()));
                server.setWhitelistedPlayers(OxygenBukkit.PLAIN_GSON.<List<String>>fromJson(map.get("whitelistedPlayers"),ArrayList.class).stream().map(UUID::fromString).collect(Collectors.toList()));
                server.setUseQueue(Boolean.parseBoolean(map.get("queue")));

                OxygenBukkit.getInstance().debug_message("Creating new queue:");
                Queue queue = Queue.getByName(server.getName());
                if (queue == null && server.isUseQueue()) {
                    queue = new Queue(server.getName(), server.getName());
                    queue.setEnabled(true);
                    OxygenBukkit.getInstance().debug_message("Creating new queue - " + queue.getName());
                }

                if (!this.servers.contains(server)) {
                    this.servers.add(server);
                }
            }
        }
    }

    public Server serverFromName(String name) {
        return this.servers.stream().filter(server -> server.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Proxy proxyFromName(String name) {
        return this.proxies.stream().filter(proxy -> proxy.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Proxy> getSortedProxies() {
        return Collections.unmodifiableList(this.proxies.stream()
                .sorted(new ProxyRegionData().reversed()
                        .thenComparing(new ProxyPlayersData().reversed()
                                .thenComparing(new ProxyPortData().reversed())
                                .thenComparing(new ProxyStatusData().reversed())))
                .collect(Collectors.toList())
        );
    }

    public List<Server> getSortedServers() {
        return Collections.unmodifiableList(this.servers.stream()
                .sorted((new ServerPlayersData().reversed()
                        .thenComparing(new ServerPortData()).reversed()
                        .thenComparing(new ServerStatusData().reversed())))
                .collect(Collectors.toList()));
    }

    public List<Server> getAvailableServers(UUID uuid) {
        return getAvailableServers(uuid, this.serverGroups);
    }

    public List<Server> getAvailableServers(UUID uuid, String group) {
        OxygenBukkit.getInstance().debug_message("UUID: " + uuid);
        OxygenBukkit.getInstance().debug_message("Server Group: " + group);
        OxygenBukkit.getInstance().debug_message("Group List: " + Arrays.asList(group));
        return this.getAvailableServers(uuid,Arrays.asList(group));
    }

    public void setupGroupList() {
        this.serverGroups.addAll(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getStringList("server-groups"));
        OxygenBukkit.getInstance().debug_message("Server Groups List: " + serverGroups);
    }


    public List<Server> getAvailableServers(UUID uuid, List<String> groups) {
        List<Server> toReturn = new ArrayList<>();
        for (Server server : this.servers) {
            if (!groups.contains(server.getGroup())) {
                OxygenBukkit.getInstance().debug_message("NOPE 1");
                continue;
            }

            if (server.getStatus().equals(StatusData.OFFLINE)) {
                OxygenBukkit.getInstance().debug_message("NOPE 2");
                continue;
            }

            if (server.getOnlinePlayers().get() >= server.getMaximumPlayers().get() && !server.getOppedPlayers().contains(uuid)) {
                OxygenBukkit.getInstance().debug_message("NOPE 3");
                continue;
            }

            if (server.getStatus().equals(StatusData.WHITELISTED) && !(server.getOppedPlayers().contains(uuid) || server.getWhitelistedPlayers().contains(uuid))) {
                OxygenBukkit.getInstance().debug_message("NOPE 4");
                continue;
            }

            toReturn.add(server);
            OxygenBukkit.getInstance().debug_message("Adding server - " + server);
        }

        return toReturn;
    }

    public int getPlayersOnNetwork() {
        return this.getPlayersOnRegion(Arrays.asList(ProxyServerRegion.values()));
    }

    public int getPlayersOnRegion(List<ProxyServerRegion> regions) {
        AtomicInteger toReturn = new AtomicInteger(0);

        for (Proxy proxy : this.proxies) {

            if (!regions.contains(proxy.getRegion())) {
                continue;
            }

            toReturn.addAndGet(proxy.getOnlinePlayers().get());
        }

        return toReturn.get();
    }
}