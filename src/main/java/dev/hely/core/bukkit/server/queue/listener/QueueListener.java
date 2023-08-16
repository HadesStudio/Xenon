package dev.hely.core.bukkit.server.queue.listener;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue.listener
//   Date: Tuesday, August 15, 2023 - 7:20 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QueueListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        Profile data = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (data == null) {
            return;
        }

        OxygenBukkit.getInstance().getTasks().runAsync(() -> OxygenBukkit.getInstance().getQueueManager().removePlayer(player.getUniqueId()));
    }
}
