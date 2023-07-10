package dev.hely.core.profile.profilesystem.packets;

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
    private final String targetName;
    private final String serverName;
    private final boolean disconnect;

    public void notifyPlayers() {
        Profile playerProfile = Oxygen.getInstance().getProfileManager().getProfile(targetName);

        if (playerProfile == null) {
            return;
        }

        for (Player loopPlayer : Oxygen.getInstance().getServer().getOnlinePlayers()) {
            if (!playerProfile.getFriends().contains(loopPlayer.getUniqueId())) {
                continue;
            }

            if (disconnect) {
                loopPlayer.sendMessage(CC.translate("&4&lFriends &8┃ &f" + playerProfile.getName() + " &7has left &f" + serverName + "&7."));
            } else {
                loopPlayer.sendMessage(CC.translate("&4&lFriends &8┃ &f" + playerProfile.getName() + " &7has joined &f" + serverName + "&7."));
            }
        }

    }


}
