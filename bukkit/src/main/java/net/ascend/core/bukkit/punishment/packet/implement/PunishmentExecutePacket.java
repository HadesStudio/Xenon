package net.ascend.core.bukkit.punishment.packet.implement;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
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
public class PunishmentExecutePacket implements Packet {

    private UUID uniqueId;
    private String sender;
    private UUID target;

}
