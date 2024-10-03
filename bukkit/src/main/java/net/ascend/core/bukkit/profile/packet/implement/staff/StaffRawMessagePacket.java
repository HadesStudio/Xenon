package net.ascend.core.bukkit.profile.packet.implement.staff;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StaffRawMessagePacket implements Packet {

    private final String permission;
    private final String serverName;
    private final String message;

}
