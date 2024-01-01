package net.ascend.core.bukkit.profile.packet.implement;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.lib.CC;
import net.ascend.lib.sound.SoundManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class NotificationSendPacket implements Packet {

    private final String targetName;

    public void notifyPlayer() {
        for (Player onlinePlayers : XenonBukkit.getInstance().getServer().getOnlinePlayers()) {
            if (!onlinePlayers.getName().equalsIgnoreCase(this.targetName)) continue;

            SoundManager.INSTANCE.playSound(onlinePlayers, "NOTE_PLING");

            for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("profile-system.notifications.new-notification")) {
                onlinePlayers.sendMessage(CC.translate(message));
            }
            break;
        }
    }
}
