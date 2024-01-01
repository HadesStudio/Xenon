package net.ascend.core.bukkit.announcements;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.announcements
//   Date: Tuesday, September 05, 2023 - 3:38 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.announcements.task.AnnouncementTask;
import net.ascend.core.bukkit.modules.ModuleLoader;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnouncementManager implements ModuleLoader {

    public boolean enabled;
    @Getter private final Map<Integer, List<String>> tips = new HashMap<>();
    @Getter @Setter private int interval;
    @Getter @Setter private AnnouncementTask announcementTask;

    @Override
    public void onEnable() {
        enabled = true;

        this.interval = XenonBukkit.getInstance().getAnnouncementConfig().getConfiguration().getInt("announcements.delay", 300);
        this.announcementTask = new AnnouncementTask(XenonBukkit.getInstance(), this);
        this.announcementTask.runTaskTimer(XenonBukkit.getInstance(), 20L * interval, 20L * interval);

        this.loadTips();
    }

    @Override
    public void onDisable() {
        enabled = false;
    }

    @Override
    public void onReload() {

    }

    public void loadTips() {
        if (XenonBukkit.getInstance().getAnnouncementConfig().getConfiguration().get("announcements") == null) {
            return;
        }

        for (String key : XenonBukkit.getInstance().getAnnouncementConfig().getConfiguration().getConfigurationSection("announcements").getKeys(false)) {
            List<String> tip = XenonBukkit.getInstance().getAnnouncementConfig().getConfiguration().getStringList("announcements." + key + ".text");

            tips.put(tips.size() + 1, tip);
        }
    }
}
