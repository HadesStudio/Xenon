package dev.hely.core.ranks.grants;

import dev.hely.core.Oxygen;
import dev.hely.core.ranks.rank.Rank;
import lombok.*;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
@Getter
@Setter
@NoArgsConstructor
public class Grant {

    private UUID uniqueId;
    private String rankName, adder, remover, reason;
    private long addedTime, removedTime, duration;
    private boolean active, permanent;
    private String server = "Global";

    public boolean isActive() {
        if (isPermanent()) {
            return true;
        }

        return (addedTime + duration) > System.currentTimeMillis();
    }

    public String getRemaining() {
        String toReturn;

        if (!active) {
            toReturn = "Expired";
        } else if (isPermanent()) {
            toReturn = "Never";
        } else if (!isActive()) {
            toReturn = "Expired";
        } else {
            toReturn = DurationFormatUtils.formatDurationWords(addedTime + duration - System.currentTimeMillis(), true, true);
        }
        return toReturn;
    }

    public Rank getRank() {
        Rank rankData = Oxygen.getInstance().getRankManager().getRank(rankName);
        if (rankData != null) return rankData;
        return null;
    }
}
