package net.ascend.core.bukkit.profile;

import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.profile.packet.implement.FriendRequestAcceptPacket;
import net.ascend.core.bukkit.profile.packet.implement.FriendRequestSendPacket;
import net.ascend.core.bukkit.util.UUIDUtil;
import net.ascend.core.bukkit.lib.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Xenon
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

    public boolean sendFriendRequest(Player sender, Profile senderProfile, Profile targetProfile) {
        if (targetProfile == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.not-real")));
            return false;
        }

        UUID targerUUID = targetProfile.getUniqueId();
        if (senderProfile.getFriends().contains(targerUUID)) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.already-friend")));
            return false;
        }

        if (senderProfile.getFriendRequests().stream().anyMatch(it -> it.toString().equalsIgnoreCase(targerUUID.toString()))) {
            acceptFriendRequest(senderProfile, sender, targerUUID);
            return true;
        }

        if (targetProfile.getFriendRequests().stream().anyMatch(it -> it.toString().equalsIgnoreCase(sender.getUniqueId().toString()))) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-add.already-outgoing").replace("%player%", targetProfile.getName())));
            return false;
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new FriendRequestSendPacket(sender.getName(), targetProfile.getName()));

        targetProfile.getFriendRequests().add(sender.getUniqueId());
        targetProfile.save();

        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("friends.friend-add.sent")) {
            sender.sendMessage(CC.translate(message.replace("%player%", targetProfile.getName())));
        }

        return true;
    }

    public boolean acceptFriendRequest(Profile senderProfile, Player sender, UUID target) {
        if (sender.getUniqueId().toString().equalsIgnoreCase(target.toString())) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.accept-self")));
            return false;
        }

        Profile targetProfile = XenonBukkit.getInstance().getProfileManager().getProfile(target);
        if (targetProfile == null) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.not-real")));
            return false;
        }

        if (senderProfile.getFriends().contains(target)) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.already-friend")));
            return false;
        }

        if (senderProfile.getFriendRequests().stream().noneMatch(it -> it.toString().equalsIgnoreCase(target.toString()))) {
            sender.sendMessage(CC.translate(XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getString("friends.friend-accept.no-request")));
            return false;
        }

        senderProfile.getFriends().add(target);
        senderProfile.getFriendRequests().remove(target);
        senderProfile.save();

        targetProfile.getFriends().add(sender.getUniqueId());
        targetProfile.getFriendRequests().remove(sender.getUniqueId());
        targetProfile.save();

        for (String message : XenonBukkit.getInstance().getLanguageConfig().getConfiguration().getStringList("friends.friend-accept.added")) {
            sender.sendMessage(CC.translate(message.replace("%player%", targetProfile.getName())));
        }

        XenonBukkit.getInstance().getRedisModule().sendPacket(new FriendRequestAcceptPacket(sender.getName(), targetProfile.getName()));
        return true;
    }
}
