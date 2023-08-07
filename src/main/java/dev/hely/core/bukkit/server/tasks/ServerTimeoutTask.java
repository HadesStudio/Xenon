package dev.hely.core.bukkit.server.tasks;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.tasks
//   Date: Monday, August 07, 2023 - 5:20 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.Server;
import dev.hely.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class ServerTimeoutTask implements Runnable {

    private static final long TIME_OUT_DELAY = 15_000L;

    @Override
    public void run() {
        List<Server> servers = OxygenBukkit.getInstance().getServerManager().getServers();

        for (Server server : servers) {
            Server serverData = OxygenBukkit.getInstance().getServerManager().getServer(server.getName());

            if (serverData != null) {
                if (System.currentTimeMillis() - serverData.getLastUpdate() >= ServerTimeoutTask.TIME_OUT_DELAY) {
                    OxygenBukkit.getInstance().debug_message("&4[Oxygen] &cThe server " + server.getName() + " has just stopped because it has crashed, or timed out!");
                    servers.remove(serverData);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.isOp()) {
                            player.sendMessage(CC.translate("&cThe server has stopped because it has crashed, or timed out."));
                            player.playSound(player.getLocation(), Sound.BURP, 2.0f, 2.0f);
                        }
                    }
                }
            }
        }
    }
}
