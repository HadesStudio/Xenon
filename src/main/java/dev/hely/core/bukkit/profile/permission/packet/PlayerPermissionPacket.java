package dev.hely.core.bukkit.profile.permission.packet;

import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 05/08/2023
 * Protect Name Oxygen
 */
@Getter
@AllArgsConstructor
public class PlayerPermissionPacket implements Packet {

    private final UUID uuid;
    private final List<String> permissions;

}
