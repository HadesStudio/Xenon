package net.ascend.core.bukkit.client.listeners;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.client.listeners
//   Date: Saturday, July 15, 2023 - 8:12 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.comphenix.protocol.ProtocolLibrary;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.client.impl.Version;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VersionListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int pVersion = ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
        for(Version version : Version.values()) {

            if(pVersion == version.getProtocolNumber()) {
                XenonBukkit.getInstance().getClientManager().getVersions().put(player.getUniqueId(), version);
            }
        }
        if (!XenonBukkit.getInstance().getClientManager().getVersions().containsKey(player.getUniqueId())) {
            XenonBukkit.getInstance().getClientManager().getVersions().put(player.getUniqueId(), Version.UNKNOWN);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(PlayerQuitEvent event) {
        XenonBukkit.getInstance().getClientManager().getVersions().remove(event.getPlayer().getUniqueId());
    }
}
