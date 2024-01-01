package net.ascend.core.bukkit.profile.notifications;

import com.google.gson.reflect.TypeToken;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.Profile;
import lombok.Getter;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class NotificationManager implements ModuleLoader {

    @Getter private final Map<String, Notification> notificationMap = new HashMap<>();
    private static final char[] CHARS = "0123456789".toCharArray();

    @Override
    public void onEnable() {
        registerNotifications();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {

    }

    public void registerNotifications() {
        for (Document document : XenonBukkit.getInstance().getMongoModule().getNotificationsCollection().find()) {
            String id = document.getString("notificationID");
            Long last_update = document.getLong("last_update");
            Long date = document.getLong("date");

            Type messageListType = new TypeToken<List<String>>() {}.getType();
            Notification notification = new Notification(XenonBukkit.PLAIN_GSON.fromJson(document.getString("messages"), messageListType));
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
        for (Document document : XenonBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(document.getString("name"));

            if (profile.getNotifications() == null) {
                break;
            }

            for (Notification notification : profile.getNotifications()) {
                if (notification.getId().equalsIgnoreCase(noti.getId())) {
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
