package dev.hely.core.punishment;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.Oxygen;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bson.Document;

import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 03/07/2023
 * Protect Name Oxygen
 */

@Data
@Setter
@NoArgsConstructor
public class Punishment {

    private PunishmentType type;
    private UUID uniqueId, targetUUID;
    private String addedBy, removedBy, addedReason, removedReason;
    private long addedAt, removedAt, duration = -1;
    private boolean silent = true, removed = false;

    private Oxygen plugin = Oxygen.getInstance();

    public void save() {
        Document document = new Document();

        document.put("uniqueId", uniqueId.toString());
        document.put("type", type.name());
        document.put("targetUUID", targetUUID.toString());
        document.put("addedBy", addedBy);
        document.put("addedReason", addedReason);
        document.put("addedAt", addedAt);
        document.put("silent", silent);
        document.put("removed", removed);

        if (removedBy != null) {
            document.put("removedBy", removedBy);
        }

        if (removedReason != null) {
            document.put("removedReason", removedReason);
        }

        if (removedAt > 0L) {
            document.put("removedAt", removedAt);
        }

        if (duration > 0L) {
            document.put("duration", duration);
        }

        plugin.getMongoManager().getPunishmentsCollection().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, new ReplaceOptions().upsert(true));
    }

    public void load(Document document) {
        if (document != null) {
            setType(PunishmentType.valueOf(document.getString("type")));
            setUniqueId(UUID.fromString(document.getString("uniqueId")));
            setTargetUUID(UUID.fromString(document.getString("targetUUID")));
            setAddedBy(document.getString("addedBy"));
            setAddedReason(document.getString("addedReason"));
            setAddedAt(document.getLong("addedAt"));
            setSilent(document.getBoolean("silent"));
            setRemoved(document.getBoolean("removed"));

            if (document.containsKey("removedBy")) {
                setRemovedBy(document.getString("removedBy"));
            }

            if (document.containsKey("removedReason")) {
                setRemovedReason(document.getString("removedReason"));
            }

            if (document.containsKey("removedAt")) {
                setRemovedAt(document.getLong("removedAt"));
            }

            if (document.containsKey("duration")) {
                setDuration(document.getLong("duration"));
            }
        }
    }

    public boolean isBan() {
        return type == PunishmentType.BAN || type == PunishmentType.BLACKLIST;
    }

    public boolean isMute() {
        return type == PunishmentType.MUTE;
    }

    public boolean isPermanent() {
        return duration == -1;
    }

    public boolean isActive() {
        if (removed) {
            return false;
        }

        if (isPermanent()) {
            return true;
        }

        return (addedAt + duration) > System.currentTimeMillis();
    }

    public String getRemaining() {
        String toReturn;

        if (removed) {
            toReturn = "Removed";
        } else if (isPermanent()) {
            toReturn = "Permanent";
        } else if (!isActive()) {
            toReturn = "Expired";
        } else {
            toReturn = DurationFormatUtils.formatDurationWords(addedAt + duration - System.currentTimeMillis(), true, true);
        }
        return toReturn;
    }
}
