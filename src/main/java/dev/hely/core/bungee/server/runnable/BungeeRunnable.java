package dev.hely.core.bungee.server.runnable;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.server.runnable
//   Date: Monday, August 14, 2023 - 12:20 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.server.data.proxy.Proxy;
import dev.hely.core.bungee.server.data.proxy.data.ProxyServerRegion;
import dev.hely.core.bungee.server.packets.ProxyUpdatePacket;

public class BungeeRunnable implements Runnable {
    @Override
    public void run() {
        Proxy proxy = new Proxy(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("server-name"), ProxyServerRegion.valueOf(OxygenBungee.getInstance().getServerConfig().getConfiguration().getString("region")), OxygenBungee.getInstance());
        proxy.cache(OxygenBungee.getInstance().getRedisBungeeModule().getJedisPool());

        OxygenBungee.getInstance().getRedisBungeeModule().sendPacket(new ProxyUpdatePacket(proxy.getName(), proxy.getPort(), proxy.toJsonObject()));
    }
}