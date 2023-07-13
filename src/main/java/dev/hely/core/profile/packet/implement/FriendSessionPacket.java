package dev.hely.core.profile.packet.implement;

import dev.hely.core.Oxygen;
import dev.hely.core.modules.database.redis.packet.Packet;
import dev.hely.core.profile.Profile;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class FriendSessionPacket implements Packet {

    private final String playerName;
    private final String serverName;
    private final boolean disconnect;

    public void notifyPlayers() {
        Profile profile = Oxygen.getInstance().getProfileManager().getProfile(playerName);
        if (profile == null) return;

        for (Player onlinePlayers : Oxygen.getInstance().getServer().getOnlinePlayers()) {
            if (!profile.getFriends().contains(onlinePlayers.getUniqueId())) continue;

            if (disconnect) {
                onlinePlayers.sendMessage(CC.translate("&4&lFriends &8┃ &f" + profile.getName() + " &7has left &f" + serverName + "&7."));
            } else {
                onlinePlayers.sendMessage(CC.translate("&4&lFriends &8┃ &f" + profile.getName() + " &7has joined &f" + serverName + "&7."));
            }
        }
    }
}
