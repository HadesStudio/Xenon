package net.ascend.core.bukkit.server.reboot.task;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.util.TimeUtil;
import net.ascend.core.bukkit.lib.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class RebootTask extends BukkitRunnable {

    private static final List<? extends Number> ANNOUNCE_TIMES = Arrays.asList(
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
            for (String message : Locale.REBOOT_INFO_MESSAGE) {
                String formattedMessage = message.replace("%time%", TimeUtil.getMoreDetailedTime(timeBeforeReboot * 1000L));

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
