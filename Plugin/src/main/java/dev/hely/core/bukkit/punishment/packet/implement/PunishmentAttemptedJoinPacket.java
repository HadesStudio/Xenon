package dev.hely.core.bukkit.punishment.packet.implement;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import dev.hely.core.bukkit.punishment.PunishmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 08/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@AllArgsConstructor
public class PunishmentAttemptedJoinPacket implements Packet {
    private UUID uniqueId;
    private PunishmentType type;
}
