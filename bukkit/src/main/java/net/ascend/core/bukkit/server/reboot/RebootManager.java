package net.ascend.core.bukkit.server.reboot;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.configuration.Locale;
import net.ascend.core.bukkit.server.reboot.task.RebootTask;
import net.ascend.core.bukkit.lib.CC;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class RebootManager {

    @Getter private RebootTask task = null;

    public void reboot(long millis) {
        if (task != null) {
            return;
        }

        task = new RebootTask((int) TimeUnit.MILLISECONDS.toSeconds(millis));
        task.runTaskTimer(XenonBukkit.getInstance(), 20L, 20L);
    }

    public void cancel() {
        if (task == null)
            return;

        task.cancel();
        task = null;

        for (String message : Locale.REBOOT_CANCEL_ALERT) {
            Bukkit.broadcastMessage(CC.translate(message));
        }
    }

    public boolean isRebooting() {
        return task != null;
    }
}
