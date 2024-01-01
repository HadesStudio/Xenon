package net.ascend.core.bukkit.profile.packet.implement;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
@Getter
@AllArgsConstructor
public class StaffSwitchPacket implements Packet {

    private final String playerName;
    private final String toServer;
    private final String fromServer;

}