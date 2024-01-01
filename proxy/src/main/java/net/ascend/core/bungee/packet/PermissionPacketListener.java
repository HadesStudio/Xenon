package net.ascend.core.bungee.packet;

import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.profile.permission.packet.PlayerPermissionPacket;
import net.ascend.core.bungee.XenonBungee;
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

        XenonBungee.getInstance().getPlayerPermissions().put(uuid, permissions);
    }
}
