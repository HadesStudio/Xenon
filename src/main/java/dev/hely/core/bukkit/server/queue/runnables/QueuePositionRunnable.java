package dev.hely.core.bukkit.server.queue.runnables;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.queue.runnables
//   Date: Tuesday, August 15, 2023 - 8:53 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.lib.CC;
import org.bukkit.scheduler.BukkitRunnable;

public class QueuePositionRunnable extends BukkitRunnable {
    @Override
    public void run() {
        OxygenBukkit.getInstance().getServer().getOnlinePlayers().forEach(o -> {
            Queue queue = Queue.getByUuid(o.getUniqueId());

            if (queue != null) {
                o.sendMessage("");
                o.sendMessage(CC.translate("Your position is " + '#' + queue.getPosition(o.getUniqueId()) + " out of " + queue.getPlayers().size() + " in the " + queue.getFormattedName() + " queue."));

                Profile data = OxygenBukkit.getInstance().getProfileManager().getProfile(o.getUniqueId());

                if (data != null) {
                    o.sendMessage(CC.translate("You can always purchase rank!! puta"));
                }

                o.sendMessage("");
            }
        });
    }
}
