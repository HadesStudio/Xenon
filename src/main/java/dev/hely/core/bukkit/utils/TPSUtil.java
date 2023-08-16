package dev.hely.core.bukkit.utils;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.utils
//   Date: Wednesday, August 16, 2023 - 11:05 AM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class TPSUtil extends BukkitRunnable {
    private static int TICK_COUNT;
    private static long[] TICKS;

    public static double getTPS() {
        return getTPS(100);
    }

    public static double getTPS(final int ticks) {
        if (TPSUtil.TICK_COUNT < ticks) {
            return 20.0;
        }
        final int target = (TPSUtil.TICK_COUNT - 1 - ticks) % TPSUtil.TICKS.length;
        final long elapsed = System.currentTimeMillis() - TPSUtil.TICKS[target];
        return ticks / (elapsed / 1000.0);
    }

    public static String getTPSColor(double tps) {
        return ((tps > 18.0) ? ChatColor.GREEN : ((tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED))
                + ((tps > 20.0) ? "*" : "");
    }

    public void run() {
        TPSUtil.TICKS[TPSUtil.TICK_COUNT % TPSUtil.TICKS.length] = System.currentTimeMillis();
        ++TPSUtil.TICK_COUNT;
    }

    static {
        TPSUtil.TICK_COUNT = 0;
        TPSUtil.TICKS = new long[600];
    }
}
