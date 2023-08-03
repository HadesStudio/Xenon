package dev.hely.core.global.profile.packet.implement;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class FriendRequestAcceptPacket implements Packet {

    private final String senderName;
    private final String targetName;

    public void notifyPlayer() {
        for (Player onlinePlayers : OxygenBukkit.getInstance().getServer().getOnlinePlayers()) {
            if (!onlinePlayers.getName().equalsIgnoreCase(this.targetName)) continue;

            onlinePlayers.sendMessage("");
            onlinePlayers.sendMessage(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "New Friend");
            onlinePlayers.sendMessage(senderName + ChatColor.GRAY + " has accepted your friend request!");
            onlinePlayers.sendMessage("");
        }
    }
}
