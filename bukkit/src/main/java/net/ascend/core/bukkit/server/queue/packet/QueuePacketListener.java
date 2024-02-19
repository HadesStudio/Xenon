package net.ascend.core.bukkit.server.queue.packet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueJoinPacket;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueLeavePacket;
import net.ascend.core.bukkit.server.queue.packet.implement.QueuePlayerLeavePacket;
import net.ascend.core.bukkit.server.queue.packet.implement.QueueSendPlayerPacket;
import net.ascend.core.bukkit.lib.CC;
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

        player.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("queue-system.sent").replace("%server%", packet.getQueueName())));
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(packet.getQueueName());
        player.sendPluginMessage(XenonBukkit.getInstance(), "BungeeCord", out.toByteArray());
    }

    @IncomingPacketHandler
    public void onPlayerLeave(QueuePlayerLeavePacket packet) {
        if (XenonBukkit.getInstance().getQueue().getPlayers().contains(packet.getUuid())) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(packet.getUuid());
            if (profile == null) {
                return;
            }

            XenonBukkit.getInstance().getQueue().removePlayer(profile);
        }
    }

    @IncomingPacketHandler
    public void onQueueLeave(QueueLeavePacket packet) {
        if (XenonBukkit.getInstance().getServerData().getName().equals(packet.getQueueName())) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(packet.getPlayerUuid());
            if (profile == null) {
                return;
            }

            XenonBukkit.getInstance().getQueue().removePlayer(profile);
        }
    }

    @IncomingPacketHandler
    public void onQueueJoin(QueueJoinPacket packet) {
        if (XenonBukkit.getInstance().getServerData().getName().equals(packet.getQueueName())) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(packet.getPlayerUuid());
            if (profile == null) {
                return;
            }

            XenonBukkit.getInstance().getQueue().addPlayer(profile);
        }
    }

    @IncomingPacketHandler
    public void onQueuePositionNotification(QueuePositionNotificationPacket packet) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(packet.getPlayer());
        if (profile == null) {
            return;
        }
        Player player = Bukkit.getPlayer(packet.getPlayer());
        if (player != null && player.isOnline()) {
            for (String message : packet.getMessage()) {
                player.sendMessage(CC.translate(message.replace("%position%", String.valueOf(packet.getPosition())).replace("%queue_size%", String.valueOf(packet.getQueueSize()))));
            }
        }

        XenonBukkit.getInstance().getQueue().addPlayer(profile);
    }
}