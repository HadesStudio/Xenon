package dev.hely.core.bungee.packet;

import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bukkit.profile.permission.packet.PlayerPermissionPacket;
import dev.hely.core.bungee.OxygenBungee;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 05/08/2023
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class PermissionPacketListener implements PacketListener {

    @IncomingPacketHandler
    public void onPlayerPermission(PlayerPermissionPacket packet){
        UUID uuid = packet.getUuid();
        List<String> permissions = packet.getPermissions();

        OxygenBungee.getInstance().getPlayerPermissions().put(uuid, permissions);
    }
}
