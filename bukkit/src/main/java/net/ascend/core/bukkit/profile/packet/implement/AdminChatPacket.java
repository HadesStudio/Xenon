package net.ascend.core.bukkit.profile.packet.implement;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 12/07/2023
 * Protect Name Xenon
 */
@Getter
@AllArgsConstructor
public class AdminChatPacket implements Packet {

    private final String playerName;
    private final String serverName;
    private final String message;

}
