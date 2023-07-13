package dev.hely.core.profile.permission;

import dev.hely.core.Oxygen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
@AllArgsConstructor
public class ProfilePermission {

    private String permission, server;
    private boolean global;
    private long duration;

    public boolean isActive() {
        if (global) return true;
        if (server == null) return false;
        if (!isPermanent()) if (System.currentTimeMillis() > this.duration) return false;

        return Oxygen.getInstance().getServerManager().getServerName().equalsIgnoreCase(this.server);
    }

    public boolean isExpired() {
        if (this.duration != -1 && this.duration != 0) return this.duration < System.currentTimeMillis();
        return false;
    }

    public boolean isPermanent() {
        return duration == -1;
    }
}
