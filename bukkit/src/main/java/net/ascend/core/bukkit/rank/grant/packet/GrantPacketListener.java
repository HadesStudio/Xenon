package net.ascend.core.bukkit.rank.grant.packet;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.rank.grant.packet.impl.GrantUpdatePacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class GrantPacketListener implements PacketListener {

    @IncomingPacketHandler
    public void onGrantUpdate(GrantUpdatePacket packet) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(packet.getProfile());

        if (profile != null) {
            if (profile.getPlayer() != null && profile.getPlayer().isOnline()) {
                profile.load();

                profile.updateGrants();
                profile.activateNextRankGrant();

                profile.setupPermissionAttachments(profile.getPlayer());
            }
        }
    }
}
