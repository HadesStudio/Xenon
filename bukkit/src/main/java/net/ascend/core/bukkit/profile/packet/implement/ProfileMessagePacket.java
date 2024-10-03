package net.ascend.core.bukkit.profile.packet.implement;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class ProfileMessagePacket implements Packet {

    private final Player player;
    private final String message;

}
