package dev.hely.core.tag.packet.implement;

import dev.hely.core.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Oxygen
 */
@Getter
@AllArgsConstructor
public class TagUpdatePacket implements Packet {

    private final String name;
    private final boolean remove;

}
