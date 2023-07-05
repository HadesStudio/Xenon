package dev.hely.core.ranks.grants;

import dev.hely.core.Oxygen;
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
    private String rank_name, adder, remover, reason;
    private long added_time, removed_time, duration;
    private boolean active, permanent;
    private String server = "Global";

    private Oxygen plugin = Oxygen.getInstance();

    public boolean isActive() {
        if (isPermanent()) {
            return true;
        }

        return (added_time + duration) > System.currentTimeMillis();
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
            toReturn = DurationFormatUtils.formatDurationWords(added_time + duration - System.currentTimeMillis(), true, true);
        }
        return toReturn;
    }
}
