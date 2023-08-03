package dev.hely.core.global.profile.packet.implement;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 12/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@AllArgsConstructor
public class StaffChatPacket implements Packet {

    private final String playerName;
    private final String serverName;
    private final String message;

}
