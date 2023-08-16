package dev.hely.core.bukkit.server.runnable;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.runnable
//   Date: Tuesday, August 15, 2023 - 5:35 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.lib.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class HeartBeatRunnable extends BukkitRunnable {
    private static final long TIME_OUT_DELAY = 15_000L;

    @Override
    public void run() {
        for (Server servers : OxygenBukkit.getInstance().getServerManager().getServers()) {
            Server server = OxygenBukkit.getInstance().getServerManager().serverFromName(servers.getName());

            if (System.currentTimeMillis() - server.getLastHeartBeat() >= TIME_OUT_DELAY) {
                Bukkit.getOnlinePlayers().stream().filter(player -> player.isOp()
                        && OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()) != null).forEach(player -> {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("server-manager.server-offline").replace("%server%", server.getName())));
                    player.playSound(player.getLocation(), Sound.BURP, 2.0f, 2.0f);
                });
            }
        }
    }
}
