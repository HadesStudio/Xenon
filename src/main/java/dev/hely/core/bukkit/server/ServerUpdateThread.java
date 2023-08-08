package dev.hely.core.bukkit.server;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.server.packet.implement.ServerAlertUpdatePacket;
import dev.hely.core.bukkit.server.packet.implement.ServerUpdatePacket;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

/**
 * Created By LeandroSSJ
 * Created on 06/08/2023
 * Protect Name Oxygen
 */
@AllArgsConstructor
public class ServerUpdateThread extends Thread {

    private OxygenBukkit plugin;

    @Override
    public void run() {
        while (true) {
            try {
                plugin.getRedisModule().sendPacket(new ServerUpdatePacket(
                        plugin.getServerManager().getServerName(),
                        plugin.getServerManager().getServerVersion(),
                        plugin.getLAST_START_TIME(),
                        20.0D,
                        Bukkit.hasWhitelist(),
                        Bukkit.getMaxPlayers(),
                        Bukkit.getOnlinePlayers().size()
                ));

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
}
