package dev.hely.core.bukkit.server.reboot.task;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.reboot.task
//   Date: Tuesday, September 12, 2023 - 3:44 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.utils.TimeUtil;
import dev.hely.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class RebootTask extends BukkitRunnable {
    private static final List<? extends Number> ANNOUNCE_TIMES = Arrays.asList( // measured in seconds puta
            60 * 60,
            45 * 60,
            30 * 60,
            15 * 60,
            10 * 60,
            5 * 60,
            4 * 60,
            3 * 60,
            2 * 60,
            60,
            50,
            45,
            40,
            35,
            30,
            25,
            20,
            15,
            10,
            5
    );

    private int timeBeforeReboot;

    @Override
    public void run() {
        if (ANNOUNCE_TIMES.contains(timeBeforeReboot)) {
            for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("reboot.rebooting")) {
                String formattedMessage = message.replace("%time%", TimeUtil.readableTime(timeBeforeReboot));
                Bukkit.broadcastMessage(CC.translate(formattedMessage));
            }
        }
        if (timeBeforeReboot == 0) {
            cancel();
            Bukkit.getServer().shutdown();
            return;
        }

        --timeBeforeReboot;
    }
}
