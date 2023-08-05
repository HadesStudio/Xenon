package dev.hely.core.bukkit.ranks.rank.packet.implement;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 14/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@AllArgsConstructor
public class RankDeletePacket implements Packet {

    private final String name;

}
