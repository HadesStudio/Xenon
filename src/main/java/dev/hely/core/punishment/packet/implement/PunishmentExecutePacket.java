package dev.hely.core.punishment.packet.implement;

import dev.hely.core.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 08/07/2023
 * Protect Name Oxygen
 */
@Getter
@AllArgsConstructor
public class PunishmentExecutePacket implements Packet {

    private UUID uniqueId;
    private String sender;
    private String target;

}
