package dev.hely.core.bukkit.profile.notifications;

import com.google.gson.reflect.TypeToken;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.profile.Profile;
import lombok.Getter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class NotificationManager implements ModuleLoader {
    @Getter private final Map<String, Notification> notificationMap = new HashMap<>();
    private static final char[] CHARS = "0123456789".toCharArray();

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
            Notification notification = new Notification(OxygenBukkit.PLAIN_GSON.fromJson(document.getString("messages"), messageListType));
            notification.setId(id);
            notification.setDate(date);
            notification.setLast_update(last_update);

            this.notificationMap.put(id, notification);
        }
    }

    public Notification getNotificationByID(String id) {
        return this.notificationMap.get(id) == null ? new Notification(Collections.singletonList("Empty message.")) : this.notificationMap.get(id);
    }

    public int getProfilesWithNotification(Notification noti) {
        int amountType = 0;
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
            Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(document.getString("name"));

            OxygenBukkit.getInstance().debug_message("Found profile: " + profile.getName());

            if (profile.getNotifications() == null) {
                OxygenBukkit.getInstance().debug_message("The users notification is null, wtf xD?");
                break;
            }

            for (Notification notification : profile.getNotifications()) {
                OxygenBukkit.getInstance().debug_message("Registered a notification: #" + notification.getId());
                OxygenBukkit.getInstance().debug_message("Searching for: #" + noti.getId());

                if (notification.getId().equalsIgnoreCase(noti.getId())) {
                    OxygenBukkit.getInstance().debug_message("Adding +1");
                    amountType = amountType + 1;
                }
            }
        }
        return amountType;
    }

    public String getRandomID() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(CHARS[ThreadLocalRandom.current().nextInt(CHARS.length)]);
        }
        return code.toString();
    }
}
