package dev.hely.core.bukkit.server.queue;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue
//   Date: Tuesday, August 15, 2023 - 6:01 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.queue.packets.QueuePositionNotificationPacket;
import dev.hely.core.bukkit.server.queue.packets.implement.QueueLeavePacket;
import dev.hely.core.bukkit.utils.UUIDUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QueueManager {
    public List<String> getQueues(UUID uuid) {
        return new ArrayList<>(OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis ->
                redis.hgetAll(String.format(Queue.POSITION_FORMAT, uuid.toString())).keySet()));
    }

    public boolean isQueueingFor(UUID uuid, String server) {
        return getQueues(uuid).contains(server);
    }

    public int getPosition(UUID uuid, String server) {
        return Integer.parseInt(OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis ->
                redis.hget(String.format(Queue.POSITION_FORMAT, uuid.toString()), server)));
    }

    public String getPrimaryQueue(UUID uuid) {
        List<String> queues = getQueues(uuid);
        return queues.isEmpty() ? null : queues.get(0);
    }

    public List<UUID> getQueueing(String server) {
        return OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
            if (!redis.exists(String.format(Queue.PLAYERS_FORMAT, server)))
                return new ArrayList<>();

            if (redis.get(String.format(Queue.PLAYERS_FORMAT, server)).isEmpty())
                return new ArrayList<>();

            List<UUID> queueing = new ArrayList<>();
            for (String s : redis.get(String.format(Queue.PLAYERS_FORMAT, server)).split(";")) {
                if (!UUIDUtil.isUUID(s))
                    continue;

                queueing.add(UUID.fromString(s));
            }

            return queueing;
        });
    }

    public void resetQueueData(UUID uuid) {
        getQueues(uuid).forEach(server -> {
            OxygenBukkit.getInstance().getRedisModule().sendPacket(new QueueLeavePacket(server, uuid));
            OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
                redis.del(String.format(Queue.POSITION_FORMAT, uuid.toString()));
                redis.hdel(String.format(Queue.WEIGHT_FORMAT, server), uuid.toString());

                if (!redis.exists(String.format(Queue.PLAYERS_FORMAT, server)))
                    return new ArrayList<>();

                if (redis.get(String.format(Queue.PLAYERS_FORMAT, server)).isEmpty())
                    return new ArrayList<>();

                List<String> inQueue = new ArrayList<>();
                for (String s : redis.get(String.format(Queue.PLAYERS_FORMAT, server)).split(";")) {
                    if (!UUIDUtil.isUUID(s))
                        continue;

                    inQueue.add(s);
                }

                inQueue.remove(uuid.toString());
                redis.set(String.format(Queue.PLAYERS_FORMAT, server), StringUtils.join(inQueue, ";"));
                inQueue.clear();
                return null;
            });
        });
    }

    public void startTask() {
        AtomicLong lastMessage = new AtomicLong(System.currentTimeMillis());
        OxygenBukkit.getInstance().getTasks().runAsyncTimer(() -> {
            if (System.currentTimeMillis() >= lastMessage.get() + TimeUnit.SECONDS.toMillis(15)) {
                AtomicInteger position = new AtomicInteger(0);

                List<String> message = OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("queue-system.waiting");

                synchronized (OxygenBukkit.getInstance().getQueue().getPlayers()) {
                    OxygenBukkit.getInstance().getQueue().getPlayers().forEach(player ->
                            OxygenBukkit.getInstance().getRedisModule().sendPacket(new QueuePositionNotificationPacket(player, message, position.incrementAndGet(), OxygenBukkit.getInstance().getQueue().getPlayers().size())));

                    lastMessage.set(System.currentTimeMillis());
                }
            }

            if (OxygenBukkit.getInstance().getServerData().isQueuePaused()) {
                return;
            }

            for (int i = 0; i < OxygenBukkit.getInstance().getServerData().getQueueRate(); i++)
                OxygenBukkit.getInstance().getQueue().sendNext();
        }, 20L, 20L);
    }
}
