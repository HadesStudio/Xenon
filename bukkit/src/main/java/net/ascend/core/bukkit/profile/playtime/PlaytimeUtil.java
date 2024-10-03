package net.ascend.core.bukkit.profile.playtime;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.utils
//   Date: Thursday, August 24, 2023 - 7:22 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import lombok.Getter;

@Getter
public class PlaytimeUtil {

    private final String name;
    private long startMillis = -1;
    private long endMillis = -1;
    private boolean running = false;

    public PlaytimeUtil(String name) {
        this.name = name;
    }

    public void startTimings() {
        this.startMillis = System.currentTimeMillis();
        this.running = true;
    }

    public void stopTimings() {
        this.endMillis = System.currentTimeMillis();
        this.running = false;
    }

    public long calculateDifference() {
        if ((startMillis == -1) && (endMillis == -1)) {
            return 0;
        }
        if (isRunning()) {
            return System.currentTimeMillis() - startMillis;
        }
        return this.endMillis - this.startMillis;
    }
}
