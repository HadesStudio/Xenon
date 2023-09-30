package dev.hely.core.bukkit.ranks.newgrants.packets.listener;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.ranks.newgrants.packets.GrantUpdatePacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class GrantPacketListener implements PacketListener {
    @IncomingPacketHandler
    public void onGrantUpdate(GrantUpdatePacket packet) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(packet.getProfile());

        if (profile != null) {
            if (Bukkit.getPlayer(packet.getProfile()) != null && Bukkit.getPlayer(packet.getProfile()).isOnline()) {
                profile.load();

                profile.updateGrants();
                profile.activateNextRankGrant();

                profile.setupPermissionAttachments(Bukkit.getPlayer(packet.getProfile()));
            }
        }
    }
}
