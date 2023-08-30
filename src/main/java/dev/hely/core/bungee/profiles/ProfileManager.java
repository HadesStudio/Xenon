package dev.hely.core.bungee.profiles;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.profiles
//   Date: Tuesday, August 29, 2023 - 8:40 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.utils.UUIDUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
}
