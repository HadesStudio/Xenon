package net.ascend.core.bukkit.server.reboot;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.server.reboot
//   Date: Tuesday, September 12, 2023 - 3:42 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.server.reboot.task.RebootTask;
import net.ascend.lib.CC;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class RebootManager {
    @Getter private RebootTask task = null;

    public void reboot() { // if no time is specified
        if (task != null) {
            return;
        }

        task = new RebootTask((int) TimeUnit.SECONDS.toSeconds(60));
        task.runTaskTimer(XenonBukkit.getInstance(), 20L, 20L);
    }

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

        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("reboot.canceled")) {
            Bukkit.broadcastMessage(CC.translate(message));
        }
    }

    public boolean isRebooting() {
        return task != null;
    }
}
