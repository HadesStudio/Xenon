package dev.hely.core.bukkit.ranks.newgrants;

import com.google.common.collect.Lists;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.TimeUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Grant {
    private final UUID uuid;

    private long duration;
    private List<String> servers = Lists.newArrayList();

    private UUID addedBy;
    private String addedReason;
    private long addedAt;

    private boolean removed;
    private UUID removedBy;
    private String removedReason;
    private long removedAt;

    public boolean isGlobal() {
        return servers.isEmpty();
    }

    public boolean canActivate() {
        if (isGlobal()) {
            return true;
        } else {
            return servers.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(OxygenBukkit.getInstance().getServer().getName().toLowerCase());
        }
    }

    public boolean isPermanent() {
        return duration == Integer.MAX_VALUE;
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public long getRemaining() {
        return (addedAt + duration) - System.currentTimeMillis();
    }

    public String getAddedDate() {
        return TimeUtil.formatIntoCalendarString(new Date(this.addedAt));
    }

    public String getRemovedDate() {
        return TimeUtil.formatIntoCalendarString(new Date(this.removedAt));
    }

    public String getExpirationDate() {
        return (this.isPermanent() ? "Never" : TimeUtil.formatIntoCalendarString(new Date(this.addedAt + this.duration)));
    }

    public String getExpiration(boolean simple) {
        if(isPermanent()) {
            return "Never";
        }

        if(hasExpired()) {
            return "Expired";
        }

        if(simple) {
            return TimeUtil.formatTimeMillis(getRemaining());
        }

        return TimeUtil.readableTime(getRemaining());
    }

    public String getDurationText(boolean simple) {
        if(isPermanent()) {
            return "Permanent";
        }

        if(simple) {
            return TimeUtil.formatTimeMillis(duration);
        }

        return TimeUtil.readableTime(duration);
    }

    public String getAddedByString() {
        String addedBy = "&4Console";

        if (this.addedBy != null) {
            addedBy = "&cUnknown";

            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(this.getAddedBy());

            if (profile != null) {
                addedBy = profile.getColoredName();
            }
        }

        return (addedBy);
    }

    public String getRemovedByString() {
        String removedBy = "&4Console";

        if (this.removedBy != null) {
            removedBy = "&c&oUnknown";

            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(this.getAddedBy());

            if (profile != null) {
                removedBy = profile.getColoredName();
            }
        }

        return (removedBy);
    }
}