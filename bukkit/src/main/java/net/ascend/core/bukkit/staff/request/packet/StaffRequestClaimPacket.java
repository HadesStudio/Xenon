package net.ascend.core.bukkit.staff.request.packet;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StaffRequestClaimPacket implements Packet {

    private final String playerName;
    private final String creatorName;

}
