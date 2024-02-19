package net.ascend.core.bukkit.announcements.task;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.announcements.task
//   Date: Tuesday, September 05, 2023 - 3:48 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.announcements.AnnouncementManager;
import net.ascend.core.bukkit.lib.CC;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementTask extends BukkitRunnable {
    private XenonBukkit instance;
    private AnnouncementManager announcementManager;

    @Getter private List<List<String>> queue = new ArrayList<>();

    public AnnouncementTask(XenonBukkit instance, AnnouncementManager announcementManager) {
        this.instance = instance;
        this.announcementManager = announcementManager;

        this.queue.addAll(this.announcementManager.getTips().values());
    }

    @Override
    public void run() {
        if (this.queue.isEmpty()) {
            this.queue.addAll(this.announcementManager.getTips().values());
        }
        if (this.announcementManager.getTips().isEmpty()) {
            return;
        }

        final List<String> chosenTip = this.queue.remove(0);

        for (Player onlinePlayer : this.instance.getServer().getOnlinePlayers()) {
            for (String string : chosenTip) {
                onlinePlayer.sendMessage(CC.translate(string));
            }
        }
    }
}
