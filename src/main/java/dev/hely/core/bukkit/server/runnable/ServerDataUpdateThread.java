package dev.hely.core.bukkit.server.runnable;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.runnable
//   Date: Wednesday, August 30, 2023 - 4:08 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.packets.ServerUpdatePacket;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerDataUpdateThread extends Thread {
    private OxygenBukkit plugin;

    @Override
    public void run() {
        while (true) {
            try {
                check();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sleep(50 * 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void check() {
        if (plugin.isStopping()) return;

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new ServerUpdatePacket(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), "Online", System.currentTimeMillis()));

        this.plugin.getServerManager().removeClosedServers();
    }
}
