package net.ascend.core.bukkit.profile.packet.implement;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.lib.CC;
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
        for (Player onlinePlayers : XenonBukkit.getInstance().getServer().getOnlinePlayers()) {
            if (!onlinePlayers.getName().equalsIgnoreCase(this.targetName)) continue;

            ComponentBuilder builder = (
                    new ComponentBuilder(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.added-friend-message").replace("%player%", senderName)))).
                    event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend add " + senderName)).
                    event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.hover-message").replace("%player%", senderName))).create()));

            onlinePlayers.getPlayer().spigot().sendMessage(builder.create());
        }
    }
}
