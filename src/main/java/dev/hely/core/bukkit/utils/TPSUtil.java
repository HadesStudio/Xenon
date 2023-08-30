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

import dev.hely.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

public class TPSUtil {
    private static Object minecraftServer;
    private static Field recentTps;
    private static DecimalFormat SECONDS_FORMAT = new DecimalFormat("#0.0");

    private static double[] getTps() {
        try {
            if (minecraftServer == null) {
                Server server = Bukkit.getServer();
                Field consoleField = server.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                minecraftServer = consoleField.get(server);
            }
            if (recentTps == null) {
                recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
                recentTps.setAccessible(true);
            }
            return (double[]) recentTps.get(minecraftServer);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        return new double[]{-1, -1, -1};
    }

    private static String formatSeconds(long time) {
        return SECONDS_FORMAT.format(time / 1000.0F);
    }


    public static double[] getRecentTps() {
        double[] tps = getTps();
        if (tps[0] >= 20.0) tps[0] = 20.0;
        if (tps[1] >= 20.0) tps[1] = 20.0;
        if (tps[2] >= 20.0) tps[2] = 20.0;

        return new double[]{Math.round(tps[0]), Math.round(tps[1]), Math.round(tps[2])};
    }

    public static String getNiceTPS(double tps) {
        if (tps >= 20.0) return CC.translate("&a*" + tps);
        if (tps < 20.0 && tps > 18.0) return CC.translate("&a" + tps);
        if (tps < 18.0 && tps > 10.0) return CC.translate("&e" + tps);
        if (tps < 10.0 && tps > 5.0) return CC.translate("&c" + tps);
        return CC.translate("&4" + tps);
    }

    public static String getTPSStatus(double tps) {
        if (tps >= 20.0) return CC.translate("&a&oVery High");
        if (tps < 20.0 && tps > 18.0) return CC.translate("&aHigh");
        if (tps < 18.0 && tps > 10.0) return CC.translate("&eMedium");
        if (tps < 10.0 && tps > 5.0) return CC.translate("&cLow");
        return CC.translate("&4VERY Low");
    }
}