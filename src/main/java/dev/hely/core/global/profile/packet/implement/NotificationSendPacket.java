package dev.hely.core.global.profile.packet.implement;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import dev.hely.lib.CC;
import dev.hely.lib.sound.SoundManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class NotificationSendPacket implements Packet {

    private final String targetName;

    public void notifyPlayer() {
        for (Player onlinePlayers : OxygenBukkit.getInstance().getServer().getOnlinePlayers()) {
            if (!onlinePlayers.getName().equalsIgnoreCase(this.targetName)) continue;

            SoundManager.INSTANCE.playSound(onlinePlayers, "NOTE_PLING");

            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-system.notifications.new-notification")) {
                onlinePlayers.sendMessage(CC.translate(message));
            }
            break;
        }
    }
}
