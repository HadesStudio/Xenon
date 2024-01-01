package net.ascend.core.bukkit.profile.permission.packet;

import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 05/08/2023
 * Protect Name Xenon
 */
@Getter
@AllArgsConstructor
public class PlayerPermissionPacket implements Packet {

    private final UUID uuid;
    private final List<String> permissions;

}
