package dev.hely.core.bukkit.server.queue;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue
//   Date: Tuesday, August 15, 2023 - 6:03 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.Server;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Queue {

    @Getter private static List<Queue> queues = new ArrayList<>();

    private String name;
    private String formattedName;
    private PriorityQueue<QueuePlayer> players = new PriorityQueue<>(QueuePlayer::compareTo);
    private boolean enabled;

    public Queue(String name, String formattedName) {
        this.name = name;
        this.formattedName = formattedName;

        queues.add(this);
    }

    public Server getServerData() {
        return OxygenBukkit.getInstance().getServerManager().getOrSetupServer(this.name);
    }

    public int getPosition(UUID uuid) {
        try {
            if(!this.containsPlayer(uuid)) {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }

        PriorityQueue<QueuePlayer> queue = new PriorityQueue<>(this.players);
        int position = 0;

        while (!queue.isEmpty()) {
            QueuePlayer player = queue.poll();

            if(player.getUuid().equals(uuid)) {
                break;
            }

            position++;
        }

        return position + 1;
    }

    public static Queue getByName(String name) {
        return queues.stream().filter(queue -> queue.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Queue getByUuid(UUID uuid) {
        for(Queue queue : Queue.getQueues()) {
            for(QueuePlayer queuePlayer : queue.getPlayers()) {
                if(queuePlayer.getUuid().equals(uuid)) {
                    return queue;
                }
            }
        }

        return null;
    }

    public boolean containsPlayer(UUID uuid) {
        for (QueuePlayer player : this.players) {
            if (player.getUuid().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    public void cache(JedisPool pool) {
        String key = QueueManager.QUEUE_CACHE_KEY + this.name;

        try (Jedis jedis = pool.getResource()) {
            jedis.hset(key,"name",this.name);
            jedis.hset(key,"formatedName", this.formattedName);
            jedis.hset(key, "enabled", String.valueOf(this.enabled));
            jedis.hset(key, "players", OxygenBukkit.PLAIN_GSON.toJson(this.players.stream().map(QueuePlayer::toString).collect(Collectors.toList())));

            jedis.hset(QueueManager.QUEUE_LOOKUP_KEY + "name", this.name, String.valueOf(this.enabled));
        }
    }

}

