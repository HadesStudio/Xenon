package net.ascend.core.bukkit.profile.notifications;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.Profile;
import lombok.Getter;
import net.ascend.core.bukkit.profile.notifications.packet.implement.NotificationDeletePacket;
import net.ascend.core.bukkit.profile.notifications.packet.implement.NotificationSendPacket;
import net.ascend.core.bukkit.punishment.Punishment;
import net.ascend.core.bukkit.util.TimeUtil;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class NotificationManager implements ModuleLoader {

    @Getter private final Map<String, Notification> notificationMap = new HashMap<>();
    private static final char[] CHARS = "0123456789".toCharArray();

    @Override
    public void onEnable() {
        registerNotifications();
    }

    @Override
    public void onDisable() {
        for (Notification notification : this.notificationMap.values()) {
            notification.save();
        }
    }

    @Override
    public void onReload() {

    }

    public void registerNotifications() {
        this.notificationMap.clear();

        for (Document document : XenonBukkit.getInstance().getMongoModule().getNotificationsCollection().find()) {
            if (document != null) {

                String id = document.getString("uniqueId");
                Long lastUpdate = document.getLong("lastUpdate");
                Long date = document.getLong("date");

                Type messageListType = new TypeToken<List<String>>() {}.getType();
                Notification notification = new Notification(XenonBukkit.PLAIN_GSON.fromJson(document.getString("messages"), messageListType));
                notification.setId(id);
                notification.setDate(date);
                notification.setLastUpdate(lastUpdate);

                this.notificationMap.put(id, notification);
            }
        }
    }

    public void removeNotification(Notification notification) {
        Document document = XenonBukkit.getInstance().getMongoModule().getNotificationsCollection().find(Filters.eq("name", notification.getId())).first();
        if (document != null) {
            XenonBukkit.getInstance().getMongoModule().getNotificationsCollection().deleteOne(document);
        }

        this.notificationMap.remove(notification.getId());
        XenonBukkit.getInstance().getRedisModule().sendPacket(new NotificationDeletePacket(notification.getId()));
    }


    public boolean sendNotification(UUID uuid, String id, String... message) {
        return sendNotification(uuid, Arrays.asList(message), id);
    }

    public boolean sendNotificationAll(String id, String... message) {
        return sendNotificationAll(Arrays.asList(message), id);
    }

    public boolean sendNotificationAll(List<String> message, String id) {
        for (Document document : XenonBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
            UUID uuid = UUID.fromString(document.getString("uniqueId"));
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);

            if (profile == null) {
                break;
            }

            if (profile.getNotifications().size() > 50) {
                profile.getNotifications().remove(profile.getNotifications().size()-1);
            }

            XenonBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(profile.getName()));


            Notification notification = new Notification(message);
            notification.setId(id);
            profile.getNotifications().add(notification);
            profile.save();
        }
        return true;
    }

    public boolean sendNotification(UUID uuid, List<String> message, String id) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);
        if (profile == null) return false;

        if (profile.getNotifications().size() > 50) {
            profile.getNotifications().remove(profile.getNotifications().size()-1);
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(Bukkit.getPlayer(uuid).getName()));

        Notification notification = new Notification(message);
        notification.setId(id);

        profile.getNotifications().add(notification);
        profile.save();
        return true;
    }

    public boolean sendBanPunishmentNotification(UUID uuid, Punishment punishment) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);
        if (profile == null) return false;

        if (profile.getNotifications().size() > 50) {
            profile.getNotifications().remove(profile.getNotifications().size()-1);
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(profile.getName()));

        List<String> list = XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("punishments.send-notifications.warning-notification");
        List<String> replacedList = list.stream().map(message -> message
                        .replace("%addedBy%", punishment.getAddedByString())
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%duration%", punishment.isPermanent() ? "Permanent" : TimeUtil.getMoreDetailedTime(punishment.getDuration()))
                        .replace("%server%", punishment.getAddedServer()))
                .collect(Collectors.toList());


        profile.getNotifications().add(new Notification(replacedList));
        profile.save();
        return true;
    }

    public boolean sendMutePunishmentNotification(UUID uuid, Punishment punishment) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);
        if (profile == null) return false;

        if (profile.getNotifications().size() > 50) {
            profile.getNotifications().remove(profile.getNotifications().size()-1);
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(profile.getName()));

        List<String> list = XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("punishments.send-notifications.warning-notification");
        List<String> replacedList = list.stream().map(message -> message
                        .replace("%addedBy%", punishment.getAddedByString())
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%duration%", punishment.isPermanent() ? "Permanent" : TimeUtil.getMoreDetailedTime(punishment.getDuration()))
                        .replace("%server%", punishment.getAddedServer()))
                .collect(Collectors.toList());


        profile.getNotifications().add(new Notification(replacedList));
        profile.save();
        return true;
    }

    public boolean sendWarningPunishmentNotification(UUID uuid, Punishment punishment) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(uuid);
        if (profile == null) return false;

        if (profile.getNotifications().size() > 50) {
            profile.getNotifications().remove(profile.getNotifications().size()-1);
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(profile.getName()));

        List<String> list = XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("punishments.send-notifications.warning-notification");
        List<String> replacedList = list.stream().map(message -> message
                        .replace("%addedBy%", punishment.getAddedByString())
                        .replace("%reason%", punishment.getAddedReason())
                        .replace("%duration%", punishment.isPermanent() ? "Permanent" : TimeUtil.getMoreDetailedTime(punishment.getDuration()))
                        .replace("%server%", punishment.getAddedServer()))
                .collect(Collectors.toList());


        profile.getNotifications().add(new Notification(replacedList));
        profile.save();
        return true;
    }


    public int getProfilesWithNotification(Notification notification) {
        int amountType = 0;
        for (Document document : XenonBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
            Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(document.getString("name"));

            if (profile == null || profile.getNotifications() == null) {
                continue;
            }

            for (Notification notifications : profile.getNotifications()) {
                if (notifications.getId().equalsIgnoreCase(notification.getId())) {
                    amountType++;
                    break;
                }
            }
        }
        return amountType;
    }

    public Notification getNotificationByID(String id) {
        return this.notificationMap.get(id) == null ? new Notification(Collections.singletonList("Empty message.")) : this.notificationMap.get(id);
    }

    public String getRandomID() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(CHARS[ThreadLocalRandom.current().nextInt(CHARS.length)]);
        }

        return code.toString();
    }
}
