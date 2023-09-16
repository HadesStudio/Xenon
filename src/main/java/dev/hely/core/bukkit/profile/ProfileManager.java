package dev.hely.core.bukkit.profile;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.packet.implement.FriendRequestAcceptPacket;
import dev.hely.core.bukkit.profile.packet.implement.FriendRequestSendPacket;
import dev.hely.core.bukkit.profile.packet.implement.NotificationSendPacket;
import dev.hely.core.bukkit.profile.profilesystem.impl.Notification;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.StatisticType;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.Statistics;
import dev.hely.core.bukkit.profile.profilesystem.impl.serverstats.StatisticsServer;
import dev.hely.core.bukkit.punishment.Punishment;
import dev.hely.core.bukkit.utils.UUIDUtil;
import dev.hely.lib.util.CC;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name OxygenGlobal
 */
public class ProfileManager {

    @Getter private Map<UUID, Profile> profileMap = new HashMap<>();

    public Profile getProfile(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null && player.isOnline()) {
            return getProfile(player.getUniqueId());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (offlinePlayer.hasPlayedBefore()) {
            return getProfile(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        }

        UUID uuid = UUIDUtil.fetchUUID(name);
        if (uuid != null) {
            return getProfile(uuid, name);
        }
        return null;
    }

    public Profile getProfile(UUID uuid) {
        return this.profileMap.get(uuid) == null ? new Profile(uuid, null) : this.profileMap.get(uuid);
    }

    public Profile getProfile(UUID uuid, String name) {
        if (this.profileMap.containsKey(uuid)) return this.profileMap.get(uuid);

        this.profileMap.put(uuid, new Profile(uuid, name));
        return this.profileMap.get(uuid);
    }

    public boolean sendNotification(UUID uuid, String... message) {
        return sendNotification(uuid, Arrays.asList(message));
    }

    public boolean sendNotificationAll(List<String> message) {
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
            UUID uuid = UUID.fromString(document.getString("uniqueId"));
            Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(uuid);

            if (playerProfile == null) {
                break;
            }

            if (playerProfile.getNotifications().size() > 50) {
                playerProfile.getNotifications().remove(playerProfile.getNotifications().size()-1);
            }

            OxygenBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(playerProfile.getName()));

            playerProfile.getNotifications().add(new Notification(uuid, java.lang.System.currentTimeMillis(), message));
            playerProfile.save();
        }
        return true;
    }

    public boolean clearNotificationsAll(String... message) {
        for (Document document : OxygenBukkit.getInstance().getMongoModule().getProfilesCollection().find()) {
            UUID uuid = UUID.fromString(document.getString("uniqueId"));
            Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(uuid);

            if (playerProfile == null) {
                break;
            }

            playerProfile.getNotifications().clear();
            playerProfile.save();
        }
        return true;
    }

    public boolean sendNotification(UUID uuid, List<String> message) {
        Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(uuid);

        if (playerProfile == null) {
            return false;
        }

        if (playerProfile.getNotifications().size() > 50) {
            playerProfile.getNotifications().remove(playerProfile.getNotifications().size()-1);
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(Bukkit.getPlayer(uuid).getName()));

        playerProfile.getNotifications().add(new Notification(uuid, System.currentTimeMillis(), message));
        playerProfile.save();
        return true;
    }

    public boolean sendFriendRequest(Player sender, UUID target) {
        /*if (sender.getUniqueId().toString().equalsIgnoreCase(target.toString())) {
            sender.sendMessage(CC.translate(OxygenGlobal.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.friend-self")));
            return false;
        }*/

        Profile senderProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(sender.getUniqueId());
        Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(target);

        if (targetProfile == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.not-real")));
            return false;
        }

        if (senderProfile.getFriends().contains(target)) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.already-friend")));
            return false;
        }

        if (senderProfile.getFriendRequests().stream().anyMatch(it -> it.toString().equalsIgnoreCase(target.toString()))) {
            acceptFriendRequest(senderProfile, sender, target);
            return true;
        }

        if (targetProfile.getFriendRequests().stream().anyMatch(it -> it.toString().equalsIgnoreCase(sender.getUniqueId().toString()))) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.already-outgoing").replace("%player%", targetProfile.getName())));
            return false;
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new FriendRequestSendPacket(sender.getName(), targetProfile.getName()));

        targetProfile.getFriendRequests().add(sender.getUniqueId());
        targetProfile.save();

        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("friends.friend-add.sent")) {
            sender.sendMessage(CC.translate(message.replace("%player%", targetProfile.getName())));
        }

        return true;
    }

    public boolean acceptFriendRequest(Profile senderProfile, Player sender, UUID target) {
        if (sender.getUniqueId().toString().equalsIgnoreCase(target.toString())) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.accept-self")));
            return false;
        }

        Profile targetProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(target);
        if (targetProfile == null) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.not-real")));
            return false;
        }

        if (senderProfile.getFriends().contains(target)) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.already-friend")));
            return false;
        }

        if (senderProfile.getFriendRequests().stream().noneMatch(it -> it.toString().equalsIgnoreCase(target.toString()))) {
            sender.sendMessage(CC.translate(OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.no-request")));
            return false;
        }

        senderProfile.getFriends().add(target);
        senderProfile.getFriendRequests().remove(target);
        senderProfile.save();

        targetProfile.getFriends().add(sender.getUniqueId());
        targetProfile.getFriendRequests().remove(sender.getUniqueId());
        targetProfile.save();

        for (String message : OxygenBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("friends.friend-accept.added")) {
            sender.sendMessage(CC.translate(message.replace("%player%", targetProfile.getName())));
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new FriendRequestAcceptPacket(sender.getName(), targetProfile.getName()));
        return true;
    }

    public boolean addStatistic(UUID target, StatisticsServer statisticServer, StatisticType statisticType, int amount) {
        Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(target);

        if (playerProfile == null) {
            return false;
        }

        playerProfile.getStatistics().putIfAbsent(statisticServer, new Statistics(statisticServer));

        Statistics statistics = playerProfile.getStatistics().get(statisticServer);
        if (statistics == null) {
            return false;
        }

        statistics.addPlayerStatistic(statisticType, amount);
        playerProfile.calculatePlayerStatistics();
        playerProfile.save();
        return true;
    }

    public boolean sendBanPunishmentNotification(UUID uuid, Punishment punishment) {
        Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(uuid);

        List<String> list = OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("punishments.send-notifications.ban-notification");

        if (playerProfile.getNotifications().size() > 50) {
            playerProfile.getNotifications().remove(playerProfile.getNotifications().size()-1);
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(playerProfile.getName()));

        playerProfile.getNotifications().add(new Notification(uuid, System.currentTimeMillis(), list));
        playerProfile.save();
        return true;
    }

    public boolean sendMutePunishmentNotification(UUID uuid, Punishment punishment) {
        Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(uuid);

        List<String> list = OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("punishments.send-notifications.mute-notification");

        if (playerProfile.getNotifications().size() > 50) {
            playerProfile.getNotifications().remove(playerProfile.getNotifications().size()-1);
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(playerProfile.getName()));


        playerProfile.getNotifications().add(new Notification(uuid, System.currentTimeMillis(), list));
        playerProfile.save();
        return true;
    }

    public boolean sendWarningPunishmentNotification(UUID uuid, Punishment punishment) {
        Profile playerProfile = OxygenBukkit.getInstance().getProfileManager().getProfile(uuid);

        List<String> list = OxygenBukkit.getInstance().getConfiguration().getConfiguration().getStringList("punishments.send-notifications.warning-notification");

        if (playerProfile.getNotifications().size() > 50) {
            playerProfile.getNotifications().remove(playerProfile.getNotifications().size()-1);
        }

        OxygenBukkit.getInstance().getRedisModule().sendPacket(new NotificationSendPacket(playerProfile.getName()));


        playerProfile.getNotifications().add(new Notification(uuid, System.currentTimeMillis(), list));
        playerProfile.save();
        return true;
    }
}
