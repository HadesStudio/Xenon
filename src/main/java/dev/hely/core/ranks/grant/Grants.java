package dev.hely.core.ranks.grant;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.ranks.grant
//   Date: Tuesday, July 04, 2023 - 4:48 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.Oxygen;
import dev.hely.core.managers.server.Server;
import dev.hely.core.ranks.Ranks;
import dev.hely.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public class Grants {
    private transient Oxygen plugin = Oxygen.getInstance();

    private String rankName;
    private long addedAt, duration, removedAt;
    private String addedBy, reason, removedBy;
    private boolean active, permanent;
    private String server = "Global";

    public boolean hasExpired() {
        if (this.getRank() == null) return true;

        if (this.server.equalsIgnoreCase("Global") || this.server.equalsIgnoreCase(Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-name"))) {
            if (!this.isActive()) return true;
            if (plugin.getRankManager().getRank(this.rankName) == null) return true;
            if (this.isPermanent()) return false;
            return System.currentTimeMillis() >= this.addedAt + this.duration;
        }
        return true;
    }

    public boolean isActiveSomewhere() {
        if (!this.isActive()) return false;
        if (plugin.getRankManager().getRank(this.rankName) == null) return false;

        if (!this.server.equalsIgnoreCase("Global")) {
            Server serverData = plugin.getServerManager().getServerData(this.server);
            if (serverData != null && !serverData.getServer_name().equalsIgnoreCase(Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-name"))) {
                if (isPermanent()) return true;

                return System.currentTimeMillis() < this.addedAt + this.duration;
            }
        } else if (this.getRank(false) != null && this.getRank(false).isActiveSomewhere() && !this.getRank(false).isGlobal()) {
            if (isPermanent()) return true;
            return System.currentTimeMillis() < this.addedAt + this.duration;
        }
        return false;
    }

    public String getNiceDuration() {
        if (isPermanent()) return "Permanent";

        return DateUtils.formatTimeMillis(this.duration);
    }

    public String getNiceExpire() {
        if (!isActive()) return "Expired";
        if (isPermanent()) return "Never";
        if (hasExpired()) return "Expired";

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        from.setTime(new Date(System.currentTimeMillis()));
        to.setTime(new Date(this.addedAt + this.getDuration()));

        return DateUtils.formatDateDiff(from, to);
    }

    public Ranks getRank(boolean old) {
        return Oxygen.getInstance().getRankManager().getRank(this.rankName);
    }

    public Ranks getRank() {
        Ranks rankData = this.getRank(true);

        if (rankData != null && rankData.isAvailableOnServer()) {
            return rankData;
        }
        return null;
    }

    public int getRankWeight() {
        Ranks data = this.getRank();

        return data != null ? data.getWeight() : 0;
    }
}
