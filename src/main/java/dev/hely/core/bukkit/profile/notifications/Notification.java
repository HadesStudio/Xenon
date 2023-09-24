package dev.hely.core.bukkit.profile.notifications;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.log.category.LogCategories;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import dev.hely.core.bukkit.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class Notification {
    private static final char[] CHARS = "0123456789".toCharArray();

    private String id;
    private List<String> message;
    private Long date;
    private Long last_update;

    public Notification(List<String> message) {
        this.id = getRandomID();
        this.message = message;
        this.date = System.currentTimeMillis();
        this.last_update = System.currentTimeMillis();
    }


    public void save() {
        save(false);
    }

    public void save(boolean leave) {
        Document document = new Document();
        document.put("notificationID", id);
        Type messageListType = new TypeToken<List<String>>() {}.getType();
        document.put("message", OxygenBukkit.PLAIN_GSON.toJson(this.message, messageListType));
        document.put("date", date);
        document.put("last_update", last_update);

        OxygenBukkit.getInstance().getMongoModule().getLogsCollection().replaceOne(Filters.eq("notificationID", id), document, new ReplaceOptions().upsert(true));
        if (leave) {
            OxygenBukkit.getInstance().getNotificationManager().getNotificationMap().remove(id);
        }
    }

    private void load() {
        Document document = OxygenBukkit.getInstance().getMongoModule().getNotificationsCollection().find(Filters.eq("notificationID", id.toString())).first();

        if (document != null) {
            id = document.getString("notificationID");
            Type messageListType = new TypeToken<List<String>>() {}.getType();
            message = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("message"), messageListType);
            date = document.getLong("date");
            last_update = document.getLong("last_update");
        }
    }

    public int getProfilesWithNotification() {
        int amountType = 0;
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
            OxygenBukkit.getInstance().debug_message("Found profile: " + document.getString("name"));

            Type notificationsType = new TypeToken<List<String>>() {}.getType();
            List<String> type = OxygenBukkit.PLAIN_GSON.fromJson(document.getString("notifications"), notificationsType);

            if (type == null) {
                OxygenBukkit.getInstance().debug_message("The users notification is null, wtf xD?");
                break;
            }

            if (type.contains(this.id)) {
                OxygenBukkit.getInstance().debug_message("The users notifications contains this notification!");
                amountType = amountType + 1;
            }
        }
        return amountType;
    }

    private String getRandomID() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(CHARS[ThreadLocalRandom.current().nextInt(CHARS.length)]);
        }
        return code.toString();
    }
}

