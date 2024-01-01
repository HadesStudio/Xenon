package net.ascend.core.bukkit.punishment.packet.implement;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.core.bukkit.punishment.PunishmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 08/07/2023
 * Protect Name Xenon
 */
@Getter
@AllArgsConstructor
public class PunishmentAttemptedJoinPacket implements Packet {
    private UUID uniqueId;
    private PunishmentType type;
}
