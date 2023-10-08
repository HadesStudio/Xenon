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
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.server.queue.packets.implement.QueueSendPlayerPacket;
import dev.hely.core.bukkit.utils.UUIDUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Queue {

    public static final String WEIGHT_FORMAT = "queue:%s:weight";
    public static final String PLAYERS_FORMAT = "queue:%s:players";
    public static final String POSITION_FORMAT = "queue-data:%s:position";

    @Getter
    private final LinkedList<UUID> players = new LinkedList<>();
    private final ConcurrentHashMap<UUID, Integer> weight = new ConcurrentHashMap<>();
    private final OxygenBukkit oxygen;

    public int getJoinPosition(Profile profile) {
        if (players.isEmpty())
            return -1;

        int playerWeight = profile.getQueuePriority();
        if (playerWeight == 0)
            return -1;

        for (int i = 0; i < players.size(); i++) {
            Integer otherWeight = weight.get(players.get(i));
            if (playerWeight >= otherWeight)
                return playerWeight > otherWeight ? i : i + 1;
        }

        return -1;
    }

    public void addPlayer(Profile profile) {
        if (players.contains(profile.getUniqueId()))
            return;
        int position = getJoinPosition(profile);
        if (position == -1)
            players.addLast(profile.getUniqueId());
        else players.add(position, profile.getUniqueId());

        weight.put(profile.getUniqueId(), profile.getActiveRank().getWeight());
        updatePositions();
    }

    public void removePlayer(Profile profile) {
        if (!players.contains(profile.getUniqueId()))
            return;

        players.remove(profile.getUniqueId());
        weight.remove(profile.getUniqueId());
        updatePositions();
        deleteQueueData(profile.getUniqueId());
    }

    public void save() {
        OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
            List<String> strings = new ArrayList<>();
            for (UUID player : players) {
                strings.add(player.toString());
            }

            redis.set(String.format(PLAYERS_FORMAT, OxygenBukkit.getInstance().getServerData().getName()), StringUtils.join(strings, ";"));
            strings.clear();
            weight.forEach((uuid, value) -> redis.hset(
                    String.format(WEIGHT_FORMAT, OxygenBukkit.getInstance().getServerData().getName()), uuid.toString(), String.valueOf(value))
            );
            return null;
        });
    }

    public void load() {
        OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
            players.clear();
            if (redis.exists(String.format(PLAYERS_FORMAT, OxygenBukkit.getInstance().getServerData().getName()))) {
                players.addAll(Arrays.stream(redis.get(String.format(PLAYERS_FORMAT, OxygenBukkit.getInstance().getServerData().getName())).split(";"))
                        .filter(UUIDUtil::isUUID)
                        .map(UUID::fromString)
                        .collect(Collectors.toList()));
            }

            weight.clear();
            redis.hgetAll(String.format(WEIGHT_FORMAT, OxygenBukkit.getInstance().getServerData().getName()))
                    .forEach((uuid, value) -> weight.put(UUID.fromString(uuid), Integer.valueOf(value)));
            return null;
        });
    }

    private void updatePositions() {
        OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
            for (UUID uuid : players) {
                if (uuid == null)
                    continue;

                redis.hset(String.format(POSITION_FORMAT, uuid), OxygenBukkit.getInstance().getServerData().getName(),
                        String.valueOf(players.indexOf(uuid)));
            }


            List<String> strings = new ArrayList<>();
            for (UUID player : players) {
                strings.add(player.toString());
            }

            redis.set(String.format(PLAYERS_FORMAT, OxygenBukkit.getInstance().getServerData().getName()), StringUtils.join(strings, ";"));
            strings.clear();
            return null;
        });
        save();
    }

    private void deleteQueueData(UUID uuid) {
        OxygenBukkit.getInstance().getRedisModule().executeBackendCommand(redis -> {
            redis.hdel(String.format(POSITION_FORMAT, uuid.toString()), OxygenBukkit.getInstance().getServerData().getName());
            return null;
        });
    }

    public void sendNext() {
        if (players.isEmpty())
            return;

        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers())
            return;

        UUID next = players.removeFirst();
        if (next == null)
            return;

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new QueueSendPlayerPacket(OxygenBukkit.getInstance().getServerData().getName(), next));
        updatePositions();
        deleteQueueData(next);
    }

}