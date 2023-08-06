package dev.hely.core.bukkit.tasks;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.server
//   Date: Sunday, July 30, 2023 - 8:54 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.NameMCUtils;
import dev.hely.lib.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NameMCReminderTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player onlinePlayer : OxygenBukkit.getInstance().getServer().getOnlinePlayers()) {
            if (!NameMCUtils.isVerified(onlinePlayer.getUniqueId())) {
                for (String message : OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("name-mc.messages.reminder")) {
                    onlinePlayer.sendMessage(CC.translate(message).replace("%player%", onlinePlayer.getName()));
                }
            }
        }
    }
}
