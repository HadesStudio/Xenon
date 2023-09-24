package dev.hely.core.bukkit.profile.notifications;

import com.google.gson.reflect.TypeToken;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.log.Log;
import dev.hely.core.bukkit.profile.log.types.LogActionTypes;
import lombok.Getter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.*;

public class NotificationManager implements ModuleLoader {
    @Getter private final Map<String, Notification> notificationMap = new HashMap<>();

    @Override
    public void enable() {
        registerNotifications();
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return "Notification";
    }

    public void registerNotifications() {
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getNotificationsCollection().find()) {
            String id = document.getString("notificationID");
            Long last_update = document.getLong("last_update");
            Long date = document.getLong("date");

            Type messageListType = new TypeToken<List<String>>() {}.getType();
            Notification notification = new Notification(OxygenBukkit.PLAIN_GSON.fromJson(document.getString("message"), messageListType));
            notification.setId(id);
            notification.setDate(date);
            notification.setLast_update(last_update);

            this.notificationMap.put(id, notification);
        }
    }

    public Notification getNotificationByID(String id) {
        return this.notificationMap.get(id) == null ? new Notification(Collections.singletonList("Empty message.")) : this.notificationMap.get(id);
    }


}
