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

import com.google.gson.JsonObject;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.rank.Rank;
import dev.hely.core.bukkit.server.queue.packets.implement.QueueRemovePlayerPacket;
import dev.hely.lib.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class QueueManager implements ModuleLoader {
    public boolean enabled;
    @Getter private Map<Rank, QueueRank> priorities = new LinkedHashMap<>();
    public static String QUEUE_CACHE_KEY = "oxygen:queue-cache:";
    public static String QUEUE_LOOKUP_KEY = "oxygen:queue-lookup:";

    @Getter @Setter private long interval = 20L;


    @Override
    public void enable() {
        this.loadPriorities();
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
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
        return "Queue";
    }

    public void changeInterval(long time) {
        this.interval = time;
    }

    private void loadPriorities() {
        for (Rank rank : OxygenBukkit.getInstance().getRankManager().getRanks()) {
            this.priorities.put(rank, new QueueRank(rank.getName(), rank.getQueuePriority()));
        }
    }

    public void removePlayer(UUID uuid) {
        JsonObject queueData = new JsonObject();
        queueData.addProperty("uuid", uuid.toString());
        queueData.addProperty("msg", false);

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new QueueRemovePlayerPacket(queueData));
    }

    public QueueRank getPriority(Player player) {
        return this.priorities.get(OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()).getActiveHighestRank());
    }
}
