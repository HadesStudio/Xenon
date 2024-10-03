package net.ascend.core.bukkit.profile.notifications.packet.implement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.ascend.core.bukkit.modules.database.redis.packet.Packet;

@Getter
@AllArgsConstructor
public class NotificationUpdatePacket implements Packet {

    private final String id;

}
