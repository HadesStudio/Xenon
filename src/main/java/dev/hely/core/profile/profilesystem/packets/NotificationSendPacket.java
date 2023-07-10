package dev.hely.core.profile.profilesystem.packets;

import dev.hely.core.Oxygen;
import dev.hely.core.modules.database.redis.packet.Packet;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class NotificationSendPacket implements Packet {
    private final String targetName;

    public void notifyPlayer() {
        for (Player loopPlayer : Oxygen.getInstance().getServer().getOnlinePlayers()) {
            if (!loopPlayer.getName().equalsIgnoreCase(this.targetName)) {
                continue;
            }

            loopPlayer.playSound(loopPlayer.getLocation(), Sound.NOTE_PLING, 1, 1);

            for (String message : Oxygen.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-system.notifications.new-notification")) {
                loopPlayer.sendMessage(CC.translate(message));
            }
            break;
        }
    }
}
