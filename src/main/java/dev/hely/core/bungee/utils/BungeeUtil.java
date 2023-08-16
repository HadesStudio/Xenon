package dev.hely.core.bungee.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.utils
//   Date: Tuesday, August 15, 2023 - 6:50 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.hely.core.bukkit.OxygenBukkit;
import org.bukkit.entity.Player;

public class BungeeUtil {
    public static void sendToServer(Player player, String server) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server);

            player.sendPluginMessage(OxygenBukkit.getInstance(), "BungeeCord", out.toByteArray());
            player.sendMessage("You are being sent to " + server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
