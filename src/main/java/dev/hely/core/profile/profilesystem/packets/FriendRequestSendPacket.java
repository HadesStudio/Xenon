package dev.hely.core.profile.profilesystem.packets;

import dev.hely.core.Oxygen;
import dev.hely.core.modules.database.redis.packet.Packet;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class FriendRequestSendPacket implements Packet {
    private final String senderName;
    private final String targetName;

    public void notifyPlayer() {

        for (Player loopPlayer : Oxygen.getInstance().getServer().getOnlinePlayers()) {

            if (!loopPlayer.getName().equalsIgnoreCase(this.targetName)) {
                continue;
            }

            ComponentBuilder builder = (
                    new ComponentBuilder(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.added-friend-message").replace("%player%", senderName)))).
                    event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend add " + senderName)).
                    event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(Oxygen.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.hover-message").replace("%faction%", senderName))).create()));


            loopPlayer.getPlayer().spigot().sendMessage(builder.create());
        }
    }
}
