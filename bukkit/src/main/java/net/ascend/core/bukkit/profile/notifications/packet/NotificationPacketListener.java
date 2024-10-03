package net.ascend.core.bukkit.profile.notifications.packet;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.profile.notifications.Notification;
import net.ascend.core.bukkit.profile.notifications.packet.implement.NotificationDeletePacket;
import net.ascend.core.bukkit.profile.notifications.packet.implement.NotificationSendPacket;
import net.ascend.core.bukkit.profile.notifications.packet.implement.NotificationUpdatePacket;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class NotificationPacketListener implements PacketListener {

    @IncomingPacketHandler
    public void onNotificationSend(NotificationSendPacket packet) {
        packet.notifyPlayer();
    }

    @IncomingPacketHandler
    public void onNotificationUpdate(NotificationUpdatePacket packet) {
        String id = packet.getId();

        Notification notification = XenonBukkit.getInstance().getNotificationManager().getNotificationByID(id);

        if (notification == null) {
            XenonBukkit.getInstance().getNotificationManager().getNotificationMap().put(id, new Notification(Collections.singletonList("Empty message.")));
            notification = XenonBukkit.getInstance().getNotificationManager().getNotificationByID(id);
        }

        Document document = XenonBukkit.getInstance().getMongoModule().getNotificationsCollection().find().filter(Filters.eq("uniqueId", id)).first();
        if (document != null) {
            Long lastUpdate = document.getLong("lastUpdate");
            Long date = document.getLong("date");

            Type messageListType = new TypeToken<List<String>>() {}.getType();
            notification.setMessage(XenonBukkit.PLAIN_GSON.fromJson(document.getString("messages"), messageListType));
            notification.setId(id);
            notification.setDate(date);
            notification.setLastUpdate(lastUpdate);
        }
    }

    @IncomingPacketHandler
    public void onNotificationDelete(NotificationDeletePacket packet) {
        String id = packet.getId();

        XenonBukkit.getInstance().getNotificationManager().getNotificationMap().remove(id);
    }
}
