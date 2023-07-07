package dev.hely.core.profile;

import dev.hely.core.utils.UUIDUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created By LeandroSSJ
 * Created on 29/06/2023
 * Protect Name Oxygen
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
        return this.profileMap.get(uuid);
    }

    public Profile getProfile(UUID uuid, String name) {
        if (this.profileMap.containsKey(uuid)) return this.profileMap.get(uuid);

        this.profileMap.put(uuid, new Profile(uuid, name));
        return this.profileMap.get(uuid);
    }
}
