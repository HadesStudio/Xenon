package dev.hely.core.leandro.grants;

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
    private String rank, addedBy, removedBy, addedReason;
    private long addedAt, removedAt, duration;
    private boolean active, permanent;
    private String server = "Global";

    private Oxygen plugin = Oxygen.getInstance();

    public boolean isActive() {
        if (isPermanent()) {
            return true;
        }

        return (addedAt + duration) > System.currentTimeMillis();
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
            toReturn = DurationFormatUtils.formatDurationWords(addedAt + duration - System.currentTimeMillis(), true, true);
        }
        return toReturn;
    }
}
