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

import dev.hely.core.bukkit.server.queue.Queue;
import dev.hely.core.bukkit.server.queue.QueuePlayer;
import dev.hely.core.bukkit.server.Server;
import dev.hely.core.bungee.utils.BungeeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MainQueueRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (Queue queue : Queue.getQueues()) {
            Server serverData = queue.getServerData();

            if (serverData == null || !queue.isEnabled() || (!(serverData.getStatus().equalsIgnoreCase("Offline")) || (serverData.getStatus().equalsIgnoreCase("Whitelisted")))) {
                continue;
            }

            QueuePlayer next = queue.getPlayers().poll();

            if(next != null) {
                Player player = Bukkit.getPlayer(next.getUuid());

                if(player != null) {
                    BungeeUtil.sendToServer(player, queue.getName());
                }
            }
        }
    }
}
