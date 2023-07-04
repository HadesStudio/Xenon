package dev.hely.core.ranks.permissions;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.permissions
//   Date: Tuesday, July 04, 2023 - 5:35 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.utils.TimeFormatUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Permission {

    private String permission, server;
    private boolean global;
    private long expire;

    public boolean isAvailable() {
        if (global) return true;
        if (server == null) return false;
        if (expire != -1) {
            if (System.currentTimeMillis() > this.expire) return false;
        }
        return Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-name").equalsIgnoreCase(this.server);
    }

    public String getExpire() {
        if (this.expire != -1 && this.expire != 0) {
            if (this.expire >= System.currentTimeMillis()) {
                return TimeFormatUtils.getMoreDetailedTime(this.expire - System.currentTimeMillis());
            } else {
                return "Expired";
            }
        }
        return "Never";
    }

    public boolean isExpired() {
        if (this.expire != -1 && this.expire != 0) {
            return this.expire < System.currentTimeMillis();
        }
        return false;
    }
}

