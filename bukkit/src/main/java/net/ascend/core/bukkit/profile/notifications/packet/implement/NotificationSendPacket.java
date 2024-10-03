package net.ascend.core.bukkit.profile.notifications.packet.implement;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.sound.SoundManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class NotificationSendPacket implements Packet {

    private final String name;

    public void notifyPlayer() {
        Player player = Bukkit.getPlayer(name);
        if (player == null || !player.isOnline()) return;


        SoundManager.INSTANCE.playSound(player, "NOTE_PLING");

        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("notification.new-notification")) {
            player.sendMessage(CC.translate(message));
        }
    }
}
