package dev.hely.core.bungee.server.data.proxy;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.data
//   Date: Monday, August 14, 2023 - 11:18 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonObject;
import dev.hely.core.bungee.server.ServerManager;
import dev.hely.core.bungee.server.data.proxy.data.ProxyServerRegion;
import dev.hely.core.bungee.server.data.proxy.data.ProxyServerStatus;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Proxy {

    private String name;
    private Integer port;
    private ProxyServerStatus status;
    private ProxyServerRegion region;
    private AtomicInteger onlinePlayers;
    private AtomicInteger maximumPlayers;

    public Proxy(String name, Integer port) {
        this.name = name;
        this.port = port;

        this.status = ProxyServerStatus.OFFLINE;
        this.region = ProxyServerRegion.UNITED_STATES;

        this.onlinePlayers = new AtomicInteger(0);
        this.maximumPlayers = new AtomicInteger(0);
    }

    public Proxy(String name, ProxyServerRegion region, Plugin plugin) {
        this.name = name;
        this.port = 25565;

        this.status = ProxyServerStatus.ONLINE;
        this.region = region;

        this.onlinePlayers = new AtomicInteger(plugin.getProxy().getPlayers().size());
        this.maximumPlayers = new AtomicInteger(plugin.getProxy().getConfig().getPlayerLimit());
    }

    public Proxy(JsonObject jsonObject) {
        this.name = jsonObject.get("name").getAsString();
        this.port = jsonObject.get("port").getAsInt();

        this.status = ProxyServerStatus.valueOf(jsonObject.get("status").getAsString());
        this.region = ProxyServerRegion.valueOf(jsonObject.get("region").getAsString());

        this.onlinePlayers = new AtomicInteger(jsonObject.get("onlinePlayers").getAsInt());
        this.maximumPlayers = new AtomicInteger(jsonObject.get("maximumPlayers").getAsInt());
    }

    public JsonObject toJsonObject() {
        JsonObject toReturn = new JsonObject();

        toReturn.addProperty("name",this.name);
        toReturn.addProperty("port",this.port);

        toReturn.addProperty("status",this.status.name());
        toReturn.addProperty("region",this.region.name());

        toReturn.addProperty("onlinePlayers",this.onlinePlayers.get());
        toReturn.addProperty("maximumPlayers",this.maximumPlayers.get());

        return toReturn;
    }

    public void refresh(JsonObject jsonObject) {
        this.status = ProxyServerStatus.valueOf(jsonObject.get("status").getAsString());
        this.region = ProxyServerRegion.valueOf(jsonObject.get("region").getAsString());

        this.onlinePlayers.set(jsonObject.get("onlinePlayers").getAsInt());
        this.maximumPlayers.set(jsonObject.get("maximumPlayers").getAsInt());
    }

    public void cache(JedisPool pool) {
        String key = ServerManager.PROXY_CACHE_KEY + this.port;

        try (Jedis jedis = pool.getResource()) {
            jedis.hset(key,"name",this.name);
            jedis.hset(key,"port","" + this.port);

            jedis.hset(key,"status",this.status.name());
            jedis.hset(key,"region",this.region.name());

            jedis.hset(key,"onlinePlayers","" + this.onlinePlayers.get());
            jedis.hset(key,"maximumPlayers",""+ this.maximumPlayers.get());

            jedis.hset(ServerManager.PROXY_LOOKUP_KEY + "port","" + this.port,this.name);
            jedis.hset(ServerManager.PROXY_LOOKUP_KEY + "name",this.name,"" + this.port);
        }

    }
}
