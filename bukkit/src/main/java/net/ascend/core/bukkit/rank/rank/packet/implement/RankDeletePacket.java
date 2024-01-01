package net.ascend.core.bukkit.rank.rank.packet.implement;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name Xenon
 */
@Getter
@AllArgsConstructor
public class RankDeletePacket implements Packet {

    private final String name;

}
