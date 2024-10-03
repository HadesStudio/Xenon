package net.ascend.core.bukkit.profile.permission;

import net.ascend.core.bukkit.XenonBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Xenon
 */
@Getter
@Setter
@AllArgsConstructor
public class ProfilePermission {

    private String permission, server;
    private boolean global;
    private long duration, addedAt;

    public boolean isActive() {
        if (global) return true;
        if (server == null) return false;
        if (!isPermanent()) {
            return (addedAt + duration) > System.currentTimeMillis();
        }

        return XenonBukkit.getInstance().getServerManager().getCurrentServerName().equalsIgnoreCase(this.server);
    }

    public boolean isExpired() {
        if (isPermanent()) {
            return false;
        }

        return (addedAt + duration) <= System.currentTimeMillis();
    }

    public boolean isPermanent() {
        return duration == -1;
    }

    public String getRemaining() {
        String toReturn;

         if (isPermanent()) {
            toReturn = "Permanent";
        } else if (!isActive()) {
            toReturn = "Expired";
        } else {
            toReturn = DurationFormatUtils.formatDurationWords(addedAt + duration - System.currentTimeMillis(), true, true);
        }
        return toReturn;
    }
}
