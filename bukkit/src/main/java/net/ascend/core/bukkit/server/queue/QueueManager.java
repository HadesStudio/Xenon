package net.ascend.core.bukkit.server.queue;

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

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.queue.packet.QueuePositionNotificationPacket;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueLeavePacket;
import net.ascend.core.bukkit.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QueueManager {
    public List<String> getQueues(UUID uuid) {
        return new ArrayList<>(XenonBukkit.getInstance().getRedisModule().executeBackendCommand(redis ->
                redis.hgetAll(String.format(Queue.POSITION_FORMAT, uuid.toString())).keySet()));
    }

    public boolean isQueueingFor(UUID uuid, String server) {
        return getQueues(uuid).contains(server);
    }

    public int getPosition(UUID uuid, String server) {
        return Integer.parseInt(XenonBukkit.getInstance().getRedisModule().executeBackendCommand(redis ->
                redis.hget(String.format(Queue.POSITION_FORMAT, uuid.toString()), server)));
    }

    public String getPrimaryQueue(UUID uuid) {
        List<String> queues = getQueues(uuid);
        return queues.isEmpty() ? null : queues.get(0);
    }

    public List<UUID> getQueueing(String server) {
        return XenonBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
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
            XenonBukkit.getInstance().getRedisModule().sendPacket(new QueueLeavePacket(server, uuid));
            XenonBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
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
        XenonBukkit.getInstance().getTaskUtil().runAsyncTimer(() -> {
            if (System.currentTimeMillis() >= lastMessage.get() + TimeUnit.SECONDS.toMillis(15)) {
                AtomicInteger position = new AtomicInteger(0);

                List<String> message = XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("queue-system.waiting");

                synchronized (XenonBukkit.getInstance().getQueue().getPlayers()) {
                    XenonBukkit.getInstance().getQueue().getPlayers().forEach(player ->
                            XenonBukkit.getInstance().getRedisModule().sendPacket(new QueuePositionNotificationPacket(player, message, position.incrementAndGet(), XenonBukkit.getInstance().getQueue().getPlayers().size())));

                    lastMessage.set(System.currentTimeMillis());
                }
            }

            if (XenonBukkit.getInstance().getServerData().isQueuePaused()) {
                return;
            }

            for (int i = 0; i < XenonBukkit.getInstance().getServerData().getQueueRate(); i++)
                XenonBukkit.getInstance().getQueue().sendNext();
        }, 20L, 20L);
    }
}
