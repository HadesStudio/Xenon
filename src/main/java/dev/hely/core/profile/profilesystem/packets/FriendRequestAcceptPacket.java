package dev.hely.core.profile.profilesystem.packets;

import dev.hely.core.Oxygen;
import dev.hely.core.modules.database.redis.packet.Packet;
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
        for (Player loopPlayer : Oxygen.getInstance().getServer().getOnlinePlayers()) {
            if (!loopPlayer.getName().equalsIgnoreCase(this.targetName)) {
                continue;
            }

            loopPlayer.sendMessage("");
            loopPlayer.sendMessage(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "New Friend");
            loopPlayer.sendMessage(senderName + ChatColor.GRAY + " has accepted your friend request!");
            loopPlayer.sendMessage("");
        }
    }
}
