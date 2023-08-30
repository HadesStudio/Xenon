package dev.hely.core.bukkit.server.queue.packets;

import com.google.gson.JsonObject;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bukkit.server.queue.QueuePlayer;
import dev.hely.core.bukkit.server.queue.QueueRank;
import dev.hely.core.bukkit.server.queue.packets.implement.*;
import dev.hely.core.bukkit.server.Server;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.UUID;

@AllArgsConstructor
public class QueuePacketListener implements PacketListener {
    @IncomingPacketHandler
    public void onLoadQueues(LoadRedisQueuesPacket packet) {
        if (!OxygenBukkit.getInstance().getServerConfig().getConfiguration().getBoolean("use-queue-system")) {
            return;
        }

        JsonObject queueJson = packet.getObject();
        String name = queueJson.get("name").getAsString();
        Queue queue = Queue.getByName(name);

        if (queue == null) {
            Server data = OxygenBukkit.getInstance().getServerManager().getOrSetupServer(name);
            queue = new Queue(name, data != null ? data.getName() : name);
        }

        PriorityQueue<QueuePlayer> players = new PriorityQueue<>(QueuePlayer::compareTo);

        queueJson.get("players").getAsJsonArray().forEach(playerElement -> {
            JsonObject player = playerElement.getAsJsonObject();
            JsonObject rank = player.get("rank").getAsJsonObject();

            QueueRank queueRank = new QueueRank();
            queueRank.setName(rank.get("rankName").getAsString());
            queueRank.setPriority(rank.get("priority").getAsInt());

        QueuePlayer queuePlayer = new QueuePlayer();
        queuePlayer.setUuid(UUID.fromString(player.get("uuid").getAsString()));
        queuePlayer.setRank(queueRank);
        queuePlayer.setInserted(player.get("inserted").getAsLong());

        players.add(queuePlayer);
    });

        queue.setPlayers(players);
        queue.setEnabled(queueJson.get("status").getAsBoolean());
    }


    @IncomingPacketHandler
    public void onRemovePlayerFromQueue(QueueRemovePlayerPacket packet) {
        UUID uuid = UUID.fromString(packet.getObject().get("uuid").getAsString());

        Queue queue = Queue.getByUuid(uuid);

        if (queue == null) {
            return;
        }

        QueuePlayer queuePlayer = null;

        Iterator<QueuePlayer> iterator = queue.getPlayers().iterator();

        while (iterator.hasNext()) {
            QueuePlayer other = iterator.next();

            if (other.getUuid().equals(uuid)) {
                queuePlayer = other;
                iterator.remove();
            }
        }

        if (queuePlayer == null) {
            return;
        }

        queue.getPlayers().removeIf(qp -> qp.getUuid().equals(uuid));

        if (packet.getObject().get("msg").getAsBoolean()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.removed").replace("%server%", queue.getName())));
            }
        }
    }

    @IncomingPacketHandler
    public void onQueueJoinPlayer(QueueAddPlayerPacket packet) {
        Queue queue = Queue.getByName(packet.getObject().get("name").getAsString());

        if (queue == null) {
            return;
        }

        JsonObject player = packet.getObject().get("player").getAsJsonObject();
        JsonObject rank = player.get("rank").getAsJsonObject();

        QueueRank queueRank = new QueueRank();
        queueRank.setName(rank.get("rankName").getAsString());
        queueRank.setPriority(rank.get("priority").getAsInt());

        QueuePlayer queuePlayer = new QueuePlayer();
        queuePlayer.setUuid(UUID.fromString(player.get("uuid").getAsString()));
        queuePlayer.setRank(queueRank);
        queuePlayer.setInserted(System.currentTimeMillis());

        queue.getPlayers().add(queuePlayer);

        Player bukkitPlayer = Bukkit.getPlayer(queuePlayer.getUuid());

        if (bukkitPlayer != null) {
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("queue-system.joined")) {
                bukkitPlayer.sendMessage(CC.translate(message)
                        .replace("%server%", queue.getName())
                        .replace("%position%", String.valueOf(queue.getPosition(bukkitPlayer.getUniqueId())))
                        .replace("%queue_size%", String.valueOf(queue.getPlayers().size()))
                );
            }
        }
    }

    @IncomingPacketHandler
    public void onQueueClearEvent(QueueClearPacket packet) {
        Queue queue = Queue.getByName(packet.getObject().get("name").getAsString());
        queue.getPlayers().clear();

        Player player = Bukkit.getPlayer(packet.getObject().get("by").getAsString());
        if (player != null && player.isOnline()) {
            player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.cleared").replace("%queue_size%", String.valueOf(queue.getPlayers().size())).replace("%server%", queue.getName())));
        }
    }

    @IncomingPacketHandler
    public void onQueueToggle(QueueTogglePacket packet) {
        boolean check = false;
        String name = packet.getObject().get("name").getAsString();

        if (packet.getObject().get("global").getAsBoolean()) {
            if (OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name").equals(name)) {
                OxygenBukkit.getInstance().getServerManager().getOrSetupServer(name).setUseQueue(!OxygenBukkit.getInstance().getServerManager().getOrSetupServer(name).isUseQueue());
                check = OxygenBukkit.getInstance().getServerManager().getOrSetupServer(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name")).isUseQueue();
            }
        } else {
            Queue queue = Queue.getByName(name);
            queue.setEnabled(!queue.isEnabled());
            check = queue.isEnabled();
        }

        Player player = Bukkit.getPlayer(packet.getObject().get("by").getAsString());
        if (check) {
            if (player != null && player.isOnline()) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.toggled.disabled").replace("%server%", name)));
            }
        } else {
            if (player != null && player.isOnline()) {
                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.toggled.enabled").replace("%server%", name)));
            }
        }
    }
}