package dev.hely.core;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit
//   Date: Thursday, August 03, 2023 - 4:52 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.utils.servers.ServerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class OxygenGlobal extends JavaPlugin {
    public ServerType systemType = ServerType.UNKNOWN;
    public OxygenGlobal instance;
    public static String serverName = "Oxygen";

    @Override
    public void onEnable() {
        setSystemType(currentServerType());
        System.out.print("We have detected that you are running Oxygen on: " + getSystemType().name());
    }

    private static ServerType currentServerType() {
        try {
            Class.forName("net.md_5.bungee.BungeeCord");
            return ServerType.BUNGEE;
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("org.bukkit.Bukkit");
            return ServerType.BUKKIT;
        } catch (ClassNotFoundException ignored) {}
        return ServerType.UNKNOWN;
    }

    public static String getSystemName() {
        switch (currentServerType()) {
            case BUNGEE:
            case BUKKIT: {
                return serverName;
            }
            default: {
                if (serverName != null && !serverName.isEmpty())
                    return "Oxygen";

                return "Custom Java Applet";
            }
        }
    }
}
