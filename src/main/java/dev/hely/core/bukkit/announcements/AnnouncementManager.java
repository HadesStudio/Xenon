package dev.hely.core.bukkit.announcements;

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

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.announcements.task.AnnouncementTask;
import dev.hely.core.bukkit.modules.ModuleLoader;
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
    public void enable() {
        enabled = true;

        this.interval = OxygenBukkit.getInstance().getAnnouncementConfig().getConfiguration().getInt("announcements.delay", 300);
        this.announcementTask = new AnnouncementTask(OxygenBukkit.getInstance(), this);
        this.announcementTask.runTaskTimer(OxygenBukkit.getInstance(), 20L * interval, 20L * interval);

        this.loadTips();
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "Announcement";
    }

    public void loadTips() {
        if (OxygenBukkit.getInstance().getAnnouncementConfig().getConfiguration().get("announcements") == null) {
            OxygenBukkit.getInstance().debug_message("&cTheir was an error loading your announcements, supposedly the main section is null?");
            return;
        }

        for (String key : OxygenBukkit.getInstance().getAnnouncementConfig().getConfiguration().getConfigurationSection("announcements").getKeys(false)) {
            List<String> tip = OxygenBukkit.getInstance().getAnnouncementConfig().getConfiguration().getStringList("announcements." + key + ".text");

            tips.put(tips.size() + 1, tip);
        }
    }
}
