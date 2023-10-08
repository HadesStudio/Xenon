package dev.hely.core.bukkit.punishment.packet.implement;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 13/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@AllArgsConstructor
public class PunishmentRemovePacket implements Packet {

    private UUID uniqueId;
    private String sender;
    private UUID target;

}
