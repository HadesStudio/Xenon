package net.ascend.core.bukkit.util;

import net.ascend.core.bukkit.lib.CC;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

public class TPSUtil {
    private static Object minecraftServer;
    private static Field recentTps;

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

    public static double[] getRecentTps() {
        double[] tps = getTps();
        if (tps[0] >= 20.0) tps[0] = 20.0;
        if (tps[1] >= 20.0) tps[1] = 20.0;
        if (tps[2] >= 20.0) tps[2] = 20.0;

        return new double[]{Math.round(tps[0]), Math.round(tps[1]), Math.round(tps[2])};
    }


    public static String getTPSStatus(double tps) {
        if (tps >= 20.0) return CC.translate("&a&oVery High");
        if (tps < 20.0 && tps > 18.0) return CC.translate("&aHigh");
        if (tps < 18.0 && tps > 10.0) return CC.translate("&eMedium");
        if (tps < 10.0 && tps > 5.0) return CC.translate("&cLow");
        return CC.translate("&4VERY Low");
    }
}