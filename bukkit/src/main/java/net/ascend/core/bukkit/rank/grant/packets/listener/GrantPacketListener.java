package net.ascend.core.bukkit.rank.grant.packets.listener;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.packets.GrantUpdatePacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class GrantPacketListener implements PacketListener {
    @IncomingPacketHandler
    public void onGrantUpdate(GrantUpdatePacket packet) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(packet.getProfile());

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
