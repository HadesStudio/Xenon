package dev.hely.core.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.utils
//   Date: Sunday, July 30, 2023 - 8:48 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class NameMCUtils {
    @SneakyThrows
    public static boolean isVerified(UUID uuid) {
        return new Scanner(new URL(Oxygen.getInstance().getConfiguration().getConfiguration().getString("name-mc.link") + uuid.toString()).openStream()).nextBoolean();
    }

    public static void runCommands(Player player) {
        for (String commands : Oxygen.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.commands")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("%player%", player.getName()));
        }
    }
}
