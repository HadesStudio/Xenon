package net.ascend.core.bukkit.profile.notifications;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.ascend.core.bukkit.XenonBukkit;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class Notification {

    private String id;
    private List<String> message;
    private long date;
    private long lastUpdate;

    public Notification(List<String> message) {
        this.id = XenonBukkit.getInstance().getNotificationManager().getRandomID();
        this.message = message;
        this.date = System.currentTimeMillis();
        this.lastUpdate = System.currentTimeMillis();
    }

    public void save() {
        Document document = new Document();
        document.put("uniqueId", id);

        Type messageListType = new TypeToken<List<String>>() {
        }.getType();
        document.put("messages", XenonBukkit.PLAIN_GSON.toJson(this.message, messageListType));
        document.put("date", date);
        document.put("lastUpdate", lastUpdate);

        XenonBukkit.getInstance().getMongoModule().getNotificationsCollection().replaceOne(Filters.eq("uniqueId", id), document, new ReplaceOptions().upsert(true));
    }
}

