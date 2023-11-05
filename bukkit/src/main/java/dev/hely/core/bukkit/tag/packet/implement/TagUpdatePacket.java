package dev.hely.core.bukkit.tag.packet.implement;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
@AllArgsConstructor
public class TagUpdatePacket implements Packet {

    private final String name;
    private final boolean remove;

}
