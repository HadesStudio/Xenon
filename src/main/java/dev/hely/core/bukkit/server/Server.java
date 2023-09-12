package dev.hely.core.bukkit.server;

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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
@Setter
@RequiredArgsConstructor
public class Server {
    @Getter private final String name;
    private double lastTps;
    private String status;
    private int onlinePlayers;
    private int maximumPlayers;
    private boolean useQueue;
    private long lastHeartBeat;
    private long startupTime;

    public boolean canJoin() {
        return this.maximumPlayers > this.onlinePlayers;
    }

    public void cache(JedisPool pool) {
        String key = ServerManager.SERVER_CACHE_KEY + this.name;
        try (Jedis jedis = pool.getResource()) {

            jedis.hset(key,"name",this.name);

            jedis.hset(key,"lastTps","" + this.getLastTps());
            jedis.hset(key,"status", this.status);
            jedis.hset(key, "queue", String.valueOf(this.useQueue));
            jedis.hset(key,"onlinePlayers","" + this.onlinePlayers);
            jedis.hset(key,"maximumPlayers",""+ this.maximumPlayers);
            jedis.hset(key, "lastHeartBeat", ""+ this.lastHeartBeat);
            jedis.hset(key, "startupTime", ""+ this.startupTime);

            jedis.hset(ServerManager.SERVER_LOOKUP_KEY + "name",this.name,"" + this.name);
        }
    }
}
