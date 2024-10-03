package net.ascend.core.bukkit.punishment;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.lib.CC;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bson.Document;

import java.util.UUID;

@Data
@Setter
@NoArgsConstructor
public class Punishment {

    private PunishmentType type;
    private UUID uniqueId, targetUUID, addedBy;
    private String addedReason, addedServer, removedBy, removedReason = "Expired Punishment.";
    private long addedAt, removedAt, duration = -1;
    private boolean silent = true, clear = false, removed = false;

    private XenonBukkit plugin = XenonBukkit.getInstance();

    public void save() {
        Document document = new Document();

        document.put("uniqueId", uniqueId.toString());
        document.put("type", type.name());
        document.put("targetUUID", targetUUID.toString());

        if (addedBy != null) {
            document.put("addedBy", addedBy.toString());
        }

        document.put("addedServer", addedServer);
        document.put("addedReason", addedReason);
        document.put("addedAt", addedAt);
        document.put("silent", silent);
        document.put("clear", clear);
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

        plugin.getMongoModule().getPunishmentsCollection().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, new ReplaceOptions().upsert(true));
    }

    public void load(Document document) {
        if (document != null) {
            setType(PunishmentType.valueOf(document.getString("type")));
            setUniqueId(UUID.fromString(document.getString("uniqueId")));
            setTargetUUID(UUID.fromString(document.getString("targetUUID")));

            if (document.containsKey("addedBy")) {
                setAddedBy(UUID.fromString(document.getString("addedBy")));
            }

            setAddedServer(document.getString("addedServer"));
            setAddedReason(document.getString("addedReason"));
            setAddedAt(document.getLong("addedAt"));
            setSilent(document.getBoolean("silent"));
            setClear(document.getBoolean("clear"));
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

    public String getAddedByString() {
        String adder = CC.translate("&4Console");

        if (this.addedBy != null) {
            adder = "&c&oUnknown";

            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(this.addedBy);

            if (profile != null) {
                adder = profile.getActiveRank().getDisplay_color() + profile.getName();
            }
        }

        return (adder);
    }

    public String getRemovedByString() {
        String remover = CC.translate("&4Console");

        if (this.removedBy != null) {
            remover = "&c&oUnknown";

            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(this.removedBy);

            if (profile != null) {
                remover = profile.getActiveRank().getDisplay_color() + profile.getName();
            }
        }

        return (remover);
    }

    public String getKey(boolean undo) {
        switch (type) {
            case BLACKLIST: {
                if (undo) return silent ? "un-blacklist-silent" : "un-blacklist";
                return !silent ? "blacklist" : "blacklist-silent";
            }

            case BAN: {
                if (undo) return silent ? "unban-silent" : "unban";
                if (isPermanent()) return silent ? "ban-silent" : "ban";
                return silent ? "temp-ban-silent" : "temp-ban";
            }

            case MUTE: {
                if (undo) return silent ? "unmute-silent" : "unmute";
                if (isPermanent()) return silent ? "mute-silent" : "mute";
                return silent ? "temp-mute-silent" : "temp-mute";
            }

            case WARN: {
                if (isPermanent()) return silent ? "warn-silent" : "warn";
                return silent ? "temp-warn-silent" : "temp-warn";
            }

            case KICK: {
                return !silent ? "kick" : "kick-silent";
            }

            default:
                return "";
        }
    }
}
