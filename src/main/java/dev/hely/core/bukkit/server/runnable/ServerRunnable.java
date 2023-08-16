package dev.hely.core.bukkit.server.runnable;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server
//   Date: Monday, August 14, 2023 - 12:54 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bungee.server.data.server.Server;
import dev.hely.core.bungee.server.data.server.data.StatusData;
import dev.hely.core.bungee.server.packets.ServerUpdatePacket;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerRunnable extends BukkitRunnable {

    @Override
    public void run() {
        Server server = new Server(OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("server-name"), OxygenBukkit.getInstance().getServerConfig().getConfiguration().getString("group"), OxygenBukkit.getInstance(), false);
        server.setStartupTime(server.getStartupTime());

        if (OxygenBukkit.getInstance().isStopping()) {
            server.setStatus(StatusData.OFFLINE);
        }

        server.cache(OxygenBukkit.getInstance().getRedisModule().getJedisPool());

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new ServerUpdatePacket(server.getName(), server.getPort(), server.toJsonObject()));
    }
}
