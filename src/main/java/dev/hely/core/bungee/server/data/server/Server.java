package dev.hely.core.bungee.server.data.server;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data.server
//   Date: Monday, August 14, 2023 - 11:25 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonObject;
import dev.hely.core.bungee.server.ServerManager;
import dev.hely.core.bungee.server.data.server.data.ServerPerformanceData;
import dev.hely.core.bungee.server.data.server.data.StatusData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Setter
public class Server {

    @Getter private String name;
    @Getter private Integer port;

    private ServerPerformanceData performance;
    private StatusData status;
    private String group;
    private AtomicInteger onlinePlayers;
    private AtomicInteger maximumPlayers;
    private List<UUID> oppedPlayers;
    private List<UUID> whitelistedPlayers;
    private long lastHeartBeat;
    private boolean useQueue;

    public Server(String name,Integer port) {
        this.name = name;
        this.port = port;
        this.performance = new ServerPerformanceData(20.0D,0.0D);
        this.status = StatusData.OFFLINE;
        this.group = "Lobbies";
        this.onlinePlayers = new AtomicInteger(0);
        this.maximumPlayers = new AtomicInteger(0);
        this.oppedPlayers = new ArrayList<>();
        this.whitelistedPlayers = new ArrayList<>();
        this.lastHeartBeat = System.currentTimeMillis();
        this.useQueue = true;
    }

    public Server(String name, String group, JavaPlugin plugin, boolean stopping) {
        this.name = name;
        this.port = plugin.getServer().getPort();
        this.performance = new ServerPerformanceData(20.0,10.0);
        this.status = plugin.getServer().hasWhitelist() ? StatusData.WHITELISTED : StatusData.ONLINE;
        if (stopping) {
            this.status = StatusData.OFFLINE;
        }
        this.group = group;
        this.onlinePlayers = new AtomicInteger(plugin.getServer().getOnlinePlayers().size());
        this.maximumPlayers = new AtomicInteger(plugin.getServer().getMaxPlayers());
        this.lastHeartBeat = System.currentTimeMillis();
        this.oppedPlayers = plugin.getServer().getOperators().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList());
        this.whitelistedPlayers = plugin.getServer().getWhitelistedPlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList());
        this.useQueue = true;
    }

    public Server(JsonObject jsonObject) {
        this.name = jsonObject.get("name").getAsString();
        this.port = jsonObject.get("port").getAsInt();
        this.performance = new ServerPerformanceData(jsonObject.get("performance").getAsJsonObject());
        this.status = StatusData.valueOf(jsonObject.get("status").getAsString());
        this.group = jsonObject.get("group").getAsString();
        this.lastHeartBeat = jsonObject.get("lastHeartBeat").getAsLong();
        this.useQueue = jsonObject.get("queue").getAsBoolean();
        this.onlinePlayers = new AtomicInteger(jsonObject.get("onlinePlayers").getAsInt());
        this.maximumPlayers = new AtomicInteger(jsonObject.get("maximumPlayers").getAsInt());
        this.oppedPlayers = ServerManager.GSON.<List<String>>fromJson(jsonObject.get("oppedPlayers").getAsString(),ArrayList.class).stream().map(UUID::fromString).collect(Collectors.toList());
        this.whitelistedPlayers = ServerManager.GSON.<List<String>>fromJson(jsonObject.get("whitelistedPlayers").getAsString(),ArrayList.class).stream().map(UUID::fromString).collect(Collectors.toList());
    }

    public JsonObject toJsonObject() {
        JsonObject toReturn = new JsonObject();

        toReturn.addProperty("name",this.name);
        toReturn.addProperty("port",this.port);
        toReturn.add("performance",this.performance.toJsonObject());
        toReturn.addProperty("status", this.status.name());
        toReturn.addProperty("group", this.group);
        toReturn.addProperty("queue", this.useQueue);
        toReturn.addProperty("onlinePlayers", this.onlinePlayers.get());
        toReturn.addProperty("maximumPlayers", this.maximumPlayers.get());
        toReturn.addProperty("lastHeartBeat", System.currentTimeMillis());
        toReturn.addProperty("oppedPlayers", ServerManager.GSON.toJson(this.oppedPlayers.stream().map(UUID::toString).collect(Collectors.toList())));
        toReturn.addProperty("whitelistedPlayers", ServerManager.GSON.toJson(this.whitelistedPlayers.stream().map(UUID::toString).collect(Collectors.toList())));

        return toReturn;
    }

    public void refresh(JsonObject jsonObject) {
        this.performance = new ServerPerformanceData(jsonObject.get("performance").getAsJsonObject());
        this.status = StatusData.valueOf(jsonObject.get("status").getAsString());
        this.group = jsonObject.get("group").getAsString();
        this.onlinePlayers.set(jsonObject.get("onlinePlayers").getAsInt());
        this.maximumPlayers.set(jsonObject.get("maximumPlayers").getAsInt());
        this.lastHeartBeat= jsonObject.get("lastHeartBeat").getAsLong();
        this.useQueue = jsonObject.get("queue").getAsBoolean();
        this.oppedPlayers = ServerManager.GSON.<List<String>>fromJson(jsonObject.get("oppedPlayers").getAsString(),ArrayList.class).stream().map(UUID::fromString).collect(Collectors.toList());
        this.whitelistedPlayers = ServerManager.GSON.<List<String>>fromJson(jsonObject.get("whitelistedPlayers").getAsString(),ArrayList.class).stream().map(UUID::fromString).collect(Collectors.toList());
    }

    public void cache(JedisPool pool) {
        String key = ServerManager.SERVER_CACHE_KEY + this.port;

        try (Jedis jedis = pool.getResource()) {

            jedis.hset(key,"name",this.name);
            jedis.hset(key,"port","" + this.port);

            jedis.hset(key,"performance","" + this.performance.toJsonObject());

            jedis.hset(key,"status",this.status.name());
            jedis.hset(key,"group",this.group);
            jedis.hset(key, "queue", String.valueOf(this.useQueue));
            jedis.hset(key,"onlinePlayers","" + this.onlinePlayers.get());
            jedis.hset(key,"maximumPlayers",""+ this.maximumPlayers.get());
            jedis.hset(key, "lastHeartBeat", ""+ this.lastHeartBeat);
            jedis.hset(key,"oppedPlayers", ServerManager.GSON.toJson(this.oppedPlayers.stream().map(UUID::toString).collect(Collectors.toList())));
            jedis.hset(key,"whitelistedPlayers", ServerManager.GSON.toJson(this.whitelistedPlayers.stream().map(UUID::toString).collect(Collectors.toList())));

            jedis.hset(ServerManager.SERVER_LOOKUP_KEY + "port","" + this.port,this.name);
            jedis.hset(ServerManager.SERVER_LOOKUP_KEY + "name",this.name,"" + this.port);
        }
    }

    public boolean canJoin(UUID uuid) {
        if (this.status == StatusData.WHITELISTED) {
            return this.oppedPlayers.contains(uuid) || this.whitelistedPlayers.contains(uuid);
        }

        if (this.maximumPlayers.get() <= this.onlinePlayers.get()) {
            return false;
        }

        return this.status == StatusData.ONLINE;
    }
}
