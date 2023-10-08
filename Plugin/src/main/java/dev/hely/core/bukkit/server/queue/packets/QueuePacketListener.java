package dev.hely.core.bukkit.server.queue.packets;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.server.queue.packets.implement.*;
import dev.hely.lib.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class QueuePacketListener implements PacketListener {
    @IncomingPacketHandler
    public void onLoadQueues(QueueSendPlayerPacket packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        if (player == null) {
            return;
        }

        player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.sent").replace("%server%", packet.getQueueName())));
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(packet.getQueueName());
        player.sendPluginMessage(OxygenBukkit.getInstance(), "BungeeCord", out.toByteArray());
    }

    @IncomingPacketHandler
    public void onPlayerLeave(QueuePlayerLeavePacket packet) {
        if (OxygenBukkit.getInstance().getQueue().getPlayers().contains(packet.getUuid())) {
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(packet.getUuid());
            if (profile == null) {
                return;
            }

            OxygenBukkit.getInstance().getQueue().removePlayer(profile);
        }
    }

    @IncomingPacketHandler
    public void onQueueLeave(QueueLeavePacket packet) {
        if (OxygenBukkit.getInstance().getServerData().getName().equals(packet.getQueueName())) {
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(packet.getPlayerUuid());
            if (profile == null) {
                return;
            }

            OxygenBukkit.getInstance().getQueue().removePlayer(profile);
        }
    }

    @IncomingPacketHandler
    public void onQueueJoin(QueueJoinPacket packet) {
        if (OxygenBukkit.getInstance().getServerData().getName().equals(packet.getQueueName())) {
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(packet.getPlayerUuid());
            if (profile == null) {
                return;
            }

            OxygenBukkit.getInstance().getQueue().addPlayer(profile);
        }
    }

    @IncomingPacketHandler
    public void onQueuePositionNotification(QueuePositionNotificationPacket packet) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(packet.getPlayer());
        if (profile == null) {
            return;
        }
        Player player = Bukkit.getPlayer(packet.getPlayer());
        if (player != null && player.isOnline()) {
            for (String message : packet.getMessage()) {
                player.sendMessage(CC.translate(message.replace("%position%", String.valueOf(packet.getPosition())).replace("%queue_size%", String.valueOf(packet.getQueueSize()))));
            }
        }

        OxygenBukkit.getInstance().getQueue().addPlayer(profile);
    }
}